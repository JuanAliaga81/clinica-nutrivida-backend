CREATE TABLE tipo_asunto (
    id_tipo_asunto BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_tipo_asunto),
    UNIQUE KEY uk_tipo_asunto_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO tipo_asunto (nombre) VALUES
    ('Consulta sobre servicios'),
    ('Reserva o modificación de cita'),
    ('Seguimiento nutricional'),
    ('Pagos y precios'),
    ('Reclamo o sugerencia'),
    ('Otro');

CREATE TABLE contacto (
    id_contacto BIGINT NOT NULL AUTO_INCREMENT,
    id_usuario BIGINT NOT NULL,
    id_tipo_asunto BIGINT NOT NULL,
    mensaje TEXT NOT NULL,
    PRIMARY KEY (id_contacto),
    CONSTRAINT fk_contacto_tipo_asunto
        FOREIGN KEY (id_tipo_asunto)
        REFERENCES tipo_asunto (id_tipo_asunto)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    INDEX idx_contacto_id_usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
