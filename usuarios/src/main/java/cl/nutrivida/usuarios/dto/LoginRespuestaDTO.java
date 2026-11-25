package cl.nutrivida.usuarios.dto;

import cl.nutrivida.usuarios.model.Usuario;

public record LoginRespuestaDTO(String token, Usuario usuario) {
}