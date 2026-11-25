package cl.nutrivida.usuarios.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.nutrivida.usuarios.model.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    boolean existsByRutIgnoreCase(String rut);
}