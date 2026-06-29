package Hospital.model;

import Hospital.comportamiento.EstadoCita;
import Hospital.comportamiento.EstadoPendiente;
import java.util.ArrayList;
import java.util.List;

public class Cita implements ICita {

    private int id;
    private Paciente paciente;
    private Medico medico;
    private String fecha;
    private String motivo;
    private boolean urgente;
    private double costo;
    private EstadoCita estado;

    public Cita(int id, Paciente paciente, Medico medico, String fecha, String motivo, boolean urgente, double costo) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.fecha = fecha;
        this.motivo = motivo;
        this.urgente = urgente;
        this.costo = costo;
        this.estado = new EstadoPendiente();
    }

    public void confirmar() { estado.confirmar(this); }
    public void cancelar()  { estado.cancelar(this); }
    public void completar() { estado.completar(this); }

    public int getId()                          { return id; }
    public Paciente getPaciente()               { return paciente; }
    public Medico getMedico()                   { return medico; }
    public String getFecha()                    { return fecha; }
    public void setFecha(String fecha)          { this.fecha = fecha; }
    public String getMotivo()                   { return motivo; }
    public void setMotivo(String motivo)        { this.motivo = motivo; }
    public boolean isUrgente()                  { return urgente; }
    public void setUrgente(boolean urgente)     { this.urgente = urgente; }
    public double getCosto()                    { return costo; }
    public void setCosto(double costo)          { this.costo = costo; }
    public EstadoCita getEstado()               { return estado; }
    public void setEstado(EstadoCita estado)    { this.estado = estado; }
}