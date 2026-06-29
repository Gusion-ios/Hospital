package Hospital.creacional;

import Hospital.model.Cita;
import Hospital.model.Medico;
import Hospital.model.Paciente;

import java.util.ArrayList;
import java.util.List;

public class GestorHospital {

    private static GestorHospital instancia;

    private List<Paciente> pacientes = new ArrayList<>();
    private List<Medico> medicos = new ArrayList<>();
    private List<Cita> citas = new ArrayList<>();

    private GestorHospital() {}

    public static GestorHospital getInstancia() {
        if (instancia == null) {
            instancia = new GestorHospital();
        }
        return instancia;
    }

    public void agregarPaciente(Paciente p) { pacientes.add(p); }
    public void agregarMedico(Medico m) { medicos.add(m); }
    public void agregarCita(Cita c) { citas.add(c); }

    public void eliminarCita(Cita c) { citas.remove(c); }

    public List<Paciente> getPacientes() { return pacientes; }
    public List<Medico> getMedicos() { return medicos; }
    public List<Cita> getCitas() { return citas; }

    public void limpiar() {
        pacientes.clear();
        medicos.clear();
        citas.clear();
    }

    public int siguienteIdPaciente() {
        return pacientes.stream().mapToInt(p -> p.getId()).max().orElse(0) + 1;
    }

    public int siguienteIdMedico() {
        return medicos.stream().mapToInt(m -> m.getId()).max().orElse(0) + 1;
    }

    public int siguienteIdCita() {
        return citas.stream().mapToInt(c -> c.getId()).max().orElse(0) + 1;
    }


}