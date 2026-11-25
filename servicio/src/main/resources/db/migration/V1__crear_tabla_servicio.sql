CREATE TABLE servicio (
    id_servicio BIGINT NOT NULL AUTO_INCREMENT,
    tipo_servicio VARCHAR(60) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT NOT NULL,
    duracion VARCHAR(50) NOT NULL,
    modalidad VARCHAR(50) NOT NULL,
    profesional VARCHAR(150) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,

    PRIMARY KEY (id_servicio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;