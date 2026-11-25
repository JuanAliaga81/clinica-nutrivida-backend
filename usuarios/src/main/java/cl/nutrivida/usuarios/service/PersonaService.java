package cl.nutrivida.usuarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.nutrivida.usuarios.model.Persona;
import cl.nutrivida.usuarios.repo.PersonaRepository;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    public Persona buscarPorId(Long id) {
        return personaRepository.findById(id)
            .orElseThrow( () -> new IllegalArgumentException(
                "No se encontró la persona con ID " + id));
    }

    public List<Persona> listar() {
        return personaRepository.findAll();
    }

    public Persona insertarPersona(Persona persona) {
        return personaRepository.save(persona);
    }

    public void eliminar(Long id) {
        Persona persona = buscarPorId(id);
        personaRepository.delete(persona);
    }

    public Persona actualizarPersona(Long id, Persona personaActualizada) {
        Persona personaExistente = buscarPorId(id);
        personaExistente.setNombre(personaActualizada.getNombre());
        personaExistente.setRut(personaActualizada.getRut());
        personaExistente.setFechaNacimiento(personaActualizada.getFechaNacimiento());
        personaExistente.setSexo(personaActualizada.getSexo());
        return personaRepository.save(personaExistente);
    }
}