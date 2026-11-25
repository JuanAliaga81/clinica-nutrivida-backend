package cl.nutrivida.usuarios.model;

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

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona", nullable = false)
    private Long idPersona;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "rut", nullable = false, length = 9, unique = true)
    private String rut;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "sexo", nullable = false, length = 1)
    private String sexo;

    public Persona orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }
}