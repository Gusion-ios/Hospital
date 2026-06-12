package Hospital.repository;

import Hospital.model.Paciente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteRepositoryH2 implements PacienteRepository {

    private Connection conn;

    public PacienteRepositoryH2() {
        this.conn = ConexionBD.getInstancia().getConnection();
        crearTabla();
    }

    private void crearTabla() {
        String sql = """
                CREATE TABLE IF NOT EXISTS pacientes (
                    id INT PRIMARY KEY,
                    nombre VARCHAR(100),
                    apellido VARCHAR(100),
                    dni VARCHAR(20),
                    telefono VARCHAR(20),
                    historial VARCHAR(500)
                )""";
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear tabla pacientes: " + e.getMessage());
        }
    }

    @Override
    public void guardar(Paciente p) {
        String sql = """
        INSERT INTO pacientes VALUES (?, ?, ?, ?, ?, ?)
        ON CONFLICT (id) DO UPDATE SET
            nombre   = EXCLUDED.nombre,
            apellido = EXCLUDED.apellido,
            dni      = EXCLUDED.dni,
            telefono = EXCLUDED.telefono,
            historial = EXCLUDED.historial
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getApellido());
            ps.setString(4, p.getDni());
            ps.setString(5, p.getTelefono());
            ps.setString(6, p.getHistorialMedico());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar paciente: " + e.getMessage());
        }
    }

    @Override
    public Paciente buscarPorId(int id) {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Paciente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("historial")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar paciente: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Paciente> listar() {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientes";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Paciente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("historial")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pacientes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM pacientes WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar paciente: " + e.getMessage());
        }
    }
}