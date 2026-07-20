# Hospital — Sistema de Gestión Hospitalaria (Java Swing)

Aplicación de escritorio para la gestión de pacientes, médicos y citas médicas, desarrollada como
proyecto académico para practicar patrones de diseño GoF (creacionales, estructurales y de
comportamiento) sobre una interfaz gráfica Swing.

## Características

- Registro y administración de **pacientes** y **médicos** por especialidad.
- Agendamiento de **citas médicas** con máquina de estados (pendiente → confirmada/cancelada → completada).
- Historial de comandos con soporte de **deshacer** (agendar/cancelar cita).
- Control de acceso por **rol de usuario** (Admin, Médico, Recepcionista).
- Recargos aplicables a una cita mediante decoradores (seguro, urgencia).
- Interfaz moderna con [FlatLaf](https://github.com/JFormDesigner/FlatLaf).
- Persistencia en base de datos relacional.

## Patrones de diseño aplicados

| Categoría        | Patrón            | Clase(s)                                                              |
|-------------------|--------------------|-------------------------------------------------------------------------|
| Creacional        | Singleton          | `GestorHospital`, `ConexionBD`, `Sesion`                                |
| Creacional        | Factory Method     | `MedicoFactory`                                                         |
| Creacional        | Builder            | `CitaBuilder`                                                           |
| Creacional        | Prototype          | `PacientePrototype` (`Paciente.clone()`)                                |
| Estructural       | Facade             | `HospitalFacade`                                                        |
| Estructural       | Proxy              | `HospitalProxy` (control de acceso por rol)                             |
| Estructural       | Decorator          | `CitaDecorator`, `CitaConSeguro`, `CitaConUrgencia`                     |
| Comportamiento    | Command            | `Comando`, `ComandoAgendarCita`, `ComandoCancelarCita`, `HistorialComandos` |
| Comportamiento    | State              | `EstadoCita`, `EstadoPendiente`, `EstadoConfirmada`, `EstadoCancelada`, `EstadoCompletada` |
| Comportamiento    | Observer           | `CitaObserver`, `AlertaDisponibilidad`                                  |

## Estructura del proyecto

```
src/Hospital/
├── Main.java              # Punto de entrada
├── creacional/             # Singleton, Factory, Builder, Prototype
├── estructural/             # Facade, Proxy, Decorator, Sesion
├── comportamiento/           # Command, State, Observer
├── model/                    # Entidades: Cita, Medico, Paciente, Usuario...
├── repository/                # Interfaces + implementaciones sobre BD
└── view/                       # UI Swing (VMain y formularios)
src/resources/
└── schemal.sql            # Script de creación de tablas
src/test/                     # Pruebas unitarias e de integración (JUnit)
```

## Independencia del motor de base de datos (principio DIP)

La capa de persistencia está desacoplada de la lógica de negocio siguiendo el **Principio de
Inversión de Dependencias** (la "D" de SOLID): `HospitalFacade` depende únicamente de las
interfaces `PacienteRepository`, `MedicoRepository` y `CitaRepository`, no de una implementación
concreta:

```java
public HospitalFacade(PacienteRepository pacienteRepo,
                      MedicoRepository medicoRepo,
                      CitaRepository citaRepo) { ... }
```

Actualmente el proyecto usa H2 (`PacienteRepositoryH2`, `MedicoRepositoryH2`,
`CitaRepositoryH2`), pero **cambiar de motor de base de datos no requiere modificar la fachada, el
proxy ni la interfaz gráfica** — solo:

1. Crear una nueva clase que implemente las tres interfaces del paquete `repository`
   (por ejemplo `PacienteRepositoryPostgres`, `MedicoRepositoryPostgres`, `CitaRepositoryPostgres`).
2. Ajustar `ConexionBD` con el driver y la URL JDBC correspondientes al nuevo motor.
3. Cambiar, en `Sesion`, la instanciación de los repositorios concretos que se inyectan a
   `HospitalFacade`:
   ```java
   PacienteRepository pacienteRepo = new PacienteRepositoryPostgres();
   MedicoRepository   medicoRepo   = new MedicoRepositoryPostgres();
   CitaRepository     citaRepo     = new CitaRepositoryPostgres();
   ```

Esto convierte a MySQL, PostgreSQL, SQLite, etc. en una decisión de configuración y no de
arquitectura, gracias a que el resto del sistema solo conoce los contratos (`interface`), no los
detalles de implementación.

## Requisitos previos

- **JDK 24** (el `pom.xml` fija `maven.compiler.source`/`target` en `24`; un JDK más antiguo
  no compilará, uno más nuevo puede funcionar pero no está garantizado)
- **Maven** 3.8+
- **PostgreSQL** en ejecución localmente (puerto por defecto `5432`)

## Configuración de la base de datos

1. Crea la base de datos:
   ```sql
   CREATE DATABASE hospital_bd;
   ```
2. Ejecuta el script de esquema incluido en el proyecto:
   ```bash
   psql -U postgres -d hospital_bd -f src/resources/schemal.sql
   ```
3. Configura las credenciales de conexión (ver [Inconsistencias](#inconsistencias-detectadas-y-cómo-solucionarlas)
   más abajo antes de continuar — actualmente están fijas en el código fuente):
   `Hospital.repository.ConexionBD` — variables `URL`, `USER`, `PASSWORD`.

Al iniciar por primera vez con la base de datos vacía, `Sesion` carga automáticamente datos
semilla (3 médicos y 2 pacientes de ejemplo) para poder probar la aplicación de inmediato.

## Cómo ejecutar

```bash
# Clonar el repositorio y ubicarse en la rama correspondiente
git clone https://github.com/Gusion-ios/Hospital.git
cd Hospital

# Compilar
mvn clean compile

# Ejecutar
mvn exec:java -Dexec.mainClass="Hospital.Main"
```

Si el plugin `exec` no está configurado en el `pom.xml`, se puede ejecutar directamente desde el IDE
(IntelliJ IDEA) ejecutando la clase `Hospital.Main`, o generando el classpath con Maven:

```bash
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt
javac -cp "$(cat cp.txt)" -d target/classes $(find src/Hospital -name "*.java")
java -cp "target/classes:$(cat cp.txt)" Hospital.Main
```

## Pruebas

```bash
mvn test
```

Incluye pruebas para el patrón State (`EstadoCitaTest`), Command (`HistorialComandosTest`),
Decorator (`DecoratorTest`), Builder (`CitaBuilderTest`) y una prueba de integración de la fachada
(`HospitalFacadeIntTest`).

## Inconsistencias detectadas y cómo solucionarlas

1. **Mezcla de PostgreSQL y H2.**
   El `pom.xml` declara el driver de `org.postgresql:postgresql`, `ConexionBD` se conecta a
   `jdbc:postgresql://localhost:5432/hospital_bd`, pero las clases de acceso a datos se llaman
   `PacienteRepositoryH2`, `MedicoRepositoryH2`, `CitaRepositoryH2`, y además hay un archivo
   `hospital_db.mv.db` (formato de base de datos H2) suelto en la raíz del proyecto.
   → **Solución:** decidir un solo motor. Si es PostgreSQL, renombrar las clases a
   `*RepositoryPostgres` (o simplemente `*RepositoryImpl`) y eliminar `hospital_db.mv.db` del
   repositorio. Si en realidad se quiere seguir usando H2 (más simple para un proyecto académico,
   sin necesidad de instalar un servidor), quitar la dependencia de `postgresql` del `pom.xml`,
   añadir la de `com.h2database:h2`, y cambiar la URL de conexión en `ConexionBD` a algo como
   `jdbc:h2:./hospital_db`.

2. **Credenciales de base de datos hardcodeadas.**
   `ConexionBD.java` tiene la URL, el usuario y la contraseña de PostgreSQL escritos directamente
   en el código fuente (incluida una contraseña real).
   → **Solución:** mover estos valores a variables de entorno o a un archivo `config.properties` /
   `.env` que **no** se suba al repositorio (agregarlo a `.gitignore`), y leerlo con
   `System.getenv(...)` o `Properties` al iniciar `ConexionBD`.

3. **Artefactos de build y de IDE incluidos en el repositorio.**
   `target/classes` (compilado de Maven) y `.idea/` (configuración de IntelliJ) están presentes en
   la rama, pero el `.gitignore` actual no los excluye.
   → **Solución:** añadir al `.gitignore`:
   ```
   target/
   .idea/
   *.mv.db
   *.trace.db
   ```
   y luego quitar esos archivos del control de versiones con
   `git rm -r --cached target .idea *.mv.db`.

4. **Nombre de archivo con error tipográfico.**
   `src/resources/schemal.sql` — sobra la "l" (debería ser `schema.sql`).
   → **Solución:** renombrar el archivo y actualizar cualquier referencia a él (documentación,
   scripts).

5. **Falta de README.md** (ya resuelto con este documento) y ausencia de licencia — si el proyecto
   se va a compartir públicamente o se va a evaluar como entregable, conviene añadir un archivo
   `LICENSE` (o indicar explícitamente que es un trabajo académico sin licencia de uso).

6. **Ausencia de `exec-maven-plugin`.**
   El `pom.xml` no define un plugin para ejecutar la app con `mvn exec:java`, lo que obliga a
   ejecutar manualmente desde el IDE o armar el classpath a mano.
   → **Solución:** añadir en `<build><plugins>`:
   ```xml
   <plugin>
       <groupId>org.codehaus.mojo</groupId>
       <artifactId>exec-maven-plugin</artifactId>
       <version>3.5.0</version>
       <configuration>
           <mainClass>Hospital.Main</mainClass>
       </configuration>
   </plugin>
   ```
