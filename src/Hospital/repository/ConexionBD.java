package Hospital.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static ConexionBD instancia;
    private Connection connection;

    private static final String URL  = "jdbc:postgresql://localhost:5432/hospital_bd";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Xnxxxnxx123@"; // cambia por tu contraseña

    private ConexionBD() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión PostgreSQL establecida.");
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con PostgreSQL: " + e.getMessage());
        }
    }

    public static ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public Connection getConnection() {
        return connection;
    }
}