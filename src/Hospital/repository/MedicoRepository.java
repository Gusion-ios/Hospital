package Hospital.repository;

import Hospital.model.Medico;
import java.util.List;

public interface MedicoRepository {
    void guardar(Medico medico);
    Medico buscarPorId(int id);
    List<Medico> listar();
    void eliminar(int id);
}