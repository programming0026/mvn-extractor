package com.juniverse.extractor.dto;

import java.util.List;

public class ArchiveInfo {
    
    /** Entradas. */
    private List<ArchiveEntry> entries;
    
    /** Nombre del archivo. */
    private String fileName;
    
    /** Ruta. */
    private String path;
    
    /** Comentario. */
    private String comment;
    

    public ArchiveInfo() {

    }

    public ArchiveInfo(List<ArchiveEntry> entries, String fileName, String path, String comment) {
        this.entries = entries;
        this.fileName = fileName;
        this.path = path;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

   
    public List<ArchiveEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<ArchiveEntry> entries) {
        this.entries = entries;
    }
}
