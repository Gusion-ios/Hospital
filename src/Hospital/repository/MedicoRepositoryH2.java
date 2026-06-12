package Hospital.repository;

import Hospital.model.Especialidad;
import Hospital.model.Medico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoRepositoryH2 implements MedicoRepository {

    private Connection conn;

    public MedicoRepositoryH2() {
        this.conn = ConexionBD.getInstancia().getConnection();
        crearTabla();
    }

    private void crearTabla() {
        String sql = """
                CREATE TABLE IF NOT EXISTS medicos (
                    id INT PRIMARY KEY,
                    nombre VARCHAR(100),
                    apellido VARCHAR(100),
                    especialidad VARCHAR(50),
                    disponible BOOLEAN
                )""";
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear tabla medicos: " + e.getMessage());
        }
    }

    @Override
    public void guardar(Medico m) {
        String sql = """
        INSERT INTO medicos VALUES (?, ?, ?, ?, ?)
        ON CONFLICT (id) DO UPDATE SET
            nombre       = EXCLUDED.nombre,
            apellido     = EXCLUDED.apellido,
            especialidad = EXCLUDED.especialidad,
            disponible   = EXCLUDED.disponible
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, m.getId());
            ps.setString(2, m.getNombre());
            ps.setString(3, m.getApellido());
            ps.setString(4, m.getEspecialidad().name());
            ps.setBoolean(5, m.isDisponible());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar medico: " + e.getMessage());
        }
    }

    @Override
    public Medico buscarPorId(int id) {
        String sql = "SELECT * FROM medicos WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Medico(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        Especialidad.valueOf(rs.getString("especialidad")),
                        rs.getBoolean("disponible")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar medico: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Medico> listar() {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM medicos";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Medico(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        Especialidad.valueOf(rs.getString("especialidad")),
                        rs.getBoolean("disponible")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar medicos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM medicos WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar medico: " + e.getMessage());
        }
    }
}