package Hospital.creacional;

import Hospital.model.Especialidad;
import Hospital.model.Medico;

public class MedicoFactory {

    public static Medico crearMedico(int id, String nombre, String apellido, Especialidad especialidad) {
        return new Medico(id, nombre, apellido, especialidad, true);
    }
}