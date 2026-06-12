package Hospital.comportamiento;

import Hospital.model.Cita;

public class EstadoPendiente implements EstadoCita {

    @Override
    public void confirmar(Cita cita) {
        cita.setEstado(new EstadoConfirmada());
    }
    @Override
    public void cancelar(Cita cita) {
        cita.setEstado(new EstadoCancelada());
    }
    @Override
    public void completar(Cita cita) {
        System.out.println("La cita debe confirmarse antes de completarse.");
    }
    @Override
    public String getNombre() { return "PENDIENTE"; }
}