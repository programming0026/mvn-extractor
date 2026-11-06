package com.juniverse.extractor.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.Serial;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 * @author Java Universe
 */
public class PasswordDialog extends JDialog {

   
	@Serial
    private static final long serialVersionUID = 1L;
	
	/** Campo de contraseña. */
	private JPasswordField passwordField;
    
    /** Indicador de aceptación. */
    private boolean accepted = false;

    public PasswordDialog(JFrame parent, String title) {
        super(parent, title, true);
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(350, 170);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        JPanel centralPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        centralPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblMessage = new JLabel("Enter the password:");
        passwordField = new JPasswordField(20);

        centralPanel.add(lblMessage);
        centralPanel.add(passwordField);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnAccept = new JButton("Accept");
        JButton btnCancel = new JButton("Cancel");

        btnAccept.addActionListener(e -> {
            accepted = true;
            dispose();
        });

        btnCancel.addActionListener(e -> {
            accepted = false;
            dispose();
        });

        passwordField.addActionListener(e -> {
            accepted = true;
            dispose();
        });

        buttonsPanel.add(btnAccept);
        buttonsPanel.add(btnCancel);

        add(centralPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                passwordField.requestFocusInWindow();
            }
        });
    }

    public String getPassword() {
        if (isAccepted()) {
            return new String(passwordField.getPassword());
        }
        return null;
    }

    public boolean isAccepted() {
        return accepted;
    }
}
