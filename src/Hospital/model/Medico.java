package Hospital.model;

public class Medico {

    private int id;
    private String nombre;
    private String apellido;
    private Especialidad especialidad;
    private boolean disponible;

    public Medico(int id, String nombre, String apellido, Especialidad especialidad, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidad = especialidad;
        this.disponible = disponible;
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
    public Especialidad getEspecialidad() {
        return especialidad;
    }
    public boolean isDisponible() {
        return disponible;
    }
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }




}
