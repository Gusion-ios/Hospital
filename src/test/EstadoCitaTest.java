package test;

import Hospital.comportamiento.*;
import Hospital.creacional.CitaBuilder;
import Hospital.model.Cita;
import Hospital.model.Especialidad;
import Hospital.model.Medico;
import Hospital.model.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EstadoCitaTest {

    private Cita cita;

    @BeforeEach
    void setUp() {
        Paciente p = new Paciente(1, "Juan", "Quispe", "12345678", "999111", "");
        Medico m   = new Medico(1, "Carlos", "Pérez", Especialidad.CARDIOLOGIA, true);
        cita = new CitaBuilder()
                .id(1).paciente(p).medico(m)
                .fecha("2025-01-01").motivo("Control")
                .urgente(false).costo(50.0)
                .build();
    }

    @Test
    void pendienteConfirmaCorrectamente() {
        cita.confirmar();
        assertEquals("CONFIRMADA", cita.getEstado().getNombre());
    }

    @Test
    void confirmadaCompletaCorrectamente() {
        cita.confirmar();
        cita.completar();
        assertEquals("COMPLETADA", cita.getEstado().getNombre());
    }

    @Test
    void pendienteCancelaCorrectamente() {
        cita.cancelar();
        assertEquals("CANCELADA", cita.getEstado().getNombre());
    }

    @Test
    void canceladaNoPermiteConfirmar() {
        cita.cancelar();
        cita.confirmar();
        assertEquals("CANCELADA", cita.getEstado().getNombre());
    }

    @Test
    void completadaNoPermiteCancelar() {
        cita.confirmar();
        cita.completar();
        cita.cancelar();
        assertEquals("COMPLETADA", cita.getEstado().getNombre());
    }
}