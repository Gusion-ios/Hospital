package test;

import Hospital.creacional.CitaBuilder;
import Hospital.model.Cita;
import Hospital.model.Especialidad;
import Hospital.model.Medico;
import Hospital.model.Paciente;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CitaBuilderTest {

    @Test
    void construyeCitaCorrectamente() {
        Paciente p = new Paciente(1, "Juan", "Quispe", "12345678", "999111", "");
        Medico m   = new Medico(1, "Carlos", "Pérez", Especialidad.CARDIOLOGIA, true);

        Cita cita = new CitaBuilder()
                .id(1)
                .paciente(p)
                .medico(m)
                .fecha("2025-01-01")
                .motivo("Control")
                .urgente(false)
                .costo(50.0)
                .build();

        assertNotNull(cita);
        assertEquals(1, cita.getId());
        assertEquals("Control", cita.getMotivo());
        assertEquals(50.0, cita.getCosto());
        assertEquals("PENDIENTE", cita.getEstado().getNombre());
    }

    @Test
    void estadoInicialEsPendiente() {
        Paciente p = new Paciente(2, "Maria", "Lopez", "87654321", "988333", "");
        Medico m   = new Medico(2, "Ana", "Garcia", Especialidad.PEDIATRIA, true);

        Cita cita = new CitaBuilder()
                .id(2).paciente(p).medico(m)
                .fecha("2025-02-01").motivo("Consulta")
                .urgente(false).costo(30.0)
                .build();

        assertEquals("PENDIENTE", cita.getEstado().getNombre());
    }
}