package Hospital.view.forms.components;

import javax.swing.*;
import java.awt.*;

public class RoundPanel extends JPanel {

    private int radio;
    private Color color;

    public RoundPanel(int radio, Color color) {
        this.radio = radio;
        this.color = color;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
        g2.dispose();
    }
}