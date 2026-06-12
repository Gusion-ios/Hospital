package Hospital.comportamiento;

import Hospital.model.Cita;

public class EstadoCompletada implements EstadoCita {

    @Override
    public void confirmar(Cita cita) {
        System.out.println("La cita ya fue completada.");
    }
    @Override
    public void cancelar(Cita cita) {
        System.out.println("No se puede cancelar una cita completada.");
    }
    @Override
    public void completar(Cita cita) {
        System.out.println("La cita ya fue completada.");
    }
    @Override
    public String getNombre() { return "COMPLETADA"; }
}