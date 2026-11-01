package com.juniverse.extractor.service;

import com.juniverse.extractor.gui.ExtractorTableModel;
import java.io.File;

/**
 * Contrato de ReportService.
 */
public interface ReportService {

    /**
     * Genera el informe.
     *
     * @param outputFile archivo de salida
     * @param model modelo
     */
    void generateReport(File outputFile, ExtractorTableModel model);
}
