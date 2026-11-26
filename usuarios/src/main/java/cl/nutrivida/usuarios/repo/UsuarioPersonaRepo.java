package cl.nutrivida.usuarios.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import cl.nutrivida.usuarios.model.UsuarioPersona;

@Repository 
public interface UsuarioPersonaRepo extends JpaRepository<UsuarioPersona, Long> {
    List<UsuarioPersona> findByUsuario_IdUsuario(Long idUsuario);

}
