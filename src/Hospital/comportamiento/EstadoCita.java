package Hospital.comportamiento;

import Hospital.model.Cita;

public interface EstadoCita {
    void confirmar(Cita cita);
    void cancelar(Cita cita);
    void completar(Cita cita);
    String getNombre();
}