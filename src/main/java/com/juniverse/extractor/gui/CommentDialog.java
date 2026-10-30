package com.juniverse.extractor.gui;

import com.juniverse.extractor.util.MessageUtil;
import com.juniverse.extractor.util.StringUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.io.Serial;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

/**
 * The Class CommentDialog.
 *
 * @author ajuar
 */
public class CommentDialog extends JDialog {

    /** The Constant serialVersionUID. */
    @Serial
    private static final long serialVersionUID = 1L;
	
	/** The comment text area. */
	private JTextArea commentTextArea;

    /**
     * Instantiates a new comment dialog.
     *
     * @param parent the parent
     * @param fileName the file name
     * @param comment the comment
     */
    public CommentDialog(JFrame parent, String fileName, String comment) {
        super(parent, "Comment", true);
        initComponents(parent, fileName, comment);
    }

    /**
     * Initialize the components.
     *
     * @param parent the parent
     * @param fileName the file name
     * @param comment the comment
     */
    private void initComponents(JFrame parent, String fileName, String comment) {
        setLayout(new BorderLayout(10, 10));
        setResizable(false);
        
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        JLabel titleLabel = new JLabel("File: " + fileName);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        commentTextArea = new JTextArea();
        commentTextArea.setText(StringUtil.hasText(comment) ? comment : "(No comment)");
        commentTextArea.setEditable(false);
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        commentTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        commentTextArea.setCaretPosition(0);
        
        JScrollPane scrollPane = new JScrollPane(commentTextArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
        
        JButton copyButton = new JButton("Copy");
        copyButton.setMnemonic(KeyEvent.VK_C);
        copyButton.addActionListener(e -> copyToClipboard());
        copyButton.setEnabled(StringUtil.hasText(comment));
        bottomPanel.add(copyButton);
        
        JButton  closeButton = new JButton("Close");
        closeButton.setMnemonic(KeyEvent.VK_ENTER);
        closeButton.addActionListener(e -> dispose());
        bottomPanel.add(closeButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);
        
        getRootPane().registerKeyboardAction(
            e -> dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        getRootPane().setDefaultButton(closeButton);
    }

    /**
     * Copy to clipboard.
     */
    private void copyToClipboard() {
        commentTextArea.selectAll();
        commentTextArea.copy();
        commentTextArea.setCaretPosition(0);
        MessageUtil.showInfo("Comment copied to clipboard", "Copied");
    }
}
