package com.juniverse.extractor;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.juniverse.extractor.gui.ExtractorFrame;
import javax.swing.SwingUtilities;


/**
 * @author ajuar
 */
public class Main {

    public static void main(String[] args) {
        FlatMacDarkLaf.setup();
        SwingUtilities.invokeLater(() -> new ExtractorFrame().setVisible(true));
    }
}
