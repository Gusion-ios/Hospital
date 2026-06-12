package test;

import Hospital.creacional.CitaBuilder;
import Hospital.estructural.CitaConSeguro;
import Hospital.estructural.CitaConUrgencia;
import Hospital.model.Cita;
import Hospital.model.Especialidad;
import Hospital.model.ICita;
import Hospital.model.Medico;
import Hospital.model.Paciente;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DecoratorTest {

    @Test
    void urgenciaAgregaRecargo() {
        ICita base = crearCita(100.0);
        ICita urgente = new CitaConUrgencia(base);
        assertEquals(150.0, urgente.getCosto());
        assertTrue(urgente.isUrgente());
    }

    @Test
    void seguroAplicaDescuento() {
        ICita base = crearCita(100.0);
        ICita conSeguro = new CitaConSeguro(base);
        assertEquals(80.0, conSeguro.getCosto());
    }

    @Test
    void urgenciaYSeguroEncadenados() {
        ICita base = crearCita(100.0);
        ICita ambos = new CitaConSeguro(new CitaConUrgencia(base));
        assertEquals(120.0, ambos.getCosto());
    }

    private ICita crearCita(double costo) {
        Paciente p = new Paciente(1, "Juan", "Quispe", "12345678", "999111", "");
        Medico m   = new Medico(1, "Carlos", "Pérez", Especialidad.CARDIOLOGIA, true);
        return new CitaBuilder()
                .id(1).paciente(p).medico(m)
                .fecha("2025-01-01").motivo("Test")
                .urgente(false).costo(costo)
                .build();
    }
}