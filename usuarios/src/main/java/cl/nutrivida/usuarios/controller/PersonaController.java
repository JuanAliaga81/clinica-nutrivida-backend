package cl.nutrivida.usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.nutrivida.usuarios.model.Persona;
import cl.nutrivida.usuarios.service.PersonaService;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController 
@RequestMapping ("/api/personas")
@CrossOrigin(origins = "http://localhost:5173")
public class PersonaController {
    @Autowired 
    public PersonaService personaService;

    @GetMapping
    public ResponseEntity<List<Persona>> listarPersonas() {
        List<Persona> personas = personaService.listar();
        if (personas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(personas);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<Persona> buscarPersonaPorId(@PathVariable Long id){
        try{
            Persona persona = personaService.buscarPorId(id);
            return ResponseEntity.ok(persona);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Persona insertaPersona(@RequestBody Persona persona) {
        return personaService.insertarPersona(persona);
    }

    @DeleteMapping ("/{id}")
    public Persona eliminarPersona(@PathVariable Long id) {
        Persona persona = personaService.buscarPorId(id);
        personaService.eliminar(id);
        return persona;
    }

    @PutMapping("/{id}")
    public Persona actualizarPersona(@PathVariable Long id, @RequestBody Persona personaActualizada) {
        return personaService.actualizarPersona(id, personaActualizada);
    }
}
