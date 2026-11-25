CREATE TABLE persona (
    id_persona BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    rut VARCHAR(9) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    sexo VARCHAR(1) NOT NULL,

    CONSTRAINT pk_persona PRIMARY KEY (id_persona),
    CONSTRAINT uk_persona_rut UNIQUE (rut)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE usuarios (
    id_usuario BIGINT NOT NULL AUTO_INCREMENT,
    id_persona BIGINT NOT NULL,
    correo VARCHAR(150) NOT NULL,
    celular VARCHAR(20) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,

    CONSTRAINT pk_usuarios PRIMARY KEY (id_usuario),
    CONSTRAINT uk_usuarios_correo UNIQUE (correo),
    CONSTRAINT uk_usuarios_persona UNIQUE (id_persona),
    CONSTRAINT fk_usuarios_persona
        FOREIGN KEY (id_persona)
        REFERENCES persona (id_persona)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE usuario_carga (
    id_usuario_carga BIGINT NOT NULL AUTO_INCREMENT,
    id_usuario BIGINT NOT NULL,
    id_persona BIGINT NOT NULL,
    relacion VARCHAR(50) NOT NULL,

    CONSTRAINT pk_usuario_carga PRIMARY KEY (id_usuario_carga),
    CONSTRAINT uk_usuario_carga_persona UNIQUE (id_usuario, id_persona),
    CONSTRAINT fk_usuario_carga_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios (id_usuario),
    CONSTRAINT fk_usuario_carga_persona
        FOREIGN KEY (id_persona)
        REFERENCES persona (id_persona)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;