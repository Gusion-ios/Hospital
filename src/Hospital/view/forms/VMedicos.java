package Hospital.view.forms;

import Hospital.estructural.Sesion;
import Hospital.model.Especialidad;
import Hospital.model.Medico;
import Hospital.model.RolUsuario;
import Hospital.view.VMain;
import Hospital.view.forms.components.TextButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VMedicos extends JPanel {

    private VMain parent;
    private DefaultTableModel modelo;
    private JTable tabla;

    public VMedicos(VMain parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 30));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        construir();
    }

    private void construir() {
        JLabel titulo = new JLabel("Gestión de Médicos");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Apellido", "Especialidad", "Disponible"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setBackground(new Color(30, 30, 45));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(50, 50, 70));
        tabla.setRowHeight(30);
        tabla.getTableHeader().setBackground(new Color(45, 45, 60));
        tabla.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(new Color(30, 30, 45));
        add(scroll, BorderLayout.CENTER);

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        sur.setOpaque(false);

        TextButton btnAgregar  = new TextButton("+ Agregar Médico", new Color(59, 130, 246));
        TextButton btnEliminar = new TextButton("Eliminar",         new Color(220, 53, 69));

        btnAgregar.addActionListener(e -> dialogoAgregar());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        sur.add(btnAgregar);
        sur.add(btnEliminar);
        add(sur, BorderLayout.SOUTH);

        refrescar();
    }

    public void refrescar() {
        modelo.setRowCount(0);
        List<Medico> lista = Sesion.getInstancia().getProxy().getMedicos();
        for (Medico m : lista) {
            modelo.addRow(new Object[]{
                    m.getId(), m.getNombre(), m.getApellido(),
                    m.getEspecialidad().name(), m.isDisponible()
            });
        }
    }

    private void dialogoAgregar() {
        if (Sesion.getInstancia().getUsuarioActivo().getRol() != RolUsuario.ADMIN) {
            JOptionPane.showMessageDialog(this, "Solo el ADMIN puede agregar médicos.");
            return;
        }

        JTextField fId       = new JTextField();
        JTextField fNombre   = new JTextField();
        JTextField fApellido = new JTextField();
        JComboBox<Especialidad> fEsp = new JComboBox<>(Especialidad.values());

        Object[] campos = {
                "ID:", fId, "Nombre:", fNombre,
                "Apellido:", fApellido, "Especialidad:", fEsp
        };

        int result = JOptionPane.showConfirmDialog(
                this, campos, "Nuevo Médico", JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                Sesion.getInstancia().getProxy().registrarMedico(
                        Integer.parseInt(fId.getText()),
                        fNombre.getText(), fApellido.getText(),
                        (Especialidad) fEsp.getSelectedItem()
                );
                refrescar();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un número.");
            }
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un médico.");
            return;
        }
        if (Sesion.getInstancia().getUsuarioActivo().getRol() != RolUsuario.ADMIN) {
            JOptionPane.showMessageDialog(this, "Solo el ADMIN puede eliminar médicos.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this, "¿Eliminar este médico?", "Confirmar", JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            List<Medico> lista = Sesion.getInstancia().getProxy().getMedicos();
            Medico m = lista.get(fila);
            Sesion.getInstancia().getProxy().eliminarMedico(m.getId());
            refrescar();
        }
    }
}