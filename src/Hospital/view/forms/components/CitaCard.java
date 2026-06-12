package Hospital.view.forms.components;

import Hospital.model.Cita;

import javax.swing.*;
import java.awt.*;

public class CitaCard extends RoundPanel {

    public CitaCard(Cita cita) {
        super(12, new Color(45, 45, 60));
        setLayout(new BorderLayout(10, 5));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel lblPaciente = new JLabel(
                cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido()
        );
        lblPaciente.setForeground(Color.WHITE);
        lblPaciente.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel lblMedico = new JLabel(
                "Dr. " + cita.getMedico().getNombre() + " — " + cita.getFecha()
        );
        lblMedico.setForeground(new Color(180, 180, 200));
        lblMedico.setFont(new Font("Arial", Font.PLAIN, 12));

        EstadoBadge badge = new EstadoBadge(cita.getEstado().getNombre());

        JPanel izquierda = new JPanel();
        izquierda.setOpaque(false);
        izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));
        izquierda.add(lblPaciente);
        izquierda.add(lblMedico);

        add(izquierda, BorderLayout.CENTER);
        add(badge, BorderLayout.EAST);
    }
}