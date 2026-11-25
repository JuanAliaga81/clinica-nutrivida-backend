package cl.nutrivida.usuarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<UsuarioPersona> listar() {
        return usuarioPersonaRepo.findAll();
    }

    public UsuarioPersona buscarPorId(Long id) {
        return usuarioPersonaRepo.findById(id)
            .orElseThrow( () -> new RuntimeException(
                "No se encontró el usuario con ID " + id));
    }

    public UsuarioPersona insertarUsuarioPersona(UsuarioPersona usuarioPersona) {
        return usuarioPersonaRepo.save(usuarioPersona);
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