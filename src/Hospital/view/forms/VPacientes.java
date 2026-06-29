package Hospital.view.forms;

import Hospital.creacional.GestorHospital;
import Hospital.estructural.Sesion;
import Hospital.model.Paciente;
import Hospital.model.RolUsuario;
import Hospital.view.VMain;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class VPacientes extends JPanel {

    private VMain parent;
    private DefaultTableModel modelo;
    private JTable tabla;
    private JTextField searchField;

    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnClonar;
    private JButton btnEliminar;

    public VPacientes(VMain parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(VMain.FONDO);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        construir();
    }

    private void construir() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel headerLeft = new JPanel();
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));
        headerLeft.setOpaque(false);
        JLabel titulo = new JLabel("Gestion de Pacientes");
        titulo.setForeground(VMain.TEXTO_DARK);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel sub = new JLabel("Registro y administracion de pacientes");
        sub.setForeground(VMain.TEXTO_GRAY);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        headerLeft.add(titulo);
        headerLeft.add(Box.createVerticalStrut(3));
        headerLeft.add(sub);
        header.add(headerLeft, BorderLayout.WEST);

        // Search
        searchField = new JTextField(22);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.putClientProperty("JTextField.placeholderText", "Buscar por nombre, apellido o ID...");
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { buscar(searchField.getText().trim()); }
        });
        header.add(searchField, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Tabla
        modelo = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Apellido", "DNI", "Telefono"}, 0
        ) { public boolean isCellEditable(int r, int c) { return false; } };

        tabla = new JTable(modelo);
        estilizarTabla(tabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(VMain.BORDE, 1));
        scroll.getViewport().setBackground(VMain.BLANCO);
        add(scroll, BorderLayout.CENTER);

        // Botones
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 14));
        sur.setOpaque(false);

        btnAgregar  = crearBtn("+ Agregar",  VMain.AZUL_MEDIO);
        btnEditar   = crearBtn("Editar",     VMain.NARANJA);
        btnClonar   = crearBtn("Clonar",     VMain.MORADO);
        btnEliminar = crearBtn("Eliminar",   VMain.ROJO);

        btnAgregar.addActionListener(e -> dialogoAgregar());
        btnEditar.addActionListener(e -> editarSeleccionado());
        btnClonar.addActionListener(e -> clonarSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        sur.add(btnAgregar);
        sur.add(btnEditar);
        sur.add(btnClonar);
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
                btnClonar.setVisible(true);  btnEliminar.setVisible(true);
            }
            case RECEPCIONISTA -> {
                btnAgregar.setVisible(true); btnEditar.setVisible(true);
                btnClonar.setVisible(true);  btnEliminar.setVisible(false);
            }
            case MEDICO -> {
                btnAgregar.setVisible(false); btnEditar.setVisible(false);
                btnClonar.setVisible(false);  btnEliminar.setVisible(false);
            }
        }
        revalidate(); repaint();
    }

    public void refrescar() {
        modelo.setRowCount(0);
        for (Paciente p : Sesion.getInstancia().getProxy().getPacientes())
            modelo.addRow(new Object[]{p.getId(), p.getNombre(), p.getApellido(), p.getDni(), p.getTelefono()});
    }

    private void buscar(String query) {
        modelo.setRowCount(0);
        List<Paciente> lista = Sesion.getInstancia().getProxy().getPacientes();
        if (query.isEmpty()) { refrescar(); return; }
        for (Paciente p : lista) {
            if (query.matches("\\d+") && String.valueOf(p.getId()).contains(query) ||
                    p.getNombre().toLowerCase().contains(query.toLowerCase()) ||
                    p.getApellido().toLowerCase().contains(query.toLowerCase()))
                modelo.addRow(new Object[]{p.getId(), p.getNombre(), p.getApellido(), p.getDni(), p.getTelefono()});
        }
    }

    private void dialogoAgregar() {
        JTextField fNombre   = new JTextField();
        JTextField fApellido = new JTextField();
        JTextField fDni      = new JTextField();
        JTextField fTelefono = new JTextField();
        int result = JOptionPane.showConfirmDialog(this,
                new Object[]{"Nombre:", fNombre, "Apellido:", fApellido, "DNI:", fDni, "Telefono:", fTelefono},
                "Nuevo Paciente", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int id = GestorHospital.getInstancia().siguienteIdPaciente();
            Sesion.getInstancia().getProxy().registrarPaciente(
                    id, fNombre.getText(), fApellido.getText(), fDni.getText(), fTelefono.getText(), "");
        }
    }

    private void editarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un paciente."); return; }
        Paciente p = Sesion.getInstancia().getProxy().getPacientes().get(fila);
        JTextField fNombre   = new JTextField(p.getNombre());
        JTextField fApellido = new JTextField(p.getApellido());
        JTextField fDni      = new JTextField(p.getDni());
        JTextField fTelefono = new JTextField(p.getTelefono());
        int result = JOptionPane.showConfirmDialog(this,
                new Object[]{"Nombre:", fNombre, "Apellido:", fApellido, "DNI:", fDni, "Telefono:", fTelefono},
                "Editar Paciente", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            p.setTelefono(fTelefono.getText());
            Sesion.getInstancia().getProxy().actualizarPaciente(p);
        }
    }

    private void clonarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un paciente."); return; }
        Paciente original = Sesion.getInstancia().getProxy().getPacientes().get(fila);
        Sesion.getInstancia().getProxy().clonarPaciente(original);
        JOptionPane.showMessageDialog(this, "Paciente clonado correctamente.");
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un paciente."); return; }
        int confirm = JOptionPane.showConfirmDialog(this, "Eliminar este paciente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Paciente p = Sesion.getInstancia().getProxy().getPacientes().get(fila);
            Sesion.getInstancia().getProxy().eliminarPaciente(p.getId());
        }
    }
}