package com.juniverse.extractor.service;

/**
 * Vista de progreso que usa el servicio al extraer: así el servicio no depende de una ventana concreta de Swing.
 */
public interface ProgressView {

    /** Muestra el porcentaje de avance (0 a 100). */
    void setProgress(int value);

    /** Muestra el mensaje actual, por ejemplo el archivo que se procesa. */
    void setMessage(String message);

    /** Abre (muestra) la vista. */
    void open();

    /** Cierra la vista. */
    void close();
}
