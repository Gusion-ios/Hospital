package Hospital.comportamiento;

import Hospital.model.Cita;

public class EstadoConfirmada implements EstadoCita {

    @Override
    public void confirmar(Cita cita) {
        System.out.println("La cita ya está confirmada.");
    }
    @Override
    public void cancelar(Cita cita) {
        cita.setEstado(new EstadoCancelada());
    }
    @Override
    public void completar(Cita cita) {
        cita.setEstado(new EstadoCompletada());
    }
    @Override
    public String getNombre() { return "CONFIRMADA"; }
}