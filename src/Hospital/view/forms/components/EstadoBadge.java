package Hospital.view.forms.components;

import javax.swing.*;
import java.awt.*;

public class EstadoBadge extends JLabel {

    public EstadoBadge(String estado) {
        super(estado);
        setOpaque(true);
        setFont(new Font("Arial", Font.BOLD, 11));
        setHorizontalAlignment(CENTER);
        setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        aplicarColor(estado);
    }

    public void aplicarColor(String estado) {
        switch (estado) {
            case "PENDIENTE"   -> { setBackground(new Color(255, 193, 7));  setForeground(Color.BLACK); }
            case "CONFIRMADA"  -> { setBackground(new Color(40, 167, 69));  setForeground(Color.WHITE); }
            case "COMPLETADA"  -> { setBackground(new Color(23, 162, 184)); setForeground(Color.WHITE); }
            case "CANCELADA"   -> { setBackground(new Color(220, 53, 69));  setForeground(Color.WHITE); }
            default            -> { setBackground(Color.GRAY);              setForeground(Color.WHITE); }
        }
        setText(estado);
    }
}