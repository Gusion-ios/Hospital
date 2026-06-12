package Hospital.creacional;

import Hospital.model.Cita;
import Hospital.model.Medico;
import Hospital.model.Paciente;

public class CitaBuilder {

    private int id;
    private Paciente paciente;
    private Medico medico;
    private String fecha;
    private String motivo;
    private boolean urgente = false;
    private double costo = 0.0;

    public CitaBuilder id(int id) {
        this.id = id;
        return this;
    }
    public CitaBuilder paciente(Paciente paciente) {
        this.paciente = paciente;
        return this;
    }
    public CitaBuilder medico(Medico medico) {
        this.medico = medico;
        return this;
    }
    public CitaBuilder fecha(String fecha) {
        this.fecha = fecha;
        return this;
    }
    public CitaBuilder motivo(String motivo) {
        this.motivo = motivo;
        return this;
    }
    public CitaBuilder urgente(boolean urgente) {
        this.urgente = urgente;
        return this;
    }
    public CitaBuilder costo(double costo) {
        this.costo = costo;
        return this;
    }
    public Cita build() {
        return new Cita(id, paciente, medico, fecha, motivo, urgente, costo);
    }
}