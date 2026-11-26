package cl.nutrivida.cita.controller;

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

import cl.nutrivida.cita.model.Cita;
import cl.nutrivida.cita.service.CitaService;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "http://localhost:5173")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @GetMapping
    public ResponseEntity<List<Cita>> listarCitas() {
        List<Cita> citas = citaService.listar();
        if (citas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Cita>> listarCitasPorUsuario(@PathVariable Long idUsuario) {
        if (idUsuario == null || idUsuario <= 0) return ResponseEntity.badRequest().build();
        List<Cita> citas = citaService.listarPorUsuario(idUsuario);
        if (citas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> buscarCitaPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(citaService.buscarPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> insertarCita(@RequestBody Cita cita) {
        try {
            return ResponseEntity.ok(citaService.insertar(cita));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> insertarCitaDeUsuario(@PathVariable Long idUsuario, @RequestBody Cita cita) {
        if (cita.getIdUsuario() == null || !idUsuario.equals(cita.getIdUsuario())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("La cita debe pertenecer a la cuenta que inició sesión.");
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(citaService.insertar(cita));
        } catch (IllegalArgumentException error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @DeleteMapping("/usuario/{idUsuario}/{idCita}")
    public ResponseEntity<?> eliminarCitaDeUsuario(@PathVariable Long idUsuario, @PathVariable Long idCita) {
        try {
            citaService.eliminarDeUsuario(idUsuario, idCita);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCita(@PathVariable Long id, @RequestBody Cita cita) {
        try {
            return ResponseEntity.ok(citaService.actualizar(id, cita));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().startsWith("No se encontró")) return ResponseEntity.notFound().build();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Cita> eliminarCita(@PathVariable Long id) {
        try {
            Cita cita = citaService.buscarPorId(id);
            citaService.eliminar(id);
            return ResponseEntity.ok(cita);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
