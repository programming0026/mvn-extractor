package com.juniverse.extractor.dto;

import java.util.List;

/**
 * The Class ArchiveInfo.
 */
public class ArchiveInfo {
    
    /** The entries. */
    private List<ArchiveEntry> entries;
    
    /** The file name. */
    private String fileName;
    
    /** The path. */
    private String path;
    
    /** The comment. */
    private String comment;
    

    /**
     * Instantiates a new archive info.
     */
    public ArchiveInfo() {

    }

    /**
     * Instantiates a new archive info.
     *
     * @param entries the entries
     * @param fileName the file name
     * @param path the path
     * @param comment the comment
     */
    public ArchiveInfo(List<ArchiveEntry> entries, String fileName, String path, String comment) {
        this.entries = entries;
        this.fileName = fileName;
        this.path = path;
        this.comment = comment;
    }

    /**
     * Gets the comment.
     *
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment.
     *
     * @param comment the new comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path.
     *
     * @param path the new path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets the file name.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name.
     *
     * @param fileName the new file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

   
    /**
     * Gets the entries.
     *
     * @return the entries
     */
    public List<ArchiveEntry> getEntries() {
        return entries;
    }

    /**
     * Sets the entries.
     *
     * @param entries the new entries
     */
    public void setEntries(List<ArchiveEntry> entries) {
        this.entries = entries;
    }
}
