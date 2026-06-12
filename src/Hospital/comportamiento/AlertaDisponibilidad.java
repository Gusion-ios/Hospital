package Hospital.comportamiento;

import Hospital.model.Cita;

public class AlertaDisponibilidad implements CitaObserver {

    @Override
    public void actualizar(Cita cita) {
        if (cita.getEstado().getNombre().equals("CANCELADA")) {
            System.out.println("ALERTA: Horario disponible para el Dr. "
                    + cita.getMedico().getNombre() + " el " + cita.getFecha());
        }
    }
}