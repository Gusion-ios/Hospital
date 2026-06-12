package Hospital.view.forms;

import Hospital.estructural.Sesion;
import Hospital.model.Cita;
import Hospital.model.Medico;
import Hospital.model.Paciente;
import Hospital.view.VMain;
import Hospital.view.forms.components.TextButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VCitas extends JPanel {

    private VMain parent;
    private DefaultTableModel modelo;
    private JTable tabla;

    public VCitas(VMain parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 30));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        construir();
    }

    private void construir() {
        JLabel titulo = new JLabel("Gestión de Citas");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new String[]{"ID", "Paciente", "Médico", "Fecha", "Motivo", "Estado", "Costo"}, 0
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

        TextButton btnAgregar   = new TextButton("+ Nueva Cita",  new Color(59, 130, 246));
        TextButton btnConfirmar = new TextButton("Confirmar",     new Color(16, 185, 129));
        TextButton btnCompletar = new TextButton("Completar",     new Color(23, 162, 184));
        TextButton btnCancelar  = new TextButton("Cancelar",      new Color(220, 53, 69));
        TextButton btnDeshacer  = new TextButton("↩ Deshacer",    new Color(139, 92, 246));

        btnAgregar.addActionListener(e -> dialogoAgregar());
        btnConfirmar.addActionListener(e -> confirmarSeleccionada());
        btnCompletar.addActionListener(e -> completarSeleccionada());
        btnCancelar.addActionListener(e -> cancelarSeleccionada());
        btnDeshacer.addActionListener(e -> {
            Sesion.getInstancia().getProxy().deshacerUltimaAccion();
            refrescar();
        });

        sur.add(btnAgregar);
        sur.add(btnConfirmar);
        sur.add(btnCompletar);
        sur.add(btnCancelar);
        sur.add(btnDeshacer);
        add(sur, BorderLayout.SOUTH);

        refrescar();
    }

    public void refrescar() {
        modelo.setRowCount(0);
        List<Cita> lista = Sesion.getInstancia().getProxy().getCitas();
        for (Cita c : lista) {
            modelo.addRow(new Object[]{
                    c.getId(),
                    c.getPaciente().getNombre() + " " + c.getPaciente().getApellido(),
                    "Dr. " + c.getMedico().getNombre(),
                    c.getFecha(),
                    c.getMotivo(),
                    c.getEstado().getNombre(),
                    "S/. " + c.getCosto()
            });
        }
    }

    private void dialogoAgregar() {
        List<Paciente> pacientes = Sesion.getInstancia().getProxy().getPacientes();
        List<Medico>   medicos   = Sesion.getInstancia().getProxy().getMedicos();

        if (pacientes.isEmpty() || medicos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Necesitas pacientes y médicos registrados.");
            return;
        }

        JTextField fId      = new JTextField();
        JTextField fFecha   = new JTextField("2025-01-01");
        JTextField fMotivo  = new JTextField();
        JTextField fCosto   = new JTextField("50.0");
        JCheckBox  fUrgente = new JCheckBox("Urgente");

        JComboBox<Paciente> cbPaciente = new JComboBox<>(pacientes.toArray(new Paciente[0]));
        JComboBox<Medico>   cbMedico   = new JComboBox<>(medicos.toArray(new Medico[0]));

        cbPaciente.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel l = new JLabel(value.getNombre() + " " + value.getApellido());
            if (isSelected) l.setBackground(list.getSelectionBackground());
            return l;
        });
        cbMedico.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel l = new JLabel("Dr. " + value.getNombre() + " " + value.getApellido());
            if (isSelected) l.setBackground(list.getSelectionBackground());
            return l;
        });

        Object[] campos = {
                "ID:", fId, "Paciente:", cbPaciente,
                "Médico:", cbMedico, "Fecha:", fFecha,
                "Motivo:", fMotivo, "Costo:", fCosto, fUrgente
        };

        int result = JOptionPane.showConfirmDialog(
                this, campos, "Nueva Cita", JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                Sesion.getInstancia().getProxy().agendarCita(
                        Integer.parseInt(fId.getText()),
                        (Paciente) cbPaciente.getSelectedItem(),
                        (Medico)   cbMedico.getSelectedItem(),
                        fFecha.getText(), fMotivo.getText(),
                        fUrgente.isSelected(),
                        Double.parseDouble(fCosto.getText())
                );
                refrescar();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID y costo deben ser números.");
            }
        }
    }

    private void confirmarSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona una cita."); return; }
        Cita cita = Sesion.getInstancia().getProxy().getCitas().get(fila);
        Sesion.getInstancia().getProxy().confirmarCita(cita);
        refrescar();
    }

    private void completarSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona una cita."); return; }
        Cita cita = Sesion.getInstancia().getProxy().getCitas().get(fila);
        Sesion.getInstancia().getProxy().completarCita(cita);
        refrescar();
    }

    private void cancelarSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona una cita."); return; }
        Cita cita = Sesion.getInstancia().getProxy().getCitas().get(fila);
        Sesion.getInstancia().getProxy().cancelarCita(cita);
        refrescar();
    }
}