package cl.nutrivida.servicio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.nutrivida.servicio.model.Servicio;
import cl.nutrivida.servicio.repo.ServicioRepository;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public List<Servicio> listar() {
        return servicioRepository.findAll();
    }

    public Servicio buscarPorId(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el servicio con ID " + id));
    }

    public Servicio insertarServicio(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    public void eliminar(Long id) {
        Servicio servicio = buscarPorId(id);
        servicioRepository.delete(servicio);
    }

    public Servicio actualizarServicio(Long id, Servicio servicioActualizado) {
        Servicio servicioExistente = buscarPorId(id);
        servicioExistente.setTipoServicio(servicioActualizado.getTipoServicio());
        servicioExistente.setNombre(servicioActualizado.getNombre());
        servicioExistente.setDescripcion(servicioActualizado.getDescripcion());
        servicioExistente.setDuracion(servicioActualizado.getDuracion());
        servicioExistente.setModalidad(servicioActualizado.getModalidad());
        servicioExistente.setProfesional(servicioActualizado.getProfesional());
        servicioExistente.setPrecio(servicioActualizado.getPrecio());
        return servicioRepository.save(servicioExistente);
    }
}