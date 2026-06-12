package Hospital.repository;

import Hospital.comportamiento.EstadoPendiente;
import Hospital.comportamiento.EstadoConfirmada;
import Hospital.comportamiento.EstadoCancelada;
import Hospital.comportamiento.EstadoCompletada;
import Hospital.model.Cita;
import Hospital.model.Medico;
import Hospital.model.Paciente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaRepositoryH2 implements CitaRepository {

    private Connection conn;
    private PacienteRepository pacienteRepo;
    private MedicoRepository medicoRepo;

    public CitaRepositoryH2() {
        this.conn = ConexionBD.getInstancia().getConnection();
        this.pacienteRepo = new PacienteRepositoryH2();
        this.medicoRepo = new MedicoRepositoryH2();
        crearTabla();
    }

    private void crearTabla() {
        String sql = """
                CREATE TABLE IF NOT EXISTS citas (
                    id INT PRIMARY KEY,
                    paciente_id INT,
                    medico_id INT,
                    fecha VARCHAR(50),
                    motivo VARCHAR(200),
                    urgente BOOLEAN,
                    costo DOUBLE PRECISION,
                    estado VARCHAR(20)
                )""";
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear tabla citas: " + e.getMessage());
        }
    }

    @Override
    public void guardar(Cita c) {
        String sql = """
        INSERT INTO citas VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT (id) DO UPDATE SET
            paciente_id = EXCLUDED.paciente_id,
            medico_id   = EXCLUDED.medico_id,
            fecha       = EXCLUDED.fecha,
            motivo      = EXCLUDED.motivo,
            urgente     = EXCLUDED.urgente,
            costo       = EXCLUDED.costo,
            estado      = EXCLUDED.estado
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getId());
            ps.setInt(2, c.getPaciente().getId());
            ps.setInt(3, c.getMedico().getId());
            ps.setString(4, c.getFecha());
            ps.setString(5, c.getMotivo());
            ps.setBoolean(6, c.isUrgente());
            ps.setDouble(7, c.getCosto());
            ps.setString(8, c.getEstado().getNombre());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar cita: " + e.getMessage());
        }
    }

    @Override
    public Cita buscarPorId(int id) {
        String sql = "SELECT * FROM citas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearCita(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cita: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Cita> listar() {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM citas";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearCita(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar citas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM citas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cita: " + e.getMessage());
        }
    }

    private Cita mapearCita(ResultSet rs) throws SQLException {
        Paciente paciente = pacienteRepo.buscarPorId(rs.getInt("paciente_id"));
        Medico medico = medicoRepo.buscarPorId(rs.getInt("medico_id"));
        Cita cita = new Cita(
                rs.getInt("id"), paciente, medico,
                rs.getString("fecha"), rs.getString("motivo"),
                rs.getBoolean("urgente"), rs.getDouble("costo")
        );
        switch (rs.getString("estado")) {
            case "CONFIRMADA" -> cita.setEstado(new EstadoConfirmada());
            case "COMPLETADA" -> cita.setEstado(new EstadoCompletada());
            case "CANCELADA"  -> cita.setEstado(new EstadoCancelada());
            default           -> cita.setEstado(new EstadoPendiente());
        }
        return cita;
    }
}