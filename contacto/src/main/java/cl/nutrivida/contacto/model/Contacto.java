package cl.nutrivida.contacto.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contacto")
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contacto", nullable = false)
    private Long idContacto;

    // ID del usuario del microservicio Usuarios; no es una FK local.
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    // FK local a la tabla tipo_asunto.
    @Column(name = "id_tipo_asunto", nullable = false)
    private Long idTipoAsunto;

    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "respuesta", columnDefinition = "TEXT")
    private String respuesta;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
}