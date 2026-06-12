package Hospital.comportamiento;

import Hospital.creacional.GestorHospital;
import Hospital.model.Cita;

public class ComandoAgendarCita implements Comando {

    private Cita cita;
    private GestorHospital gestor;

    public ComandoAgendarCita(Cita cita, GestorHospital gestor) {
        this.cita = cita;
        this.gestor = gestor;
    }

    @Override
    public void ejecutar() {
        gestor.agregarCita(cita);
    }

    @Override
    public void deshacer() {
        gestor.eliminarCita(cita);
    }
}