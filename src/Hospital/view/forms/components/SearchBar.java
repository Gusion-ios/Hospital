package Hospital.view.forms.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SearchBar extends JPanel {

    private JTextField campo;

    public SearchBar(String placeholder, Runnable onBuscar) {
        setLayout(new BorderLayout(5, 0));
        setOpaque(false);

        campo = new JTextField();
        campo.putClientProperty("JTextField.placeholderText", placeholder);
        campo.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton btnBuscar = new TextButton("Buscar", new Color(59, 130, 246));

        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) onBuscar.run();
            }
        });
        btnBuscar.addActionListener(e -> onBuscar.run());

        add(campo, BorderLayout.CENTER);
        add(btnBuscar, BorderLayout.EAST);
    }

    public String getTexto() {
        return campo.getText().trim();
    }

    public void limpiar() {
        campo.setText("");
    }
}