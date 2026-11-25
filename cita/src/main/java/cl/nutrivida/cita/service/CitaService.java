package cl.nutrivida.cita.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.nutrivida.cita.model.Cita;
import cl.nutrivida.cita.repo.CitaRepository;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    public List<Cita> listar() {
        return citaRepository.findAll();
    }

    public List<Cita> listarPorUsuario(Long idUsuario) {
        return citaRepository.findByIdUsuarioOrderByFechaAsc(idUsuario);
    }

    public Cita buscarPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró la cita con ID " + id));
    }

    public Cita insertar(Cita cita) {
        validarCita(cita);
        return citaRepository.save(cita);
    }

    public Cita actualizar(Long id, Cita citaActualizada) {
        Cita citaExistente = buscarPorId(id);
        validarCita(citaActualizada);
        citaExistente.setIdUsuario(citaActualizada.getIdUsuario());
        citaExistente.setIdPersona(citaActualizada.getIdPersona());
        citaExistente.setIdServicio(citaActualizada.getIdServicio());
        citaExistente.setFecha(citaActualizada.getFecha());
        citaExistente.setPrevision(citaActualizada.getPrevision().trim());
        return citaRepository.save(citaExistente);
    }

    public void eliminar(Long id) {
        citaRepository.delete(buscarPorId(id));
    }

    private void validarCita(Cita cita) {
        if (cita.getIdUsuario() == null || cita.getIdUsuario() <= 0) {
            throw new IllegalArgumentException("El ID de usuario debe ser válido.");
        }
        if (cita.getIdPersona() == null || cita.getIdPersona() <= 0) {
            throw new IllegalArgumentException("El ID de persona debe ser válido.");
        }
        if (cita.getIdServicio() == null || cita.getIdServicio() <= 0) {
            throw new IllegalArgumentException("El ID de servicio debe ser válido.");
        }
        if (cita.getFecha() == null || cita.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de la cita debe ser hoy o posterior.");
        }
        if (cita.getPrevision() == null || cita.getPrevision().isBlank()) {
            throw new IllegalArgumentException("Debes indicar la previsión.");
        }
        if (cita.getPrevision().trim().length() > 80) {
            throw new IllegalArgumentException("La previsión no puede superar 80 caracteres.");
        }
        cita.setPrevision(cita.getPrevision().trim());
    }
}