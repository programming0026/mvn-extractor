package com.juniverse.extractor.service;

import com.juniverse.extractor.dto.ArchiveEntry;
import com.juniverse.extractor.dto.ArchiveInfo;
import com.juniverse.extractor.exception.ExtractorException;
import com.juniverse.extractor.util.MessageUtil;
import com.juniverse.extractor.util.StringUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;

/**
 * @author ajuar
 */
public class SevenZipExtractorService implements ExtractorService {

    private static final Logger log
            = Logger.getLogger(ExtractorService.class.getName());

    /**
     * Lee las entradas del archivo.
     *
     * @param inputFile archivo de entrada
     * @return información del archivo comprimido
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    public ArchiveInfo readEntries(File inputFile) throws IOException {
        return readEntries(inputFile, null);
    }

    /**
     * Lee las entradas del archivo.
     *
     * @param inputFile archivo de entrada
     * @param password contraseña
     * @return información del archivo comprimido
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    public ArchiveInfo readEntries(File inputFile, String password) throws IOException {

        ArchiveInfo entry = new ArchiveInfo();
        List<ArchiveEntry> result = new ArrayList<>();
        if (!inputFile.exists()) {
            throw new FileNotFoundException("File not found: " + inputFile.getName());
        }

        entry.setFileName(inputFile.getName());
        entry.setPath(inputFile.getAbsolutePath());

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(inputFile, "r"); IInArchive archive = StringUtil.hasText(password)
                ? SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile), password)
                : SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile))) {

            int items = archive.getNumberOfItems();

            Object comment = archive.getArchiveProperty(PropID.COMMENT);
            entry.setComment(comment != null ? comment.toString() : "");
            
            for (int i = 0; i < items; i++) {

                ArchiveEntry info = new ArchiveEntry();

                info.setName((String) archive.getProperty(i, PropID.PATH));

                Long size = (Long) archive.getProperty(i, PropID.SIZE);
                info.setSize(size != null ? size : 0L);

                Long packed = (Long) archive.getProperty(i, PropID.PACKED_SIZE);
                info.setCompressedSize(packed != null ? packed : 0L);

                Boolean folder = (Boolean) archive.getProperty(i, PropID.IS_FOLDER);
                info.setDirectory(Boolean.TRUE.equals(folder));

                Integer crc = (Integer) archive.getProperty(i, PropID.CRC);
                info.setCrc(crc != null ? crc : 0);

                Date date = (Date) archive.getProperty(i, PropID.LAST_MODIFICATION_TIME);
                long time = date != null ? date.getTime() : 0L;
                info.setTime(time);

                result.add(info);
            }
        }
        entry.setEntries(result);
        return entry;
    }

    @Override
    public boolean requiresPassword(File inputFile) {

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(inputFile, "r"); IInArchive inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile))) {

            if (inArchive.getNumberOfItems() > 0) {
                Boolean isEncrypted = (Boolean) inArchive.getProperty(0, PropID.ENCRYPTED);
                return isEncrypted != null && isEncrypted;
            }

            return false;

        } catch (Exception e) {
            return e.getMessage() != null
                    && (e.getMessage().contains("password")
                    || e.getMessage().contains("encrypted"));
        }
    }

    /**
     * Extrae el archivo comprimido.
     *
     * @param inputFile archivo de entrada
     * @param outputFile archivo de salida
     * @param progressBar barra de progreso
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    public void extractArchive(File inputFile, File outputFile, ProgressView progressBar)
            throws IOException {
        extractArchive(inputFile, outputFile, null, progressBar);
    }

    /**
     * Extrae el archivo comprimido.
     *
     * @param inputFile archivo de entrada
     * @param outputFile archivo de salida
     * @param password contraseña
     * @param progressBar barra de progreso
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    public void extractArchive(File inputFile, File outputFile, String password, ProgressView progressBar)
            throws IOException {

        if (!inputFile.exists()) {
            throw new FileNotFoundException("File not found: " + inputFile.getName());
        }

        var worker = new SwingWorker<Void, Object[]>() {
            boolean success = false;

            @Override
            protected Void doInBackground() {
                
                try (RandomAccessFile randomAccessFile = new RandomAccessFile(inputFile, "r"); IInArchive inArchive = StringUtil.hasText(password)
                        ? SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile), password)
                        : SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile))) {

                    Path pathDestination = outputFile.toPath();
                    Files.createDirectories(pathDestination);

                    log.log(Level.INFO, "Format: {0}", inArchive.getArchiveFormat());
                    log.log(Level.INFO, "Items: {0}", inArchive.getNumberOfItems());

                    ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();

                    int total = inArchive.getNumberOfItems();
                    int counter = 0;

                    for (ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
                        counter++;
                        int progress = (counter * 100 / total);
                        publish(new Object[]{progress, item.getPath()});
                        if (!item.isFolder()) {
                            extractItem(item, pathDestination, password);
                        }
                    }
                    success = true;
                } catch (Exception ex) {
                    MessageUtil.showError("Error decompressing", ex);
                    success = false;
                }
                return null;
            }

            @Override
            protected void process(List<Object[]> chunks) {
                Object[] data = chunks.getLast();
                progressBar.setProgress((int) data[0]);
                progressBar.setMessage("Processing: " + data[1]);
            }

            @Override
            protected void done() {
                progressBar.close();
                if (success) {
                    log.log(Level.INFO, "Extraction completed: {0}", inputFile.getName());
                    MessageUtil.showInfo("Extracción finalizada.", "Success");
                }

            }
        };
        worker.execute();
        progressBar.open();
    }

    /**
     * Extrae un elemento individual del archivo.
     *
     * @param item elemento
     * @param baseDirectory directorio
     * @param password contraseña
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void extractItem(ISimpleInArchiveItem item, Path baseDirectory, String password)
            throws IOException {

        String relativePath = item.getPath();
        // SEGURIDAD (Zip Slip): un archivo malicioso puede traer rutas como "../../x" o absolutas.
        // Se normaliza la ruta y se rechaza si queda fuera de la carpeta de destino.
        Path base = baseDirectory.toAbsolutePath().normalize();
        Path outputPath = base.resolve(relativePath).normalize();
        if (!outputPath.startsWith(base)) {
            throw new ExtractorException("Unsafe path in archive (path traversal): " + relativePath);
        }

        Files.createDirectories(outputPath.getParent());

        ExtractOperationResult result;
        // BUG: antes se abría (y truncaba) el archivo en cada bloque de datos, así que los archivos grandes
        // quedaban con solo el último bloque, y los vacíos no se creaban. Ahora se abre una sola vez.
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(outputPath))) {
            ISequentialOutStream sink = data -> {
                try {
                    out.write(data);
                } catch (IOException e) {
                    throw new ExtractorException("Error writing file: " + outputPath, e);
                }
                return data.length;
            };
            result = StringUtil.hasText(password) ? item.extractSlow(sink, password) : item.extractSlow(sink);
        }

        if (result != ExtractOperationResult.OK) {
            throw new ExtractorException("Error extracting: " + relativePath + " - " + result);
        }
    }

}
