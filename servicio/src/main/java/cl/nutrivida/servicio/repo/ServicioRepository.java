package cl.nutrivida.servicio.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.nutrivida.servicio.model.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
}