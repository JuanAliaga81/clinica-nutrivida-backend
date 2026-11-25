package cl.nutrivida.usuarios.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.nutrivida.usuarios.dto.LoginUsuarioDTO;
import cl.nutrivida.usuarios.dto.RegistroUsuarioDTO;
import cl.nutrivida.usuarios.model.Persona;
import cl.nutrivida.usuarios.model.Usuario;
import cl.nutrivida.usuarios.repo.PersonaRepository;
import cl.nutrivida.usuarios.repo.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró el usuario con ID " + id));
    }

    @Transactional
    public Usuario registrar(RegistroUsuarioDTO datos) {
        String correo = datos.correo() == null ? "" : datos.correo().trim().toLowerCase();
        String rut = datos.rut() == null ? "" : datos.rut().trim().toUpperCase();
        String nombre = datos.nombre() == null ? "" : datos.nombre().trim();
        String celular = datos.celular() == null ? "" : datos.celular().replaceAll("\\D", "");
        String sexo = datos.sexo() == null ? "" : datos.sexo().trim().toUpperCase();

        if (nombre.isBlank() || nombre.length() > 150) {
            throw new IllegalArgumentException("Ingresa un nombre de hasta 150 caracteres.");
        }
        if (!correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Ingresa un correo válido.");
        }
        if (usuarioRepository.findByCorreoIgnoreCase(correo).isPresent()) {
            throw new IllegalArgumentException("Este correo ya está registrado.");
        }
        if (!rut.matches("^[0-9]{7,8}[0-9K]$") || rut.length() > 9) {
            throw new IllegalArgumentException("Ingresa un RUT válido, sin puntos ni guion.");
        }
        if (personaRepository.existsByRutIgnoreCase(rut)) {
            throw new IllegalArgumentException("Este RUT ya está registrado.");
        }
        if (datos.fechaNacimiento() == null || datos.fechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ingresa una fecha de nacimiento válida.");
        }
        if (!sexo.equals("F") && !sexo.equals("M")) {
            throw new IllegalArgumentException("Selecciona un sexo válido.");
        }
        if (!celular.matches("9\\d{8}")) {
            throw new IllegalArgumentException("Ingresa un celular de 9 dígitos que comience con 9.");
        }
        if (datos.contrasena() == null || datos.contrasena().length() < 4 || datos.contrasena().length() > 72) {
            throw new IllegalArgumentException("La contraseña debe tener entre 4 y 72 caracteres.");
        }

        Persona persona = new Persona();
        persona.setNombre(nombre);
        persona.setRut(rut);
        persona.setFechaNacimiento(datos.fechaNacimiento());
        persona.setSexo(sexo);
        persona = personaRepository.save(persona);

        Usuario usuario = new Usuario();
        usuario.setPersona(persona);
        usuario.setCorreo(correo);
        usuario.setCelular(celular);
        usuario.setContrasena(passwordEncoder.encode(datos.contrasena()));
        usuario.setRol("PACIENTE");
        return usuarioRepository.save(usuario);
    }

    public Usuario autenticar(LoginUsuarioDTO datos) {
        if (datos.correo() == null || datos.contrasena() == null) return null;
        return usuarioRepository.findByCorreoIgnoreCase(datos.correo().trim())
                .filter(usuario -> passwordEncoder.matches(datos.contrasena(), usuario.getContrasena()))
                .orElse(null);
    }

    public Usuario insertarUsuario(Usuario usuario) {
        if (usuario.getContrasena() == null || usuario.getContrasena().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuario.setRol("PACIENTE");
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }

    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = buscarPorId(id);
        usuarioExistente.setCelular(usuarioActualizado.getCelular());
        usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
        return usuarioRepository.save(usuarioExistente);
    }
}


