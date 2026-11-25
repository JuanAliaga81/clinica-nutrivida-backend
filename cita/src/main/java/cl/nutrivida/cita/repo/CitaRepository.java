package cl.nutrivida.cita.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.nutrivida.cita.model.Cita;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByIdUsuarioOrderByFechaAsc(Long idUsuario);
}