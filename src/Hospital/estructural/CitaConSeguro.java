package Hospital.estructural;

import Hospital.model.ICita;

public class CitaConSeguro extends CitaDecorator {

    private static final double DESCUENTO = 0.20;

    public CitaConSeguro(ICita cita) {
        super(cita);
    }

    @Override
    public double getCosto() {
        return citaDecorada.getCosto() * (1 - DESCUENTO);
    }
}