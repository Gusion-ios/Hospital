package Hospital.model;

public interface ICita {
    int getId();
    Paciente getPaciente();
    Medico getMedico();
    String getFecha();
    String getMotivo();
    boolean isUrgente();
    double getCosto();
    Hospital.comportamiento.EstadoCita getEstado();
    void confirmar();
    void cancelar();
    void completar();

}