package com.juniverse.extractor.dto;


/**
 * The Class ArchiveEntry.
 */
public class ArchiveEntry {
    
    /** The name. */
    private String name;
    
    /** The size. */
    private long size;
    
    /** The compressed size. */
    private long compressedSize;
    
    /** The is directory. */
    private boolean isDirectory;
    
    /** The time. */
    private long time;
    
    /** The crc. */
    private int crc;

    /**
     * Instantiates a new archive entry.
     */
    public ArchiveEntry() {
        
    }
    
    /**
     * Instantiates a new archive entry.
     *
     * @param name the name
     * @param size the size
     * @param compressedSize the compressed size
     * @param isDirectory the is directory
     * @param time the time
     * @param crc the crc
     */
    public ArchiveEntry(String name, long size, long compressedSize, boolean isDirectory, long time, int crc) {
        this.name = name;
        this.size = size;
        this.compressedSize = compressedSize;
        this.isDirectory = isDirectory;
        this.time = time;
        this.crc = crc;
    }


    /**
     * Gets the crc.
     *
     * @return the crc
     */
    public int getCrc() {
        return crc;
    }

    /**
     * Sets the crc.
     *
     * @param crc the new crc
     */
    public void setCrc(Integer crc) {
        this.crc = crc;
    }

    /**
     * Gets the time.
     *
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets the time.
     *
     * @param time the new time
     */
    public void setTime(Long time) {
        this.time = time;
    }

    /**
     * Checks if is directory.
     *
     * @return true, if is directory
     */
    public boolean isDirectory() {
        return isDirectory;
    }

    /**
     * Sets the directory.
     *
     * @param directory the new directory
     */
    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    /**
     * Gets the compressed size.
     *
     * @return the compressed size
     */
    public long getCompressedSize() {
        return compressedSize;
    }

    /**
     * Sets the compressed size.
     *
     * @param compressedSize the new compressed size
     */
    public void setCompressedSize(long compressedSize) {
        this.compressedSize = compressedSize;
    }

    /**
     * Gets the size.
     *
     * @return the size
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the size.
     *
     * @param size the new size
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }
}
