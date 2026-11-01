package com.juniverse.extractor.service;

import com.juniverse.extractor.dto.ArchiveInfo;
import java.io.File;
import java.io.IOException;

/**
 * Contrato de ExtractorService.
 */
public interface ExtractorService {

    /**
     * Lee las entradas del archivo.
     *
     * @param inputFile archivo de entrada
     * @return información del archivo comprimido
     * @throws IOException si ocurre un error de entrada/salida
     */
    ArchiveInfo readEntries(File inputFile) throws IOException;

    /**
     * Lee las entradas del archivo.
     *
     * @param inputFile archivo de entrada
     * @param password contraseña
     * @return información del archivo comprimido
     * @throws IOException si ocurre un error de entrada/salida
     */
    ArchiveInfo readEntries(File inputFile, String password) throws IOException;

    boolean requiresPassword(File inputFile);

    /**
     * Extrae el archivo comprimido.
     *
     * @param inputFile archivo de entrada
     * @param outputFile archivo de salida
     * @param progressBar barra de progreso
     * @throws IOException si ocurre un error de entrada/salida
     */
    void extractArchive(File inputFile, File outputFile, ProgressView progressBar) throws IOException;

    /**
     * Extrae el archivo comprimido.
     *
     * @param inputFile archivo de entrada
     * @param outputFile archivo de salida
     * @param password contraseña
     * @param progressBar barra de progreso
     * @throws IOException si ocurre un error de entrada/salida
     */
    void extractArchive(File inputFile, File outputFile, String password, ProgressView progressBar) throws IOException;
}
