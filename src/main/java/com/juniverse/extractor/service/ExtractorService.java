package com.juniverse.extractor.service;

import com.juniverse.extractor.dto.ArchiveEntry;
import com.juniverse.extractor.dto.ArchiveInfo;
import com.juniverse.extractor.exception.ExtractorException;
import com.juniverse.extractor.gui.ProgressDialog;
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
 * The Class ExtractorService.
 *
 * @author ajuar
 */
public class ExtractorService {

    /** The Constant log. */
    private static final Logger log
            = Logger.getLogger(ExtractorService.class.getName());

    /**
     * Read entries.
     *
     * @param inputFile the input file
     * @return the archive info
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public ArchiveInfo readEntries(File inputFile) throws IOException {
        return readEntries(inputFile, null);
    }

    /**
     * Read entries.
     *
     * @param inputFile the input file
     * @param password the password
     * @return the archive info
     * @throws IOException Signals that an I/O exception has occurred.
     */
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

    /**
     * Requires password.
     *
     * @param inputFile the input file
     * @return true, if successful
     */
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
     * Extract archive.
     *
     * @param inputFile the input file
     * @param outputFile the output file
     * @param progressBar the progress bar
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void extractArchive(File inputFile, File outputFile, ProgressDialog progressBar)
            throws IOException {
        extractArchive(inputFile, outputFile, null, progressBar);
    }

    /**
     * Extract archive.
     *
     * @param inputFile the input file
     * @param outputFile the output file
     * @param password the password
     * @param progressBar the progress bar
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void extractArchive(File inputFile, File outputFile, String password, ProgressDialog progressBar)
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

                    Path pathDestino = outputFile.toPath();
                    Files.createDirectories(pathDestino);

                    log.log(Level.INFO, "Format: {0}", inArchive.getArchiveFormat());
                    log.log(Level.INFO, "Items: {0}", inArchive.getNumberOfItems());

                    ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();

                    int total = inArchive.getNumberOfItems();
                    int contador = 0;

                    for (ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
                        contador++;
                        int progress = (contador * 100 / total);
                        publish(new Object[]{progress, item.getPath()});
                        if (!item.isFolder()) {
                            extractItem(item, pathDestino, password);
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
                progressBar.dispose();
                if (success) {
                    log.log(Level.INFO, "Extraction completed: {0}", inputFile.getName());
                    MessageUtil.showInfo("Extracción finalizada.", "Success");
                }

            }
        };
        worker.execute();
        progressBar.setVisible(true);
    }

    /**
     * Extrae un elemento individual del archivo.
     *
     * @param item The item
     * @param baseDirectory The directory
     * @param password The password
     * @throws IOException Signals that an I/O exception has occurred.
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
