package cl.nutrivida.contacto.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.nutrivida.contacto.model.Contacto;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {
    List<Contacto> findByIdUsuarioOrderByFechaCreacionDesc(Long idUsuario);
}