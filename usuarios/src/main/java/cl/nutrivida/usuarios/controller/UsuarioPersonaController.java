package cl.nutrivida.usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.nutrivida.usuarios.model.UsuarioPersona;
import cl.nutrivida.usuarios.service.UsuarioPersonaService;
import org.springframework.web.bind.annotation.RequestBody;

@RestController 
@RequestMapping ("/api/usuarios-personas")
@CrossOrigin (origins = "http://localhost:5173")
public class UsuarioPersonaController {
    @Autowired 
    private UsuarioPersonaService usuarioPersonaService;

    @GetMapping
    public ResponseEntity<List<UsuarioPersona>> listarUsuariosPersonas() {
        List<UsuarioPersona> usuariosPersonas = usuarioPersonaService.listar();
        if (usuariosPersonas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuariosPersonas);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<UsuarioPersona>> listarPorUsuario(@PathVariable Long idUsuario) {
        List<UsuarioPersona> vinculos = usuarioPersonaService.listarPorUsuario(idUsuario);
        if (vinculos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(vinculos);
    }

    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> crearPersonaVinculada(@PathVariable Long idUsuario,
            @RequestBody UsuarioPersona usuarioPersona) {
        try {
            return ResponseEntity.status(201)
                    .body(usuarioPersonaService.insertarPersonaVinculada(idUsuario, usuarioPersona));
        } catch (IllegalArgumentException error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @GetMapping ("/{id}")
    public ResponseEntity<UsuarioPersona> buscarUsuarioPersonaPorId(@PathVariable Long id) {
        try {
            UsuarioPersona usuarioPersona = usuarioPersonaService.buscarPorId(id);
            return ResponseEntity.ok(usuarioPersona);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public UsuarioPersona insertarUsuarioPersona(@RequestBody UsuarioPersona usuarioPersona){
        return usuarioPersonaService.insertarUsuarioPersona(usuarioPersona);
    }

    @DeleteMapping ("/{id}")
    public UsuarioPersona eliminarUsuarioPersona(@PathVariable Long id) {
        UsuarioPersona usuarioPersona = usuarioPersonaService.buscarPorId(id);
        usuarioPersonaService.eliminar(id);
        return usuarioPersona;
    }

    @PutMapping("/{id}")
    public UsuarioPersona actualizarUsuarioPersona(@PathVariable Long id, @RequestBody UsuarioPersona usuarioPersonaActualizado) {
        return usuarioPersonaService.actualizarUsuarioPersona(id, usuarioPersonaActualizado);
    }
}
