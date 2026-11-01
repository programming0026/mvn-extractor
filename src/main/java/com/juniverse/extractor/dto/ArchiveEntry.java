package com.juniverse.extractor.dto;


public class ArchiveEntry {
    
    /** Nombre. */
    private String name;
    
    /** Tamaño. */
    private long size;
    
    /** Tamaño comprimido. */
    private long compressedSize;
    
    private boolean isDirectory;
    
    /** Hora. */
    private long time;
    
    /** CRC. */
    private int crc;

    public ArchiveEntry() {
        
    }
    
    public ArchiveEntry(String name, long size, long compressedSize, boolean isDirectory, long time, int crc) {
        this.name = name;
        this.size = size;
        this.compressedSize = compressedSize;
        this.isDirectory = isDirectory;
        this.time = time;
        this.crc = crc;
    }


    public int getCrc() {
        return crc;
    }

    public void setCrc(Integer crc) {
        this.crc = crc;
    }

    public long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public void setCompressedSize(long compressedSize) {
        this.compressedSize = compressedSize;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
