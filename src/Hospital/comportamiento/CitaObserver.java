package Hospital.comportamiento;

import Hospital.model.Cita;

public interface CitaObserver {
    void actualizar(Cita cita); // cita puede ser null para refrescos generales
}