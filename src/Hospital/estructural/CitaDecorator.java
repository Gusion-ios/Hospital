package Hospital.estructural;

import Hospital.comportamiento.EstadoCita;
import Hospital.model.ICita;
import Hospital.model.Medico;
import Hospital.model.Paciente;

public abstract class CitaDecorator implements ICita {

    protected ICita citaDecorada;

    public CitaDecorator(ICita cita) {
        this.citaDecorada = cita;
    }

    @Override public int getId()              { return citaDecorada.getId(); }
    @Override public Paciente getPaciente()   { return citaDecorada.getPaciente(); }
    @Override public Medico getMedico()       { return citaDecorada.getMedico(); }
    @Override public String getFecha()        { return citaDecorada.getFecha(); }
    @Override public String getMotivo()       { return citaDecorada.getMotivo(); }
    @Override public boolean isUrgente()      { return citaDecorada.isUrgente(); }
    @Override public double getCosto()        { return citaDecorada.getCosto(); }
    @Override public EstadoCita getEstado()   { return citaDecorada.getEstado(); }
    @Override public void confirmar()         { citaDecorada.confirmar(); }
    @Override public void cancelar()          { citaDecorada.cancelar(); }
    @Override public void completar()         { citaDecorada.completar(); }

}