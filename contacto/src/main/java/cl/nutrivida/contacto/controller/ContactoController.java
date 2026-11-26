package cl.nutrivida.contacto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.nutrivida.contacto.dto.RespuestaContactoDTO;
import cl.nutrivida.contacto.model.Contacto;
import cl.nutrivida.contacto.model.TipoAsunto;
import cl.nutrivida.contacto.service.ContactoService;

@RestController
@RequestMapping("/api/contactos")
@CrossOrigin(origins = "http://localhost:5173")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;

    @GetMapping("/tipos-asunto")
    public ResponseEntity<List<TipoAsunto>> listarTiposAsunto() {
        return ResponseEntity.ok(contactoService.listarTiposAsunto());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Contacto>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(contactoService.listarPorUsuario(idUsuario));
    }

    @GetMapping
    public ResponseEntity<List<Contacto>> listarContactos() {
        List<Contacto> contactos = contactoService.listar();
        if (contactos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contactos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contacto> buscarContactoPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(contactoService.buscarPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> insertarContacto(@RequestBody Contacto contacto) {
        try {
            return ResponseEntity.ok(contactoService.insertar(contacto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> insertarContactoDeUsuario(@PathVariable Long idUsuario,
            @RequestBody Contacto contacto) {
        if (contacto.getIdUsuario() == null || !idUsuario.equals(contacto.getIdUsuario())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("El mensaje debe pertenecer a la cuenta que inició sesión.");
        }
        return insertarContacto(contacto);
    }

    @PutMapping("/{id}/respuesta")
    public ResponseEntity<?> responderContacto(
            @PathVariable Long id,
            @RequestBody RespuestaContactoDTO datos) {
        try {
            return ResponseEntity.ok(contactoService.responder(id, datos.respuesta()));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().startsWith("No se encontró")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarContacto(
            @PathVariable Long id,
            @RequestBody Contacto contacto) {
        try {
            return ResponseEntity.ok(contactoService.actualizar(id, contacto));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().startsWith("No se encontró")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Contacto> eliminarContacto(@PathVariable Long id) {
        try {
            Contacto contacto = contactoService.buscarPorId(id);
            contactoService.eliminar(id);
            return ResponseEntity.ok(contacto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
