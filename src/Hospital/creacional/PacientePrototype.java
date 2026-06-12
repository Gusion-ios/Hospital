package Hospital.creacional;

import Hospital.model.Paciente;

public class PacientePrototype {

    public static Paciente clonar(Paciente original) {
        return original.clone();
    }
}