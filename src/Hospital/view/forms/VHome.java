package Hospital.view.forms;

import Hospital.estructural.Sesion;
import Hospital.view.VMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VHome extends JPanel {

    private VMain parent;
    private JLabel lblPacientes;
    private JLabel lblCitas;
    private JLabel lblMedicos;

    public VHome(VMain parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(VMain.FONDO);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        construir();
    }

    private void construir() {
        // Header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 22, 0));

        JLabel titulo = new JLabel("Panel Principal");
        titulo.setForeground(VMain.TEXTO_DARK);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("Resumen general del sistema hospitalario");
        subtitulo.setForeground(VMain.TEXTO_GRAY);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        header.add(titulo);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitulo);
        add(header, BorderLayout.NORTH);

        // Panel central con BoxLayout vertical
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);

        // Stats cards
        JPanel cards = new JPanel(new GridLayout(1, 3, 18, 0));
        cards.setOpaque(false);
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        lblPacientes = new JLabel("0");
        lblCitas     = new JLabel("0");
        lblMedicos   = new JLabel("0");

        cards.add(crearStatCard("Total Pacientes", lblPacientes,
                new Color(59, 130, 246), "Ir a Pacientes", () -> parent.mostrar("PACIENTES")));
        cards.add(crearStatCard("Citas Activas", lblCitas,
                new Color(16, 185, 129), "Ir a Citas", () -> parent.mostrar("CITAS")));
        cards.add(crearStatCard("Medicos Activos", lblMedicos,
                new Color(124, 58, 237), "Ir a Medicos", () -> parent.mostrar("MEDICOS")));

        centro.add(cards);
        centro.add(Box.createVerticalStrut(28));

        // Titulo acciones
        JLabel quickLabel = new JLabel("Acciones rapidas");
        quickLabel.setForeground(VMain.TEXTO_DARK);
        quickLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        quickLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(quickLabel);
        centro.add(Box.createVerticalStrut(12));

        // Quick cards
        JPanel quickPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        quickPanel.setOpaque(false);
        quickPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        quickPanel.add(crearQuickCard("Nuevo Paciente",
                "Registrar en el sistema", () -> parent.mostrar("PACIENTES")));
        quickPanel.add(crearQuickCard("Nueva Cita",
                "Agendar consulta medica", () -> parent.mostrar("CITAS")));
        quickPanel.add(crearQuickCard("Ver Reportes",
                "Costos y estadisticas", () -> parent.mostrar("REPORTE")));

        centro.add(quickPanel);

        add(centro, BorderLayout.CENTER);
        refrescar();
    }

    private JPanel crearStatCard(String titulo, JLabel lblNum,
                                 Color color, String linkText, Runnable navegar) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(VMain.BLANCO);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VMain.BORDE, 1),
                BorderFactory.createEmptyBorder(18, 20, 14, 20)
        ));

        // Barra de color superior
        JPanel colorBar = new JPanel();
        colorBar.setBackground(color);
        colorBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 4));
        colorBar.setPreferredSize(new Dimension(0, 4));

        JLabel lTitulo = new JLabel(titulo);
        lTitulo.setForeground(VMain.TEXTO_GRAY);
        lTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        lblNum.setForeground(color);
        lblNum.setFont(new Font("Segoe UI", Font.BOLD, 38));

        JLabel lLink = new JLabel("→ " + linkText);
        lLink.setForeground(VMain.AZUL_MEDIO);
        lLink.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { navegar.run(); }
            public void mouseEntered(MouseEvent e) {
                lLink.setText("<html><u>→ " + linkText + "</u></html>");
            }
            public void mouseExited(MouseEvent e) {
                lLink.setText("→ " + linkText);
            }
        });

        card.add(lTitulo);
        card.add(Box.createVerticalStrut(8));
        card.add(lblNum);
        card.add(Box.createVerticalStrut(8));
        card.add(lLink);

        // Wrapper con barra de color
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(VMain.BLANCO);
        wrapper.setBorder(BorderFactory.createLineBorder(VMain.BORDE, 1));
        wrapper.add(colorBar, BorderLayout.NORTH);
        wrapper.add(card, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel crearQuickCard(String titulo, String sub, Runnable accion) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(VMain.BLANCO);
        card.setPreferredSize(new Dimension(200, 80));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VMain.BORDE, 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel t = new JLabel(titulo);
        t.setForeground(VMain.TEXTO_DARK);
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel s = new JLabel(sub);
        s.setForeground(VMain.TEXTO_GRAY);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        card.add(t);
        card.add(Box.createVerticalStrut(4));
        card.add(s);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(VMain.AZUL_CLARO);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(VMain.AZUL_MEDIO, 1),
                        BorderFactory.createEmptyBorder(16, 16, 16, 16)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(VMain.BLANCO);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(VMain.BORDE, 1),
                        BorderFactory.createEmptyBorder(16, 16, 16, 16)
                ));
            }
            public void mouseClicked(MouseEvent e) { accion.run(); }
        });

        return card;
    }

    public void refrescar() {
        Sesion s = Sesion.getInstancia();
        lblPacientes.setText(String.valueOf(s.getProxy().getPacientes().size()));
        lblCitas.setText(String.valueOf(s.getProxy().getCitas().size()));
        lblMedicos.setText(String.valueOf(s.getProxy().getMedicos().size()));
    }
}