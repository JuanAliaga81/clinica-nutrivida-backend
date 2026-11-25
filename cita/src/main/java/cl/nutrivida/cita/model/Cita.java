package cl.nutrivida.cita.model;

import java.time.LocalDate;

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
@Table(name = "cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita", nullable = false)
    private Long idCita;

    // Estos IDs pertenecen a otros microservicios.
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_persona", nullable = false)
    private Long idPersona;

    @Column(name = "id_servicio", nullable = false)
    private Long idServicio;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "prevision", nullable = false, length = 80)
    private String prevision;
}
