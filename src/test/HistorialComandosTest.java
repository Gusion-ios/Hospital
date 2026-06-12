package test;

import Hospital.comportamiento.ComandoAgendarCita;
import Hospital.comportamiento.ComandoCancelarCita;
import Hospital.comportamiento.HistorialComandos;
import Hospital.creacional.CitaBuilder;
import Hospital.creacional.GestorHospital;
import Hospital.model.Cita;
import Hospital.model.Especialidad;
import Hospital.model.Medico;
import Hospital.model.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HistorialComandosTest {

    private Cita cita;
    private GestorHospital gestor;
    private HistorialComandos historial;

    @BeforeEach
    void setUp() {
        gestor   = GestorHospital.getInstancia();
        historial = new HistorialComandos();

        Paciente p = new Paciente(1, "Juan", "Quispe", "12345678", "999111", "");
        Medico m   = new Medico(1, "Carlos", "Pérez", Especialidad.CARDIOLOGIA, true);
        cita = new CitaBuilder()
                .id(99).paciente(p).medico(m)
                .fecha("2025-01-01").motivo("Test")
                .urgente(false).costo(50.0)
                .build();
    }

    @Test
    void agendarCitaLaAgregaAlGestor() {
        historial.ejecutar(new ComandoAgendarCita(cita, gestor));
        assertTrue(gestor.getCitas().contains(cita));
    }

    @Test
    void deshacerAgendarEliminaCita() {
        historial.ejecutar(new ComandoAgendarCita(cita, gestor));
        historial.deshacerUltimo();
        assertFalse(gestor.getCitas().contains(cita));
    }

    @Test
    void cancelarYDeshacerRestauraPendiente() {
        historial.ejecutar(new ComandoAgendarCita(cita, gestor));
        historial.ejecutar(new ComandoCancelarCita(cita));
        assertEquals("CANCELADA", cita.getEstado().getNombre());
        historial.deshacerUltimo();
        assertEquals("PENDIENTE", cita.getEstado().getNombre());
    }
}