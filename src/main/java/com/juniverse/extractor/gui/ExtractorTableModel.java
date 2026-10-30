package com.juniverse.extractor.gui;

import com.juniverse.extractor.dto.ArchiveEntry;
import com.juniverse.extractor.util.DateUtil;
import com.juniverse.extractor.util.StringUtil;

import java.io.Serial;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;


/**
 * The Class ExtractorTableModel.
 *
 * @author ajuar
 */
public class ExtractorTableModel extends AbstractTableModel {

	/** The Constant serialVersionUID. */
	@Serial
    private static final long serialVersionUID = 1L;

	/** The cols. */
    private final String[] cols = {"Name", "Size", "Compressed", "Type", "Modified", "Crc"};
    
    /** The entries. */
    private transient List<ArchiveEntry> entries = List.of();

    /**
     * Sets the entries.
     *
     * @param list the new entries
     */
    public void setEntries(List<ArchiveEntry> list) {
        this.entries = list.stream()
                .sorted(Comparator.comparing(ArchiveEntry::getName))
                .toList();
        fireTableDataChanged();
    }

    /**
     * Gets the entry.
     *
     * @param row the row
     * @return the entry
     */
    public ArchiveEntry getEntry(int row) {
        return entries.get(row);
    }

    /**
     * Gets the row count.
     *
     * @return the row count
     */
    @Override
    public int getRowCount() {
        return entries.size();
    }

    /**
     * Gets the column count.
     *
     * @return the column count
     */
    @Override
    public int getColumnCount() {
        return cols.length;
    }

    /**
     * Gets the column name.
     *
     * @param col the col
     * @return the column name
     */
    @Override
    public String getColumnName(int col) {
        return cols[col];
    }

    /**
     * Gets the value at.
     *
     * @param row the row
     * @param col the col
     * @return the value at
     */
    @Override
    public Object getValueAt(int row, int col) {
        ArchiveEntry e = getEntry(row);
        return switch (col) {
            case 0 ->
                e.getName();
            case 1 ->
                StringUtil.toFormat(e.getSize());
            case 2 ->
                StringUtil.toFormat(e.getCompressedSize());
            case 3 ->
                e.isDirectory() ? "Directory" : "File";
            case 4 ->
                DateUtil.format(e.getTime());
            case 5 ->
                String.format("%08X", e.getCrc());
            default ->
                "";
        };
    }
}
