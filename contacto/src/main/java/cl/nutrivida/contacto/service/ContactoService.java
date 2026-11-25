package cl.nutrivida.contacto.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.nutrivida.contacto.model.Contacto;
import cl.nutrivida.contacto.model.TipoAsunto;
import cl.nutrivida.contacto.repo.ContactoRepository;
import cl.nutrivida.contacto.repo.TipoAsuntoRepository;

@Service
public class ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private TipoAsuntoRepository tipoAsuntoRepository;

    public List<Contacto> listar() {
        return contactoRepository.findAll();
    }

    public List<Contacto> listarPorUsuario(Long idUsuario) {
        if (idUsuario == null || idUsuario <= 0) {
            throw new IllegalArgumentException("El ID de usuario debe ser válido.");
        }
        return contactoRepository.findByIdUsuarioOrderByFechaCreacionDesc(idUsuario);
    }

    public Contacto buscarPorId(Long id) {
        return contactoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el contacto con ID " + id));
    }

    public Contacto insertar(Contacto contacto) {
        validarContacto(contacto);
        contacto.setRespuesta(null);
        contacto.setFechaCreacion(LocalDateTime.now());
        return contactoRepository.save(contacto);
    }

    public Contacto responder(Long id, String respuesta) {
        if (respuesta == null || respuesta.isBlank()) {
            throw new IllegalArgumentException("La respuesta no puede estar vacía.");
        }
        if (respuesta.trim().length() > 2000) {
            throw new IllegalArgumentException("La respuesta no puede superar 2000 caracteres.");
        }
        Contacto contacto = buscarPorId(id);
        contacto.setRespuesta(respuesta.trim());
        return contactoRepository.save(contacto);
    }

    public Contacto actualizar(Long id, Contacto contactoActualizado) {
        Contacto contactoExistente = buscarPorId(id);
        validarContacto(contactoActualizado);
        contactoExistente.setIdUsuario(contactoActualizado.getIdUsuario());
        contactoExistente.setIdTipoAsunto(contactoActualizado.getIdTipoAsunto());
        contactoExistente.setMensaje(contactoActualizado.getMensaje().trim());
        return contactoRepository.save(contactoExistente);
    }

    public void eliminar(Long id) {
        contactoRepository.delete(buscarPorId(id));
    }

    public List<TipoAsunto> listarTiposAsunto() {
        return tipoAsuntoRepository.findAll();
    }

    private void validarContacto(Contacto contacto) {
        if (contacto.getIdUsuario() == null || contacto.getIdUsuario() <= 0) {
            throw new IllegalArgumentException("El ID de usuario debe ser válido.");
        }
        if (contacto.getIdTipoAsunto() == null
                || !tipoAsuntoRepository.existsById(contacto.getIdTipoAsunto())) {
            throw new IllegalArgumentException("Selecciona un tipo de asunto existente.");
        }
        if (contacto.getMensaje() == null || contacto.getMensaje().isBlank()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío.");
        }
        if (contacto.getMensaje().trim().length() > 2000) {
            throw new IllegalArgumentException("El mensaje no puede superar 2000 caracteres.");
        }
        contacto.setMensaje(contacto.getMensaje().trim());
    }
}