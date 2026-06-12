package Hospital.view.forms;

import Hospital.model.RolUsuario;
import Hospital.estructural.Sesion;
import Hospital.view.VMain;
import Hospital.view.forms.components.TextButton;

import javax.swing.*;
import java.awt.*;

public class VConfi extends JPanel {

    private VMain parent;
    private JTextField fNombre;
    private JComboBox<RolUsuario> cbRol;

    public VConfi(VMain parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 30));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        construir();
    }

    private void construir() {
        JLabel titulo = new JLabel("Configuración");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 15));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 300));

        JLabel lblUsuario = new JLabel("Usuario activo:");
        lblUsuario.setForeground(Color.WHITE);

        JLabel lblUsuarioValor = new JLabel(
                Sesion.getInstancia().getUsuarioActivo().getNombre() +
                        " (" + Sesion.getInstancia().getUsuarioActivo().getRol() + ")"
        );
        lblUsuarioValor.setForeground(new Color(160, 160, 200));

        JLabel lblNombre = new JLabel("Nuevo nombre:");
        lblNombre.setForeground(Color.WHITE);
        fNombre = new JTextField();

        JLabel lblRol = new JLabel("Nuevo rol:");
        lblRol.setForeground(Color.WHITE);
        cbRol = new JComboBox<>(RolUsuario.values());

        TextButton btnGuardar = new TextButton("Guardar cambios", new Color(59, 130, 246));
        btnGuardar.addActionListener(e -> {
            String nombre = fNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
                return;
            }
            Sesion.getInstancia().cambiarUsuario(nombre, (RolUsuario) cbRol.getSelectedItem());
            lblUsuarioValor.setText(nombre + " (" + cbRol.getSelectedItem() + ")");
            JOptionPane.showMessageDialog(this, "Usuario cambiado correctamente.");
        });

        form.add(lblUsuario);    form.add(lblUsuarioValor);
        form.add(lblNombre);     form.add(fNombre);
        form.add(lblRol);        form.add(cbRol);
        form.add(new JLabel()); form.add(btnGuardar);

        add(form, BorderLayout.CENTER);
    }
}