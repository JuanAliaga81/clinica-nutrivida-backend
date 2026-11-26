package cl.nutrivida.usuarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.nutrivida.usuarios.model.Persona;
import cl.nutrivida.usuarios.model.Usuario;
import cl.nutrivida.usuarios.model.UsuarioPersona;
import cl.nutrivida.usuarios.repo.PersonaRepository;
import cl.nutrivida.usuarios.repo.UsuarioPersonaRepo;
import cl.nutrivida.usuarios.repo.UsuarioRepository;

@Service
public class UsuarioPersonaService {

    @Autowired 
    private UsuarioPersonaRepo usuarioPersonaRepo;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioPersona> listar() {
        return usuarioPersonaRepo.findAll();
    }

    public List<UsuarioPersona> listarPorUsuario(Long idUsuario) {
        return usuarioPersonaRepo.findByUsuario_IdUsuario(idUsuario);
    }

    public UsuarioPersona buscarPorId(Long id) {
        return usuarioPersonaRepo.findById(id)
            .orElseThrow( () -> new RuntimeException(
                "No se encontró el usuario con ID " + id));
    }

    public UsuarioPersona insertarUsuarioPersona(UsuarioPersona usuarioPersona) {
        return usuarioPersonaRepo.save(usuarioPersona);
    }

    @Transactional
    public UsuarioPersona insertarPersonaVinculada(Long idUsuario, UsuarioPersona vinculo) {
        if (vinculo == null || vinculo.getUsuario() == null
                || !idUsuario.equals(vinculo.getUsuario().getIdUsuario())) {
            throw new IllegalArgumentException("El vínculo debe pertenecer a la cuenta que inició sesión.");
        }
        if (vinculo.getPersona() == null || vinculo.getPersona().getIdPersona() != null) {
            throw new IllegalArgumentException("Debes enviar los datos de una persona nueva.");
        }
        if (vinculo.getRelacion() == null || vinculo.getRelacion().isBlank()) {
            throw new IllegalArgumentException("Debes indicar la relación con la persona.");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la cuenta."));
        Persona persona = personaRepository.save(vinculo.getPersona());
        vinculo.setUsuario(usuario);
        vinculo.setPersona(persona);
        vinculo.setRelacion(vinculo.getRelacion().trim());
        return usuarioPersonaRepo.save(vinculo);
    }

    public void eliminar(Long id) {
        UsuarioPersona usuarioPersona = buscarPorId(id);
        usuarioPersonaRepo.delete(usuarioPersona);
    }

    public UsuarioPersona actualizarUsuarioPersona(Long id, UsuarioPersona usuarioPersonaActualizado) {
        UsuarioPersona usuarioPersonaExistente = buscarPorId(id);
        usuarioPersonaExistente.setUsuario(usuarioPersonaActualizado.getUsuario());
        usuarioPersonaExistente.setPersona(usuarioPersonaActualizado.getPersona());
        usuarioPersonaExistente.setRelacion(usuarioPersonaActualizado.getRelacion());
        return usuarioPersonaRepo.save(usuarioPersonaExistente);
    }
}
