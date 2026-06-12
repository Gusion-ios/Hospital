package Hospital.model;

public class Usuario {

    private String nombre;
    private RolUsuario rol;

    public Usuario(String nombre, RolUsuario rol) {
        this.nombre = nombre;
        this.rol = rol;
    }


    public String getNombre() {
        return nombre;
    }
    public RolUsuario getRol() {
        return rol;
    }



}
