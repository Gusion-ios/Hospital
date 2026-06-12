package Hospital.repository;

import Hospital.model.Paciente;
import java.util.List;

public interface PacienteRepository {
    void guardar(Paciente paciente);
    Paciente buscarPorId(int id);
    List<Paciente> listar();
    void eliminar(int id);
}