package com.juniverse.extractor.gui;

import com.juniverse.extractor.dto.ArchiveEntry;
import com.juniverse.extractor.util.DateUtil;
import com.juniverse.extractor.util.StringUtil;

import java.io.Serial;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;


/**
 * @author Java Universe
 */
public class ExtractorTableModel extends AbstractTableModel {

	@Serial
    private static final long serialVersionUID = 1L;

	/** Columnas. */
    private final String[] cols = {"Name", "Size", "Compressed", "Type", "Modified", "Crc"};
    
    /** Entradas. */
    private transient List<ArchiveEntry> entries = List.of();

    public void setEntries(List<ArchiveEntry> list) {
        this.entries = list.stream()
                .sorted(Comparator.comparing(ArchiveEntry::getName))
                .toList();
        fireTableDataChanged();
    }

    public ArchiveEntry getEntry(int row) {
        return entries.get(row);
    }

    @Override
    public int getRowCount() {
        return entries.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public String getColumnName(int col) {
        return cols[col];
    }

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
