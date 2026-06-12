package Hospital.estructural;

import Hospital.model.Especialidad;
import Hospital.model.RolUsuario;
import Hospital.model.Usuario;
import Hospital.repository.*;

public class Sesion {

    private static Sesion instancia;
    private Usuario usuarioActivo;
    private HospitalProxy proxy;
    private HospitalFacade facade;

    private Sesion() {
        // Sesion crea los repositorios concretos e inyecta en la Facade
        PacienteRepository pacienteRepo = new PacienteRepositoryH2();
        MedicoRepository   medicoRepo   = new MedicoRepositoryH2();
        CitaRepository     citaRepo     = new CitaRepositoryH2();

        this.facade        = new HospitalFacade(pacienteRepo, medicoRepo, citaRepo);
        this.usuarioActivo = new Usuario("Admin", RolUsuario.ADMIN);
        this.proxy         = new HospitalProxy(facade, usuarioActivo);
        cargarSemillaSiVacio();
    }

    public static Sesion getInstancia() {
        if (instancia == null) {
            instancia = new Sesion();
        }
        return instancia;
    }

    private void cargarSemillaSiVacio() {
        if (!facade.getMedicos().isEmpty()) return;
        facade.registrarMedico(1, "Carlos", "Pérez",  Especialidad.CARDIOLOGIA);
        facade.registrarMedico(2, "Ana",    "García", Especialidad.PEDIATRIA);
        facade.registrarMedico(3, "Luis",   "Torres", Especialidad.MEDICINA_GENERAL);
        facade.registrarPaciente(1, "Juan",  "Quispe", "12345678", "999111222", "Sin antecedentes");
        facade.registrarPaciente(2, "María", "López",  "87654321", "988333444", "Hipertensión");
    }

    public void cambiarUsuario(String nombre, RolUsuario rol) {
        this.usuarioActivo = new Usuario(nombre, rol);
        this.proxy         = new HospitalProxy(facade, usuarioActivo);
    }

    public HospitalProxy getProxy()         { return proxy; }
    public Usuario       getUsuarioActivo() { return usuarioActivo; }
}