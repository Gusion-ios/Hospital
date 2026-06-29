package Hospital.view.forms;

import Hospital.estructural.CitaConSeguro;
import Hospital.estructural.CitaConUrgencia;
import Hospital.estructural.Sesion;
import Hospital.model.Cita;
import Hospital.model.ICita;
import Hospital.view.VMain;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class VReporte extends JPanel {

    private VMain parent;
    private DefaultTableModel modelo;

    public VReporte(VMain parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(VMain.FONDO);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        construir();
    }

    private void construir() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titulo = new JLabel("Reporte de Costos");
        titulo.setForeground(VMain.TEXTO_DARK);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel sub = new JLabel("Comparativa de costos con decoradores de urgencia y seguro");
        sub.setForeground(VMain.TEXTO_GRAY);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        header.add(titulo);
        header.add(Box.createVerticalStrut(3));
        header.add(sub);
        add(header, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new String[]{"Paciente", "Medico", "Estado", "Costo base",
                        "Con urgencia", "Con seguro", "Final (urg+seg)"}, 0
        ) { public boolean isCellEditable(int r, int c) { return false; } };

        JTable tabla = new JTable(modelo);
        tabla.setBackground(VMain.BLANCO);
        tabla.setForeground(VMain.TEXTO_DARK);
        tabla.setGridColor(VMain.BORDE);
        tabla.setRowHeight(36);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setSelectionBackground(VMain.AZUL_CLARO);
        tabla.setSelectionForeground(VMain.AZUL_OSCURO);
        tabla.setShowVerticalLines(false);
        tabla.getTableHeader().setBackground(VMain.FONDO);
        tabla.getTableHeader().setForeground(VMain.TEXTO_GRAY);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, VMain.BORDE));
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                        t, value, isSelected, hasFocus, row, col);
                if (!isSelected)
                    c.setBackground(row % 2 == 0 ? VMain.BLANCO : new Color(248, 250, 252));
                if (col == 6 && !isSelected) {
                    c.setForeground(VMain.AZUL_MEDIO);
                    ((JLabel)c).setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else if (!isSelected) {
                    c.setForeground(VMain.TEXTO_DARK);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(VMain.BORDE, 1));
        scroll.getViewport().setBackground(VMain.BLANCO);
        add(scroll, BorderLayout.CENTER);

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 14));
        sur.setOpaque(false);
        JButton btnRefrescar = new JButton("Actualizar reporte");
        btnRefrescar.setBackground(VMain.AZUL_MEDIO);
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setBorderPainted(false);
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefrescar.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btnRefrescar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefrescar.addActionListener(e -> refrescar());
        sur.add(btnRefrescar);
        add(sur, BorderLayout.SOUTH);

        refrescar();
    }

    public void refrescar() {
        modelo.setRowCount(0);
        List<Cita> lista = Sesion.getInstancia().getProxy().getCitas();
        for (Cita c : lista) {
            double base    = c.getCosto();
            double urgente = new CitaConUrgencia(c).getCosto();
            double seguro  = new CitaConSeguro(c).getCosto();
            double ambos   = new CitaConSeguro(new CitaConUrgencia(c)).getCosto();
            modelo.addRow(new Object[]{
                    c.getPaciente().getNombre() + " " + c.getPaciente().getApellido(),
                    "Dr. " + c.getMedico().getNombre(),
                    c.getEstado().getNombre(),
                    "S/. " + String.format("%.2f", base),
                    "S/. " + String.format("%.2f", urgente),
                    "S/. " + String.format("%.2f", seguro),
                    "S/. " + String.format("%.2f", ambos)
            });
        }
    }
}