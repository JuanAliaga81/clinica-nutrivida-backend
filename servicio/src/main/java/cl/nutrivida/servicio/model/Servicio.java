package cl.nutrivida.servicio.model;

import java.math.BigDecimal;

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
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio", nullable = false)
    private Long idServicio;

    @Column(name = "tipo_servicio", nullable = false, length = 60)
    private String tipoServicio;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "duracion", nullable = false, length = 50)
    private String duracion;

    @Column(name = "modalidad", nullable = false, length = 50)
    private String modalidad;

    @Column(name = "profesional", nullable = false, length = 150)
    private String profesional;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
}