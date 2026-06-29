package Hospital.view.forms;

import Hospital.creacional.GestorHospital;
import Hospital.estructural.Sesion;
import Hospital.model.Cita;
import Hospital.model.Medico;
import Hospital.model.Paciente;
import Hospital.model.RolUsuario;
import Hospital.view.VMain;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class VCitas extends JPanel {

    private VMain parent;
    private DefaultTableModel modelo;
    private JTable tabla;

    private JButton btnAgregar;
    private JButton btnConfirmar;
    private JButton btnCompletar;
    private JButton btnEditar;
    private JButton btnCancelar;
    private JButton btnDeshacer;

    public VCitas(VMain parent) {
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
        JLabel titulo = new JLabel("Gestion de Citas");
        titulo.setForeground(VMain.TEXTO_DARK);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel sub = new JLabel("Agenda y seguimiento de citas medicas");
        sub.setForeground(VMain.TEXTO_GRAY);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        headerLeft.add(titulo);
        headerLeft.add(Box.createVerticalStrut(3));
        headerLeft.add(sub);
        header.add(headerLeft, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new String[]{"ID", "Paciente", "Medico", "Fecha", "Motivo", "Estado", "Costo"}, 0
        ) { public boolean isCellEditable(int r, int c) { return false; } };

        tabla = new JTable(modelo);
        estilizarTabla(tabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(VMain.BORDE, 1));
        scroll.getViewport().setBackground(VMain.BLANCO);
        add(scroll, BorderLayout.CENTER);

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 14));
        sur.setOpaque(false);

        btnAgregar   = crearBtn("+ Nueva Cita", VMain.AZUL_MEDIO);
        btnConfirmar = crearBtn("Confirmar",    VMain.VERDE);
        btnCompletar = crearBtn("Completar",    new Color(8, 145, 178));
        btnEditar    = crearBtn("Editar",       VMain.NARANJA);
        btnCancelar  = crearBtn("Cancelar",     VMain.ROJO);
        btnDeshacer  = crearBtn("Deshacer",     VMain.MORADO);

        btnAgregar.addActionListener(e -> dialogoAgregar());
        btnConfirmar.addActionListener(e -> confirmarSeleccionada());
        btnCompletar.addActionListener(e -> completarSeleccionada());
        btnEditar.addActionListener(e -> editarSeleccionada());
        btnCancelar.addActionListener(e -> cancelarSeleccionada());
        btnDeshacer.addActionListener(e -> {
            Sesion.getInstancia().getProxy().deshacerUltimaAccion();
            refrescar();
        });

        sur.add(btnAgregar);
        sur.add(btnConfirmar);
        sur.add(btnCompletar);
        sur.add(btnEditar);
        sur.add(btnCancelar);
        sur.add(btnDeshacer);
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
                String estado = String.valueOf(table.getValueAt(row, 5));
                if (!isSelected) {
                    switch (estado) {
                        case "CONFIRMADA"  -> c.setBackground(new Color(240, 253, 244));
                        case "PENDIENTE"   -> c.setBackground(new Color(254, 252, 232));
                        case "CANCELADA"   -> c.setBackground(new Color(255, 241, 242));
                        case "COMPLETADA"  -> c.setBackground(new Color(239, 246, 255));
                        default            -> c.setBackground(row % 2 == 0 ?
                                VMain.BLANCO : new Color(248, 250, 252));
                    }
                }
                if (col == 5 && !isSelected) {
                    switch (estado) {
                        case "CONFIRMADA" -> c.setForeground(new Color(22, 101, 52));
                        case "PENDIENTE"  -> c.setForeground(new Color(133, 77, 14));
                        case "CANCELADA"  -> c.setForeground(new Color(153, 27, 27));
                        case "COMPLETADA" -> c.setForeground(new Color(30, 64, 175));
                        default           -> c.setForeground(VMain.TEXTO_DARK);
                    }
                    ((JLabel)c).setFont(new Font("Segoe UI", Font.BOLD, 12));
                } else if (!isSelected) {
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
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    public void aplicarPermisos() {
        RolUsuario rol = Sesion.getInstancia().getUsuarioActivo().getRol();
        switch (rol) {
            case ADMIN -> {
                btnAgregar.setVisible(true); btnConfirmar.setVisible(true);
                btnCompletar.setVisible(true); btnEditar.setVisible(true);
                btnCancelar.setVisible(true); btnDeshacer.setVisible(true);
            }
            case RECEPCIONISTA -> {
                btnAgregar.setVisible(true); btnConfirmar.setVisible(false);
                btnCompletar.setVisible(false); btnEditar.setVisible(true);
                btnCancelar.setVisible(true); btnDeshacer.setVisible(false);
            }
            case MEDICO -> {
                btnAgregar.setVisible(false); btnConfirmar.setVisible(true);
                btnCompletar.setVisible(true); btnEditar.setVisible(false);
                btnCancelar.setVisible(false); btnDeshacer.setVisible(false);
            }
        }
        revalidate(); repaint();
    }

    public void refrescar() {
        modelo.setRowCount(0);
        for (Cita c : Sesion.getInstancia().getProxy().getCitas())
            modelo.addRow(new Object[]{
                    c.getId(),
                    c.getPaciente().getNombre() + " " + c.getPaciente().getApellido(),
                    "Dr. " + c.getMedico().getNombre(),
                    c.getFecha(), c.getMotivo(),
                    c.getEstado().getNombre(),
                    "S/. " + String.format("%.2f", c.getCosto())
            });
    }

    private void dialogoAgregar() {
        List<Paciente> pacientes = Sesion.getInstancia().getProxy().getPacientes();
        List<Medico>   medicos   = Sesion.getInstancia().getProxy().getMedicos();
        if (pacientes.isEmpty() || medicos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Necesitas pacientes y medicos registrados."); return;
        }
        JTextField fFecha   = new JTextField("2025-01-01");
        JTextField fMotivo  = new JTextField();
        JTextField fCosto   = new JTextField("50.0");
        JCheckBox  fUrgente = new JCheckBox("Urgente");
        JComboBox<Paciente> cbPaciente = new JComboBox<>(pacientes.toArray(new Paciente[0]));
        JComboBox<Medico>   cbMedico   = new JComboBox<>(medicos.toArray(new Medico[0]));
        cbPaciente.setRenderer((list, value, index, isSelected, cf) -> {
            JLabel l = new JLabel(value.getNombre() + " " + value.getApellido());
            if (isSelected) l.setBackground(list.getSelectionBackground());
            return l;
        });
        cbMedico.setRenderer((list, value, index, isSelected, cf) -> {
            JLabel l = new JLabel("Dr. " + value.getNombre() + " " + value.getApellido());
            if (isSelected) l.setBackground(list.getSelectionBackground());
            return l;
        });
        int result = JOptionPane.showConfirmDialog(this,
                new Object[]{"Paciente:", cbPaciente, "Medico:", cbMedico,
                        "Fecha:", fFecha, "Motivo:", fMotivo, "Costo:", fCosto, fUrgente},
                "Nueva Cita", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = GestorHospital.getInstancia().siguienteIdCita();
                Sesion.getInstancia().getProxy().agendarCita(id,
                        (Paciente) cbPaciente.getSelectedItem(),
                        (Medico) cbMedico.getSelectedItem(),
                        fFecha.getText(), fMotivo.getText(),
                        fUrgente.isSelected(), Double.parseDouble(fCosto.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El costo debe ser un numero.");
            }
        }
    }

    private void confirmarSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona una cita."); return; }
        Sesion.getInstancia().getProxy().confirmarCita(
                Sesion.getInstancia().getProxy().getCitas().get(fila));
        refrescar();
    }

    private void completarSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona una cita."); return; }
        Sesion.getInstancia().getProxy().completarCita(
                Sesion.getInstancia().getProxy().getCitas().get(fila));
        refrescar();
    }

    private void editarSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona una cita."); return; }
        Cita cita = Sesion.getInstancia().getProxy().getCitas().get(fila);
        JTextField fFecha  = new JTextField(cita.getFecha());
        JTextField fMotivo = new JTextField(cita.getMotivo());
        JTextField fCosto  = new JTextField(String.valueOf(cita.getCosto()));
        int result = JOptionPane.showConfirmDialog(this,
                new Object[]{"Fecha:", fFecha, "Motivo:", fMotivo, "Costo:", fCosto},
                "Editar Cita", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                cita.setFecha(fFecha.getText());
                cita.setMotivo(fMotivo.getText());
                cita.setCosto(Double.parseDouble(fCosto.getText()));
                Sesion.getInstancia().getProxy().actualizarCita(cita);
                refrescar();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El costo debe ser un numero.");
            }
        }
    }

    private void cancelarSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona una cita."); return; }
        Sesion.getInstancia().getProxy().cancelarCita(
                Sesion.getInstancia().getProxy().getCitas().get(fila));
        refrescar();
    }
}