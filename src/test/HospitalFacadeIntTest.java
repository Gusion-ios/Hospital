package test;

import Hospital.creacional.GestorHospital;
import Hospital.estructural.HospitalFacade;
import Hospital.model.Cita;
import Hospital.model.Especialidad;
import Hospital.model.Medico;
import Hospital.model.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import Hospital.repository.PacienteRepositoryH2;
import Hospital.repository.MedicoRepositoryH2;
import Hospital.repository.CitaRepositoryH2;

public class HospitalFacadeIntTest {

    private HospitalFacade facade;

    @BeforeEach
    void setUp() {
        GestorHospital.getInstancia().limpiar();
        facade = new HospitalFacade(
                new PacienteRepositoryH2(),
                new MedicoRepositoryH2(),
                new CitaRepositoryH2()
        );
    }

    @Test
    void registrarPacienteLoAgregaALaLista() {
        facade.registrarPaciente(10, "Pedro", "Ramos", "11111111", "900000001", "");
        assertTrue(facade.getPacientes().stream()
                .anyMatch(p -> p.getDni().equals("11111111")));
    }

    @Test
    void registrarMedicoLoAgregaALaLista() {
        facade.registrarMedico(10, "Luis", "Torres", Especialidad.NEUROLOGIA);
        assertTrue(facade.getMedicos().stream()
                .anyMatch(m -> m.getEspecialidad() == Especialidad.NEUROLOGIA));
    }

    @Test
    void agendarYConfirmarCita() {
        facade.registrarPaciente(20, "Ana", "Flores", "22222222", "900000002", "");
        facade.registrarMedico(20, "Mario", "Rios", Especialidad.TRAUMATOLOGIA);

        Paciente p = facade.getPacientes().stream()
                .filter(x -> x.getDni().equals("22222222")).findFirst().orElseThrow();
        Medico m = facade.getMedicos().stream()
                .filter(x -> x.getEspecialidad() == Especialidad.TRAUMATOLOGIA).findFirst().orElseThrow();

        facade.agendarCita(50, p, m, "2025-03-01", "Fractura", false, 80.0);
        Cita cita = facade.getCitas().stream()
                .filter(c -> c.getId() == 50).findFirst().orElseThrow();

        assertEquals("PENDIENTE", cita.getEstado().getNombre());
        facade.confirmarCita(cita);
        assertEquals("CONFIRMADA", cita.getEstado().getNombre());
    }
}