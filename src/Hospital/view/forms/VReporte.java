package Hospital.view.forms;

import Hospital.estructural.CitaConSeguro;
import Hospital.estructural.CitaConUrgencia;
import Hospital.model.Cita;
import Hospital.estructural.Sesion;
import Hospital.view.VMain;
import Hospital.view.forms.components.TextButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import Hospital.model.ICita;

public class VReporte extends JPanel {

    private VMain parent;
    private DefaultTableModel modelo;

    public VReporte(VMain parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 30));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        construir();
    }

    private void construir() {
        JLabel titulo = new JLabel("Reporte de Costos");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new String[]{"Paciente", "Médico", "Estado", "Costo base",
                        "Con urgencia", "Con seguro", "Final (urg+seg)"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabla = new JTable(modelo);
        tabla.setBackground(new Color(30, 30, 45));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(50, 50, 70));
        tabla.setRowHeight(30);
        tabla.getTableHeader().setBackground(new Color(45, 45, 60));
        tabla.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(new Color(30, 30, 45));
        add(scroll, BorderLayout.CENTER);

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sur.setOpaque(false);
        TextButton btnRefrescar = new TextButton("Refrescar", new Color(59, 130, 246));
        btnRefrescar.addActionListener(e -> refrescar());
        sur.add(btnRefrescar);
        add(sur, BorderLayout.SOUTH);

        refrescar();
    }

    public void refrescar() {
        modelo.setRowCount(0);
        List<Cita> lista = Sesion.getInstancia().getProxy().getCitas();
        for (ICita c : lista) {
            double base     = c.getCosto();
            double urgencia = new CitaConUrgencia(c).getCosto();
            double seguro   = new CitaConSeguro(c).getCosto();
            double ambos    = new CitaConSeguro(new CitaConUrgencia(c)).getCosto();

            modelo.addRow(new Object[]{
                    c.getPaciente().getNombre() + " " + c.getPaciente().getApellido(),
                    "Dr. " + c.getMedico().getNombre(),
                    c.getEstado().getNombre(),
                    "S/. " + base,
                    "S/. " + urgencia,
                    "S/. " + String.format("%.2f", seguro),
                    "S/. " + String.format("%.2f", ambos)
            });
        }
    }

}