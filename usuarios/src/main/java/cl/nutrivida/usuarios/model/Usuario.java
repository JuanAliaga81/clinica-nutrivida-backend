package cl.nutrivida.usuarios.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @OneToOne
    @JoinColumn(name = "id_persona", nullable = false, unique = true)
    private Persona persona;

    @Column(name = "correo", nullable = false, unique = true, length = 150)
    private String correo;

    @Column(name = "celular", nullable = false, length = 20)
    private String celular;
    @Column(name = "rol", nullable = false, length = 20)
    private String rol = "PACIENTE";

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;
}


