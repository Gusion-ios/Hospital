package Hospital.repository;

import Hospital.model.Cita;
import java.util.List;

public interface CitaRepository {
    void guardar(Cita cita);
    Cita buscarPorId(int id);
    List<Cita> listar();
    void eliminar(int id);
}