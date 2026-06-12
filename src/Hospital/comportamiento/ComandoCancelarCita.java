package Hospital.comportamiento;

import Hospital.model.Cita;

public class ComandoCancelarCita implements Comando {

    private Cita cita;
    private String estadoAnterior;

    public ComandoCancelarCita(Cita cita) {
        this.cita = cita;
    }

    @Override
    public void ejecutar() {
        estadoAnterior = cita.getEstado().getNombre();
        cita.cancelar();
    }

    @Override
    public void deshacer() {
        if (estadoAnterior.equals("PENDIENTE")) {
            cita.setEstado(new EstadoPendiente());
        } else if (estadoAnterior.equals("CONFIRMADA")) {
            cita.setEstado(new EstadoConfirmada());
        }
    }
}