package Hospital.comportamiento;

import java.util.Stack;

public class HistorialComandos {

    private Stack<Comando> historial = new Stack<>();

    public void ejecutar(Comando comando) {
        comando.ejecutar();
        historial.push(comando);
    }

    public void deshacerUltimo() {
        if (!historial.isEmpty()) {
            historial.pop().deshacer();
        } else {
            System.out.println("No hay acciones para deshacer.");
        }
    }
}