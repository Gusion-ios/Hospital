package Hospital.view.forms.components;

import javax.swing.*;
import java.awt.*;

public class TextButton extends JButton {

    public TextButton(String texto, Color fondo) {
        super(texto);
        setBackground(fondo);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("Arial", Font.BOLD, 12));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }
}