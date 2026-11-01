package com.juniverse.extractor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.juniverse.extractor.dto.ArchiveInfo;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Pruebas del servicio con un ZIP real creado en una carpeta temporal. */
class SevenZipExtractorServiceTest {

    @TempDir
    Path folder;

    private File zip() throws IOException {
        File file = folder.resolve("demo.zip").toFile();
        try (ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(file.toPath()))) {
            out.putNextEntry(new ZipEntry("hola.txt"));
            out.write("hola".getBytes(StandardCharsets.UTF_8));
            out.closeEntry();
            out.putNextEntry(new ZipEntry("carpeta/"));
            out.closeEntry();
            out.putNextEntry(new ZipEntry("carpeta/dato.txt"));
            out.write("dato".getBytes(StandardCharsets.UTF_8));
            out.closeEntry();
        }
        return file;
    }

    @Test
    void leeLasEntradasDeUnZip() throws IOException {
        ExtractorService service = new SevenZipExtractorService();
        ArchiveInfo info = service.readEntries(zip());
        assertEquals("demo.zip", info.getFileName());
        assertEquals(3, info.getEntries().size());
        assertTrue(info.getEntries().stream().anyMatch(e -> e.getName().endsWith("hola.txt")));
    }

    @Test
    void unZipSinCifrarNoPideContrasena() throws IOException {
        assertFalse(new SevenZipExtractorService().requiresPassword(zip()));
    }
}
