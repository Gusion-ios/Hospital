package Hospital.view.forms;

import Hospital.estructural.Sesion;
import Hospital.view.VMain;
import Hospital.view.forms.components.RoundPanel;
import Hospital.view.forms.components.TextButton;

import javax.swing.*;
import java.awt.*;

public class VHome extends JPanel {

    private VMain parent;
    private JLabel lblPacientes;
    private JLabel lblCitas;
    private JLabel lblMedicos;

    public VHome(VMain parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 30));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        construir();
    }

    private void construir() {
        JLabel titulo = new JLabel("Panel Principal");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 3, 20, 0));
        cards.setOpaque(false);

        lblPacientes = new JLabel("0", SwingConstants.CENTER);
        lblCitas     = new JLabel("0", SwingConstants.CENTER);
        lblMedicos   = new JLabel("0", SwingConstants.CENTER);

        cards.add(crearCard("Pacientes",  lblPacientes, new Color(59, 130, 246)));
        cards.add(crearCard("Citas hoy",  lblCitas,     new Color(16, 185, 129)));
        cards.add(crearCard("Médicos",    lblMedicos,   new Color(139, 92, 246)));

        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.add(cards, BorderLayout.NORTH);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 20));
        acciones.setOpaque(false);

        TextButton btnPaciente = new TextButton("+ Nuevo Paciente", new Color(59, 130, 246));
        TextButton btnCita     = new TextButton("+ Nueva Cita",     new Color(16, 185, 129));

        btnPaciente.addActionListener(e -> parent.mostrar("PACIENTES"));
        btnCita.addActionListener(e -> parent.mostrar("CITAS"));

        acciones.add(btnPaciente);
        acciones.add(btnCita);
        centro.add(acciones, BorderLayout.CENTER);

        add(centro, BorderLayout.CENTER);
        refrescar();
    }

    private JPanel crearCard(String titulo, JLabel lblNumero, Color color) {
        RoundPanel card = new RoundPanel(16, new Color(30, 30, 45));
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(0, 120));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(new Color(160, 160, 180));
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 13));

        lblNumero.setForeground(color);
        lblNumero.setFont(new Font("Arial", Font.BOLD, 36));

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblNumero, BorderLayout.CENTER);
        return card;
    }

    public void refrescar() {
        Sesion s = Sesion.getInstancia();
        lblPacientes.setText(String.valueOf(s.getProxy().getPacientes().size()));
        lblCitas.setText(String.valueOf(s.getProxy().getCitas().size()));
        lblMedicos.setText(String.valueOf(s.getProxy().getMedicos().size()));
    }
}