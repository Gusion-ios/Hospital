package Hospital.estructural;

import Hospital.comportamiento.*;
import Hospital.creacional.CitaBuilder;
import Hospital.creacional.GestorHospital;
import Hospital.creacional.MedicoFactory;
import Hospital.creacional.PacientePrototype;
import Hospital.model.Cita;
import Hospital.model.Especialidad;
import Hospital.model.Medico;
import Hospital.model.Paciente;
import Hospital.repository.*;

import java.util.List;

public class HospitalFacade {

    private GestorHospital gestor;
    private HistorialComandos historial;
    private CitaObserver alerta;
    private PacienteRepository pacienteRepo;
    private MedicoRepository medicoRepo;
    private CitaRepository citaRepo;

    // Constructor con inyección de dependencias — DIP aplicado
    public HospitalFacade(PacienteRepository pacienteRepo,
                          MedicoRepository medicoRepo,
                          CitaRepository citaRepo) {
        this.gestor       = GestorHospital.getInstancia();
        this.historial    = new HistorialComandos();
        this.alerta       = new AlertaDisponibilidad();
        this.pacienteRepo = pacienteRepo;
        this.medicoRepo   = medicoRepo;
        this.citaRepo     = citaRepo;
        cargarDesdeDB();
    }

    private void cargarDesdeDB() {
        pacienteRepo.listar().forEach(gestor::agregarPaciente);
        medicoRepo.listar().forEach(gestor::agregarMedico);
        citaRepo.listar().forEach(gestor::agregarCita);
    }

    public void registrarPaciente(int id, String nombre, String apellido,
                                  String dni, String telefono, String historial) {
        Paciente p = new Paciente(id, nombre, apellido, dni, telefono, historial);
        gestor.agregarPaciente(p);
        pacienteRepo.guardar(p);
    }

    public void clonarPaciente(Paciente original) {
        Paciente clon = PacientePrototype.clonar(original);
        clon = new Paciente(
                gestor.getPacientes().size() + 1,
                clon.getNombre(), clon.getApellido(),
                clon.getDni() + "_clon",
                clon.getTelefono(), clon.getHistorialMedico()
        );
        gestor.agregarPaciente(clon);
        pacienteRepo.guardar(clon);
    }

    public void registrarMedico(int id, String nombre, String apellido, Especialidad especialidad) {
        Medico m = MedicoFactory.crearMedico(id, nombre, apellido, especialidad);
        gestor.agregarMedico(m);
        medicoRepo.guardar(m);
    }

    public void agendarCita(int id, Paciente paciente, Medico medico,
                            String fecha, String motivo, boolean urgente, double costo) {
        Cita cita = new CitaBuilder()
                .id(id).paciente(paciente).medico(medico)
                .fecha(fecha).motivo(motivo).urgente(urgente).costo(costo)
                .build();
        historial.ejecutar(new ComandoAgendarCita(cita, gestor));
        citaRepo.guardar(cita);
    }

    public void confirmarCita(Cita cita) {
        cita.confirmar();
        citaRepo.guardar(cita);
    }

    public void cancelarCita(Cita cita) {
        historial.ejecutar(new ComandoCancelarCita(cita));
        alerta.actualizar(cita);
        citaRepo.guardar(cita);
    }

    public void completarCita(Cita cita) {
        cita.completar();
        citaRepo.guardar(cita);
    }

    public void eliminarPaciente(int id) {
        gestor.getPacientes().removeIf(p -> p.getId() == id);
        pacienteRepo.eliminar(id);
    }

    public void eliminarMedico(int id) {
        gestor.getMedicos().removeIf(m -> m.getId() == id);
        medicoRepo.eliminar(id);
    }

    public void deshacerUltimaAccion() {
        historial.deshacerUltimo();
        gestor.getCitas().forEach(citaRepo::guardar);
    }

    public List<Paciente> getPacientes() { return gestor.getPacientes(); }
    public List<Medico>   getMedicos()   { return gestor.getMedicos(); }
    public List<Cita>     getCitas()     { return gestor.getCitas(); }
}