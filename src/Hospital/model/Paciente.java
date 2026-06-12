package Hospital.model;

public class Paciente implements Cloneable{

    private int id;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String historialMedico;

    public Paciente(int id, String nombre, String apellido, String dni, String telefono, String historialMedico) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.historialMedico = historialMedico;
    }

    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public String getDni() {
        return dni;
    }
    public String getHistorialMedico() {
        return historialMedico;
    }
    public void setHistorialMedico(String historialMedico) {
        this.historialMedico = historialMedico;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public Paciente clone() {
        try {
            return (Paciente) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);

        }

    }


}
