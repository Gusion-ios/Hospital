package Hospital.comportamiento;

import Hospital.model.Cita;

public class EstadoCancelada implements EstadoCita {

    @Override
    public void confirmar(Cita cita) {
        System.out.println("No se puede confirmar una cita cancelada.");
    }
    @Override
    public void cancelar(Cita cita) {
        System.out.println("La cita ya está cancelada.");
    }
    @Override
    public void completar(Cita cita) {
        System.out.println("No se puede completar una cita cancelada.");
    }
    @Override
    public String getNombre() { return "CANCELADA"; }
}