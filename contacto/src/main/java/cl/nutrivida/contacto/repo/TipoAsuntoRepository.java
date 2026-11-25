package cl.nutrivida.contacto.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.nutrivida.contacto.model.TipoAsunto;

@Repository
public interface TipoAsuntoRepository extends JpaRepository<TipoAsunto, Long> {
}
