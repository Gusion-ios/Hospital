package Hospital.view;

import Hospital.comportamiento.CitaObserver;
import Hospital.estructural.Sesion;
import Hospital.model.Cita;
import Hospital.view.forms.*;

import javax.swing.*;
import java.awt.*;

public class VMain extends JFrame implements CitaObserver {

    private JPanel contenedor;
    private CardLayout cardLayout;

    private VHome vHome;
    private VPacientes vPacientes;
    private VMedicos vMedicos;
    private VCitas vCitas;
    private VReporte vReporte;
    private VConfi vConfi;

    public VMain() {
        setTitle("Hospital - Sistema de Gestión");
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        contenedor = new JPanel(cardLayout);

        vHome = new VHome(this);
        vPacientes = new VPacientes(this);
        vMedicos = new VMedicos(this);
        vCitas = new VCitas(this);
        vReporte = new VReporte(this);
        vConfi = new VConfi(this);

        contenedor.add(vHome, "HOME");
        contenedor.add(vPacientes, "PACIENTES");
        contenedor.add(vMedicos, "MEDICOS");
        contenedor.add(vCitas, "CITAS");
        contenedor.add(vReporte, "REPORTE");
        contenedor.add(vConfi, "CONFI");

        JPanel sidebar = crearSidebar();
        add(sidebar, BorderLayout.WEST);
        add(contenedor, BorderLayout.CENTER);

        mostrar("HOME");
    }

    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(180, 0));
        sidebar.setBackground(new Color(30, 30, 40));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel titulo = new JLabel("Hospital");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        sidebar.add(titulo);

        sidebar.add(botonNav("🏠 Inicio", "HOME"));
        sidebar.add(botonNav("👤 Pacientes", "PACIENTES"));
        sidebar.add(botonNav("👨‍⚕️ Médicos", "MEDICOS"));
        sidebar.add(botonNav("📅 Citas", "CITAS"));
        sidebar.add(botonNav("📊 Reportes", "REPORTE"));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(botonNav("⚙️ Configuración", "CONFI"));

        return sidebar;
    }

    private JButton botonNav(String texto, String panel) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(160, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(45, 45, 60));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> mostrar(panel));
        return btn;
    }

    public void mostrar(String panel) {
        cardLayout.show(contenedor, panel);
    }

    @Override
    public void actualizar(Cita cita) {
        vHome.refrescar();
        vCitas.refrescar();
    }

    public Sesion getSesion() {
        return Sesion.getInstancia();
    }
}