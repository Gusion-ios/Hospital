package Hospital;

import Hospital.estructural.Sesion;
import Hospital.view.VMain;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.width", 8);
            UIManager.put("TabbedPane.selectedBackground", new java.awt.Color(255, 255, 255));
            UIManager.put("Table.rowHeight", 32);
            UIManager.put("TableHeader.height", 36);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sesion.getInstancia();
        SwingUtilities.invokeLater(() -> {
            VMain ventana = new VMain();
            ventana.setVisible(true);
        });
    }
}