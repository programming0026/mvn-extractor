package com.juniverse.extractor.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.Serial;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import com.juniverse.extractor.service.ProgressView;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * @author ajuar
 */
public class ProgressDialog extends JDialog implements ProgressView {

    @Serial
    private static final long serialVersionUID = 1L;
	
	/** Barra de progreso. */
	private JProgressBar progressBar;
    
    /** Etiqueta del mensaje. */
    private JLabel lblMessage;

    public ProgressDialog(JFrame parent, String title) {
        super(parent, title, true);
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblMessage = new JLabel("Processing...");
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(350, 25));

        panelCentral.add(lblMessage);
        panelCentral.add(Box.createVerticalStrut(15));
        panelCentral.add(progressBar);

        add(panelCentral, BorderLayout.CENTER);
    }

    @Override
    public void setProgress(int value) {
        progressBar.setValue(value);
    }

    @Override
    public void setMessage(String message) {
        lblMessage.setText(message);
    }

    public void setIndeterminate(boolean indeterminate) {
        progressBar.setIndeterminate(indeterminate);
    }

    @Override
    public void open() {
        setVisible(true);
    }

    @Override
    public void close() {
        dispose();
    }
}
