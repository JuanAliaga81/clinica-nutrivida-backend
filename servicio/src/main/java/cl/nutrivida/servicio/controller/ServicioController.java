package cl.nutrivida.servicio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import cl.nutrivida.servicio.model.Servicio;
import cl.nutrivida.servicio.service.ServicioService;

@RestController
@RequestMapping("/api/servicios")
@CrossOrigin(origins = "http://localhost:5173")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @GetMapping
    public ResponseEntity<List<Servicio>> listarServicios() {
        List<Servicio> servicios = servicioService.listar();
        if (servicios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> buscarServicioPorId(@PathVariable Long id) {
        try {
            Servicio servicio = servicioService.buscarPorId(id);
            return ResponseEntity.ok(servicio);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Servicio insertarServicio(@RequestBody Servicio servicio) {
        return servicioService.insertarServicio(servicio);
    }

    @DeleteMapping("/{id}")
    public Servicio eliminarServicio(@PathVariable Long id) {
        Servicio servicio = servicioService.buscarPorId(id);
        servicioService.eliminar(id);
        return servicio;
    }

    @PutMapping("/{id}")
    public Servicio actualizarServicio(
            @PathVariable Long id,
            @RequestBody Servicio servicioActualizado) {
        return servicioService.actualizarServicio(id, servicioActualizado);
    }
}