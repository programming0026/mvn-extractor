package com.juniverse.extractor.service;

import com.juniverse.extractor.dto.ArchiveEntry;
import com.juniverse.extractor.gui.ExtractorTableModel;
import com.juniverse.extractor.util.MessageUtil;
import com.juniverse.extractor.util.StringUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Java Universe
 */
public class TextReportService implements ReportService {

    /**
     * Genera el informe.
     *
     * @param outputFile archivo de salida
     * @param model modelo
     */
    @Override
    public void generateReport(File outputFile, ExtractorTableModel model) {
        Path out = outputFile.toPath();
        try (BufferedWriter w = Files.newBufferedWriter(out)) {
            w.write(String.format("Report %s (%d entries)%n%n", outputFile.getName(), model.getRowCount()));
            for (int i = 0; i < model.getRowCount(); i++) {
                ArchiveEntry entry = model.getEntry(i);
                w.write(String.format("%-60s %10s %10s %s%n",
                        entry.getName(),
                        StringUtil.toFormat(entry.getSize()),
                        StringUtil.toFormat(entry.getCompressedSize()),
                        entry.isDirectory() ? "DIR" : ""));
            }
            MessageUtil.showInfo("Report generated: " + out, "Report");
        } catch (IOException ex) {
            MessageUtil.showError("Report could not be generated", ex);
        }
    }
}
