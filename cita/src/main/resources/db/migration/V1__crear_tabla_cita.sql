CREATE TABLE cita (
    id_cita BIGINT NOT NULL AUTO_INCREMENT,
    id_usuario BIGINT NOT NULL,
    id_persona BIGINT NOT NULL,
    id_servicio BIGINT NOT NULL,
    fecha DATE NOT NULL,
    prevision VARCHAR(80) NOT NULL,
    PRIMARY KEY (id_cita),
    INDEX idx_cita_id_usuario (id_usuario),
    INDEX idx_cita_id_persona (id_persona),
    INDEX idx_cita_id_servicio (id_servicio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
