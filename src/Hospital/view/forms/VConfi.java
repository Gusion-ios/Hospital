package Hospital.view.forms;

import Hospital.model.RolUsuario;
import Hospital.estructural.Sesion;
import Hospital.view.VMain;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class VConfi extends JPanel {

    private VMain parent;
    private JTextField fNombre;
    private JComboBox<RolUsuario> cbRol;
    private JLabel lblUsuarioValor;

    public VConfi(VMain parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(VMain.FONDO);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        construir();
    }

    private void construir() {
        // Header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        JLabel titulo = new JLabel("Configuracion");
        titulo.setForeground(VMain.TEXTO_DARK);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("Gestion de usuario activo y permisos del sistema");
        subtitulo.setForeground(VMain.TEXTO_GRAY);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        header.add(titulo);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitulo);
        add(header, BorderLayout.NORTH);

        // Card de configuracion
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(VMain.BLANCO);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VMain.BORDE, 1),
                BorderFactory.createEmptyBorder(24, 24, 24, 24)
        ));

        // Usuario activo
        JLabel secUsuario = new JLabel("Usuario activo");
        secUsuario.setForeground(VMain.TEXTO_GRAY);
        secUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        lblUsuarioValor = new JLabel(
                Sesion.getInstancia().getUsuarioActivo().getNombre() +
                        "  |  " + Sesion.getInstancia().getUsuarioActivo().getRol()
        );
        lblUsuarioValor.setForeground(VMain.AZUL_MEDIO);
        lblUsuarioValor.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JSeparator sep = new JSeparator();
        sep.setForeground(VMain.BORDE);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // Nuevo nombre
        JLabel lblNombre = new JLabel("Nuevo nombre de usuario");
        lblNombre.setForeground(VMain.TEXTO_DARK);
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        fNombre = new JTextField();
        fNombre.setMaximumSize(new Dimension(340, 36));
        fNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VMain.BORDE, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        // Nuevo rol
        JLabel lblRol = new JLabel("Nuevo rol");
        lblRol.setForeground(VMain.TEXTO_DARK);
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        cbRol = new JComboBox<>(RolUsuario.values());
        cbRol.setMaximumSize(new Dimension(340, 36));
        cbRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Info de permisos
        JPanel infoBox = new JPanel(new BorderLayout());
        infoBox.setMaximumSize(new Dimension(340, 80));
        infoBox.setBackground(new Color(232, 244, 253));
        infoBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 220, 245), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        JLabel infoText = new JLabel("<html>" +
                "<b>ADMIN</b> — Acceso total<br>" +
                "<b>RECEPCIONISTA</b> — Pacientes y citas (sin eliminar)<br>" +
                "<b>MEDICO</b> — Solo confirmar y completar citas" +
                "</html>");
        infoText.setForeground(VMain.AZUL_OSCURO);
        infoText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoBox.add(infoText, BorderLayout.CENTER);

        // Boton guardar
        JButton btnGuardar = new JButton("Guardar cambios");
        btnGuardar.setMaximumSize(new Dimension(340, 40));
        btnGuardar.setBackground(VMain.AZUL_MEDIO);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnGuardar.addActionListener(e -> {
            String nombre = fNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacio.");
                return;
            }
            RolUsuario nuevoRol = (RolUsuario) cbRol.getSelectedItem();
            Sesion.getInstancia().cambiarUsuario(nombre, nuevoRol);
            lblUsuarioValor.setText(nombre + "  |  " + nuevoRol);
            parent.actualizarHeaderUsuario();
            parent.refrescarPermisos();
            JOptionPane.showMessageDialog(this,
                    "Usuario cambiado a: " + nombre + " (" + nuevoRol + ")\nPermisos actualizados.");
        });

        card.add(secUsuario);
        card.add(Box.createVerticalStrut(4));
        card.add(lblUsuarioValor);
        card.add(Box.createVerticalStrut(16));
        card.add(sep);
        card.add(Box.createVerticalStrut(16));
        card.add(lblNombre);
        card.add(Box.createVerticalStrut(6));
        card.add(fNombre);
        card.add(Box.createVerticalStrut(14));
        card.add(lblRol);
        card.add(Box.createVerticalStrut(6));
        card.add(cbRol);
        card.add(Box.createVerticalStrut(16));
        card.add(infoBox);
        card.add(Box.createVerticalStrut(20));
        card.add(btnGuardar);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.setOpaque(false);
        wrapper.add(card);
        card.setPreferredSize(new Dimension(400, 420));

        add(wrapper, BorderLayout.CENTER);
    }
}