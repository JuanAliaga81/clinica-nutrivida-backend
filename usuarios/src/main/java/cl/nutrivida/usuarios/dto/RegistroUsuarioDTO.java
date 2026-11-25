package cl.nutrivida.usuarios.dto;

import java.time.LocalDate;

public record RegistroUsuarioDTO(
        String nombre,
        String rut,
        LocalDate fechaNacimiento,
        String sexo,
        String correo,
        String celular,
        String contrasena) {
}