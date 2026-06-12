package Hospital;

import Hospital.estructural.Sesion;
import Hospital.view.VMain;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        Sesion.getInstancia();
        SwingUtilities.invokeLater(() -> {
            VMain ventana = new VMain();
            ventana.setVisible(true);
        });
    }
}