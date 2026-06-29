package Hospital.view.forms;

import Hospital.creacional.GestorHospital;
import Hospital.estructural.Sesion;
import Hospital.model.Especialidad;
import Hospital.model.Medico;
import Hospital.model.RolUsuario;
import Hospital.view.VMain;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class VMedicos extends JPanel {

    private VMain parent;
    private DefaultTableModel modelo;
    private JTable tabla;
    private JTextField searchField;

    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnDisponible;
    private JButton btnEliminar;

    public VMedicos(VMain parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(VMain.FONDO);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        construir();
    }

    private void construir() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel headerLeft = new JPanel();
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));
        headerLeft.setOpaque(false);
        JLabel titulo = new JLabel("Gestion de Medicos");
        titulo.setForeground(VMain.TEXTO_DARK);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel sub = new JLabel("Personal medico registrado en el sistema");
        sub.setForeground(VMain.TEXTO_GRAY);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        headerLeft.add(titulo);
        headerLeft.add(Box.createVerticalStrut(3));
        headerLeft.add(sub);
        header.add(headerLeft, BorderLayout.WEST);

        searchField = new JTextField(22);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.putClientProperty("JTextField.placeholderText", "Buscar medico...");
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { buscar(searchField.getText().trim()); }
        });
        header.add(searchField, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Apellido", "Especialidad", "Disponible"}, 0
        ) { public boolean isCellEditable(int r, int c) { return false; } };

        tabla = new JTable(modelo);
        estilizarTabla(tabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(VMain.BORDE, 1));
        scroll.getViewport().setBackground(VMain.BLANCO);
        add(scroll, BorderLayout.CENTER);

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 14));
        sur.setOpaque(false);

        btnAgregar    = crearBtn("+ Agregar",      VMain.AZUL_MEDIO);
        btnEditar     = crearBtn("Editar",         VMain.NARANJA);
        btnDisponible = crearBtn("Disponibilidad", new Color(8, 145, 178));
        btnEliminar   = crearBtn("Eliminar",       VMain.ROJO);

        btnAgregar.addActionListener(e -> dialogoAgregar());
        btnEditar.addActionListener(e -> editarSeleccionado());
        btnDisponible.addActionListener(e -> cambiarDisponibilidad());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        sur.add(btnAgregar);
        sur.add(btnEditar);
        sur.add(btnDisponible);
        sur.add(btnEliminar);
        add(sur, BorderLayout.SOUTH);

        refrescar();
    }

    private void estilizarTabla(JTable t) {
        t.setBackground(VMain.BLANCO);
        t.setForeground(VMain.TEXTO_DARK);
        t.setGridColor(VMain.BORDE);
        t.setRowHeight(36);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setSelectionBackground(VMain.AZUL_CLARO);
        t.setSelectionForeground(VMain.AZUL_OSCURO);
        t.setShowVerticalLines(false);
        t.getTableHeader().setBackground(VMain.FONDO);
        t.getTableHeader().setForeground(VMain.TEXTO_GRAY);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, VMain.BORDE));
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? VMain.BLANCO : new Color(248, 250, 252));
                }
                // Colorear columna Disponible
                if (col == 4 && !isSelected) {
                    boolean disponible = Boolean.TRUE.equals(value) ||
                            "true".equalsIgnoreCase(String.valueOf(value));
                    c.setForeground(disponible ? VMain.VERDE : VMain.ROJO);
                    ((JLabel)c).setText(disponible ? "Disponible" : "No disponible");
                } else {
                    c.setForeground(VMain.TEXTO_DARK);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return c;
            }
        });
    }

    private JButton crearBtn(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return btn;
    }

    public void aplicarPermisos() {
        RolUsuario rol = Sesion.getInstancia().getUsuarioActivo().getRol();
        switch (rol) {
            case ADMIN -> {
                btnAgregar.setVisible(true); btnEditar.setVisible(true);
                btnDisponible.setVisible(true); btnEliminar.setVisible(true);
            }
            case RECEPCIONISTA -> {
                btnAgregar.setVisible(false); btnEditar.setVisible(false);
                btnDisponible.setVisible(false); btnEliminar.setVisible(false);
            }
            case MEDICO -> {
                btnAgregar.setVisible(false); btnEditar.setVisible(false);
                btnDisponible.setVisible(true); btnEliminar.setVisible(false);
            }
        }
        revalidate(); repaint();
    }

    public void refrescar() {
        modelo.setRowCount(0);
        for (Medico m : Sesion.getInstancia().getProxy().getMedicos())
            modelo.addRow(new Object[]{
                    m.getId(), m.getNombre(), m.getApellido(),
                    m.getEspecialidad().name(), m.isDisponible()
            });
    }

    private void buscar(String query) {
        modelo.setRowCount(0);
        List<Medico> lista = Sesion.getInstancia().getProxy().getMedicos();
        if (query.isEmpty()) { refrescar(); return; }
        for (Medico m : lista) {
            if (m.getNombre().toLowerCase().contains(query.toLowerCase()) ||
                    m.getApellido().toLowerCase().contains(query.toLowerCase()) ||
                    m.getEspecialidad().name().toLowerCase().contains(query.toLowerCase()))
                modelo.addRow(new Object[]{m.getId(), m.getNombre(), m.getApellido(),
                        m.getEspecialidad().name(), m.isDisponible()});
        }
    }

    private void dialogoAgregar() {
        if (Sesion.getInstancia().getUsuarioActivo().getRol() != RolUsuario.ADMIN) {
            JOptionPane.showMessageDialog(this, "Solo el ADMIN puede agregar medicos."); return;
        }
        JTextField fNombre   = new JTextField();
        JTextField fApellido = new JTextField();
        JComboBox<Especialidad> fEsp = new JComboBox<>(Especialidad.values());
        int result = JOptionPane.showConfirmDialog(this,
                new Object[]{"Nombre:", fNombre, "Apellido:", fApellido, "Especialidad:", fEsp},
                "Nuevo Medico", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int id = GestorHospital.getInstancia().siguienteIdMedico();
            Sesion.getInstancia().getProxy().registrarMedico(
                    id, fNombre.getText(), fApellido.getText(), (Especialidad) fEsp.getSelectedItem());
        }
    }

    private void editarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un medico."); return; }
        Medico m = Sesion.getInstancia().getProxy().getMedicos().get(fila);
        JTextField fNombre   = new JTextField(m.getNombre());
        JTextField fApellido = new JTextField(m.getApellido());
        JComboBox<Especialidad> fEsp = new JComboBox<>(Especialidad.values());
        fEsp.setSelectedItem(m.getEspecialidad());
        int result = JOptionPane.showConfirmDialog(this,
                new Object[]{"Nombre:", fNombre, "Apellido:", fApellido, "Especialidad:", fEsp},
                "Editar Medico", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Medico actualizado = new Medico(m.getId(), fNombre.getText(), fApellido.getText(),
                    (Especialidad) fEsp.getSelectedItem(), m.isDisponible());
            Sesion.getInstancia().getProxy().actualizarMedico(actualizado);
        }
    }

    private void cambiarDisponibilidad() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un medico."); return; }
        Medico m = Sesion.getInstancia().getProxy().getMedicos().get(fila);
        m.setDisponible(!m.isDisponible());
        Sesion.getInstancia().getProxy().actualizarMedico(m);
        JOptionPane.showMessageDialog(this,
                "Disponibilidad: " + (m.isDisponible() ? "Disponible" : "No disponible"));
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un medico."); return; }
        if (Sesion.getInstancia().getUsuarioActivo().getRol() != RolUsuario.ADMIN) {
            JOptionPane.showMessageDialog(this, "Solo el ADMIN puede eliminar medicos."); return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Eliminar este medico?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Medico m = Sesion.getInstancia().getProxy().getMedicos().get(fila);
            Sesion.getInstancia().getProxy().eliminarMedico(m.getId());
        }
    }
}