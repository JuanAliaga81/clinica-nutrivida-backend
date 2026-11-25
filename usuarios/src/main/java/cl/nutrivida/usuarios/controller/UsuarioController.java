package cl.nutrivida.usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.nutrivida.usuarios.dto.LoginRespuestaDTO;
import cl.nutrivida.usuarios.dto.LoginUsuarioDTO;
import cl.nutrivida.usuarios.dto.RegistroUsuarioDTO;
import cl.nutrivida.usuarios.model.Usuario;
import cl.nutrivida.usuarios.service.TokenService;
import cl.nutrivida.usuarios.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listar();
        if (usuarios.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(usuarioService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroUsuarioDTO datos) {
        try {
            Usuario usuario = usuarioService.registrar(datos);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (IllegalArgumentException e) {
            HttpStatus estado = e.getMessage().contains("ya está registrado")
                    ? HttpStatus.CONFLICT
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(estado).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody LoginUsuarioDTO datos) {
        Usuario usuario = usuarioService.autenticar(datos);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Correo o contraseña incorrectos.");
        }
        return ResponseEntity.ok(new LoginRespuestaDTO(tokenService.crearToken(usuario), usuario));
    }

    @PostMapping
    public Usuario insertarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.insertarUsuario(usuario);
    }

    @DeleteMapping("/{id}")
    public Usuario eliminarUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        usuarioService.eliminar(id);
        return usuario;
    }

    @PutMapping("/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        return usuarioService.actualizarUsuario(id, usuarioActualizado);
    }
}