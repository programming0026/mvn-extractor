package com.juniverse.extractor.gui;

import com.juniverse.extractor.dto.ArchiveInfo;
import com.juniverse.extractor.service.ExtractorService;
import com.juniverse.extractor.service.ReportService;
import com.juniverse.extractor.util.IconUtil;
import com.juniverse.extractor.util.MessageUtil;
import com.juniverse.extractor.util.StringUtil;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.stream.Stream;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The Class ExtractorFrame.
 *
 * @author ajuar
 */
public class ExtractorFrame extends JFrame {

    /** The Constant serialVersionUID. */
    @Serial
    private static final long serialVersionUID = 1L;
	
	/** The model. */
	private final ExtractorTableModel model = new ExtractorTableModel();
    
    /** The status. */
    private final JLabel status = new JLabel("Ready");
    
    /** The input file. */
    private File inputFile;
    
    /** The password. */
    private String password;
    
    /** The current file. */
    private transient ExtractorService currentFile;
    
    /** The info. */
    private transient ArchiveInfo info;
    
    /** The extract btn. */
    private JButton extractBtn;
    
    /** The comment btn. */
    private JButton commentBtn;
    
    /** The report btn. */
    private JButton reportBtn;
    
    /**
     * Instantiates a new extractor frame.
     */
    public ExtractorFrame() {
        super("Free Extractor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(900, 600);
        setLocationRelativeTo(null);
        ImageIcon icon = IconUtil.loadIcon("/icons/main.png", 36, 36);
        if (icon != null) {
            setIconImage(icon.getImage());
        }
        JTable table = new JTable();
        table.setModel(model);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createToolBar(), BorderLayout.NORTH);
        add(status, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the tool bar.
     *
     * @return the jtoolbar
     */
    private JToolBar createToolBar() {
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        
        extractBtn = makeButton("Extract in", 'E', e -> extractSelected());
        commentBtn = makeButton("See comment", 'M', e -> showComment());
        reportBtn = makeButton("Generate report", 'R', e -> generateReport());
        JButton openBtn = makeButton("Open", 'O', e -> open());
        JButton aboutBtn = makeButton("About", 'A', e -> about());
        openBtn.setIcon(IconUtil.loadIcon("/icons/open.png", 32, 32));
        extractBtn.setIcon(IconUtil.loadIcon("/icons/extract.png", 32, 32));
        reportBtn.setIcon(IconUtil.loadIcon("/icons/report.png", 32, 32));
        aboutBtn.setIcon(IconUtil.loadIcon("/icons/about.png", 32, 32));
        commentBtn.setIcon(IconUtil.loadIcon("/icons/comment.png", 32, 32));
        bar.add(openBtn);
        bar.add(extractBtn);
        bar.add(commentBtn);
        bar.add(reportBtn);
        bar.addSeparator();
        bar.add(aboutBtn);
        
        Stream.of(extractBtn, commentBtn, reportBtn).forEach(b -> b.setEnabled(false));
        return bar;
    }
    
    /**
     * Extract selected.
     */
    private void extractSelected() {
        if (currentFile == null || info == null) {
            return;
        }
        
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        try {
            ProgressDialog progressBar = new ProgressDialog(this, "Progress");
            currentFile.extractArchive(inputFile, fc.getSelectedFile(), password, progressBar);
        } catch (IOException ex) {
            MessageUtil.showError("Error extracting file: " + info.getFileName(), ex);
        }
    }
    
    /**
     * Make button.
     *
     * @param text the text
     * @param mnemonic the mnemonic
     * @param al the al
     * @return the j button
     */
    private JButton makeButton(String text, int mnemonic, java.awt.event.ActionListener al) {
        JButton b = new JButton(text);
        if (mnemonic != 0) {
            b.setMnemonic(mnemonic);
        }
        
        b.addActionListener(al);
        return b;
    }
    
    /**
     * Open.
     */
    private void open() {
        
        try {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("Files (ZIP, RAR, 7Z, TAR)", "zip", "rar", "7z", "tar", "gz", "bz2"));
            if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            inputFile = fc.getSelectedFile();
            currentFile = new ExtractorService();
            password = null;
            if (currentFile.requiresPassword(inputFile)) {
                PasswordDialog dialog = new PasswordDialog(this, "Password required");
                dialog.setVisible(true);
                password = dialog.getPassword();
                if (!dialog.isAccepted()) {
                    MessageUtil.showInfo("You need to specify the password to extract", "Password required");
                    return;
                }
            }
            info = currentFile.readEntries(inputFile, password);
            model.setEntries(info.getEntries());
            setTitle("Free Extractor - " + info.getFileName());
            status.setText(" " + info.getPath());
            Stream.of(extractBtn, commentBtn, reportBtn).forEach(b -> b.setEnabled(true));
        } catch (Exception ex) {
            MessageUtil.showError("The file could not be opened", ex);
        }
    }
    
    /**
     * Generate report.
     */
    private void generateReport() {
        if (currentFile == null || info == null) {
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("report_" + StringUtil.getName(info.getFileName()) + ".txt"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        ReportService reportService = new ReportService();
        reportService.generateReport(fc.getSelectedFile(), model);
        
    }
    
    /**
     * Show comment.
     */
    private void showComment() {
        if (currentFile == null || info == null) {
            return;
        }
        String comment = info.getComment();
        CommentDialog commentDialog = new CommentDialog(this, info.getFileName(), comment);
        commentDialog.setVisible(true);
    }
    
    /**
     * About.
     */
    private void about() {
        AboutDialog aboutDialog = new AboutDialog(this);
        aboutDialog.setVisible(true);
    }
    
}
