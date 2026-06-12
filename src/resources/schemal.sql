CREATE TABLE IF NOT EXISTS pacientes (
                                         id INT PRIMARY KEY,
                                         nombre VARCHAR(100),
    apellido VARCHAR(100),
    dni VARCHAR(20),
    telefono VARCHAR(20),
    historial VARCHAR(500)
    );

CREATE TABLE IF NOT EXISTS medicos (
                                       id INT PRIMARY KEY,
                                       nombre VARCHAR(100),
    apellido VARCHAR(100),
    especialidad VARCHAR(50),
    disponible BOOLEAN
    );

CREATE TABLE IF NOT EXISTS citas (
                                     id INT PRIMARY KEY,
                                     paciente_id INT,
                                     medico_id INT,
                                     fecha VARCHAR(50),
    motivo VARCHAR(200),
    urgente BOOLEAN,
    costo DOUBLE PRECISION,
    estado VARCHAR(20)
    );