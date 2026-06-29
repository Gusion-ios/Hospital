package Hospital.estructural;

import Hospital.model.Cita;
import Hospital.model.Especialidad;
import Hospital.model.Medico;
import Hospital.model.Paciente;
import Hospital.model.RolUsuario;
import Hospital.model.Usuario;

import java.util.List;

public class HospitalProxy {

    private HospitalFacade facade;
    private Usuario usuarioActivo;

    public HospitalProxy(HospitalFacade facade, Usuario usuarioActivo) {
        this.facade = facade;
        this.usuarioActivo = usuarioActivo;
    }

    public void registrarPaciente(int id, String nombre, String apellido,
                                  String dni, String telefono, String historial) {
        if (tienePermiso(RolUsuario.ADMIN, RolUsuario.RECEPCIONISTA)) {
            facade.registrarPaciente(id, nombre, apellido, dni, telefono, historial);
        }
    }

    public void registrarMedico(int id, String nombre, String apellido, Especialidad especialidad) {
        if (tienePermiso(RolUsuario.ADMIN)) {
            facade.registrarMedico(id, nombre, apellido, especialidad);
        }
    }

    public void agendarCita(int id, Paciente paciente, Medico medico,
                            String fecha, String motivo, boolean urgente, double costo) {
        if (tienePermiso(RolUsuario.ADMIN, RolUsuario.RECEPCIONISTA)) {
            facade.agendarCita(id, paciente, medico, fecha, motivo, urgente, costo);
        }
    }

    public void confirmarCita(Cita cita) {
        if (tienePermiso(RolUsuario.ADMIN, RolUsuario.MEDICO)) {
            facade.confirmarCita(cita);
        }
    }

    public void cancelarCita(Cita cita) {
        if (tienePermiso(RolUsuario.ADMIN, RolUsuario.RECEPCIONISTA)) {
            facade.cancelarCita(cita);
        }
    }

    public void deshacerUltimaAccion() {
        if (tienePermiso(RolUsuario.ADMIN)) {
            facade.deshacerUltimaAccion();
        }
    }

    public List<Paciente> getPacientes() { return facade.getPacientes(); }
    public List<Medico> getMedicos() { return facade.getMedicos(); }
    public List<Cita> getCitas() { return facade.getCitas(); }

    private boolean tienePermiso(RolUsuario... roles) {
        for (RolUsuario rol : roles) {
            if (usuarioActivo.getRol() == rol) return true;
        }
        System.out.println("Acceso denegado para: " + usuarioActivo.getRol());
        return false;
    }

    public void clonarPaciente(Paciente original) {
        if (tienePermiso(RolUsuario.ADMIN, RolUsuario.RECEPCIONISTA)) {
            facade.clonarPaciente(original);
        }
    }

    public void completarCita(Cita cita) {
        if (tienePermiso(RolUsuario.ADMIN, RolUsuario.MEDICO)) {
            facade.completarCita(cita);
        }
    }

    public void eliminarPaciente(int id) {
        if (tienePermiso(RolUsuario.ADMIN)) {
            facade.eliminarPaciente(id);
        }
    }

    public void eliminarMedico(int id) {
        if (tienePermiso(RolUsuario.ADMIN)) {
            facade.eliminarMedico(id);
        }
    }

    public void actualizarPaciente(Paciente p) {
        if (tienePermiso(RolUsuario.ADMIN, RolUsuario.RECEPCIONISTA)) {
            facade.actualizarPaciente(p);
        }
    }

    public void actualizarMedico(Medico m) {
        if (tienePermiso(RolUsuario.ADMIN)) {
            facade.actualizarMedico(m);
        }
    }

    public void actualizarCita(Cita cita) {
        if (tienePermiso(RolUsuario.ADMIN, RolUsuario.RECEPCIONISTA)) {
            facade.actualizarCita(cita);
        }
    }

}