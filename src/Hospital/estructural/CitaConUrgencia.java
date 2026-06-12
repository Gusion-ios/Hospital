package Hospital.estructural;

import Hospital.model.ICita;

public class CitaConUrgencia extends CitaDecorator {

    private static final double RECARGO = 50.0;

    public CitaConUrgencia(ICita cita) {
        super(cita);
    }

    @Override
    public double getCosto() {
        return citaDecorada.getCosto() + RECARGO;
    }

    @Override
    public boolean isUrgente() {
        return true;
    }
}