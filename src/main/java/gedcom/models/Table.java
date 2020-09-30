package gedcom.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {

    private List<String> headersList;
    private List<List<String>> rowsList;

    private int PADDING_SIZE = 1;
    private String NEW_LINE = "\n";
    private String TABLE_JOINT_SYMBOL = "+";
    private String TABLE_V_SPLIT_SYMBOL = "|";
    private String TABLE_H_SPLIT_SYMBOL = "-";

    public Table(List<String> headers, List<List<String>> rows) {
        // TODO: check dimensions
        this.headersList = headers;
        this.rowsList = rows;
    }

    private void fillSpace(StringBuilder sb, int length) {
        for (int i = 0; i < length; i++) {
            sb.append(" ");
        }
    }

    private void createRowLine(StringBuilder sb, int headersListSize, Map<Integer, Integer> columnMaxWidthMapping) {
        for (int i = 0; i < headersListSize; i++) {
            if (i == 0) {
                sb.append(TABLE_JOINT_SYMBOL);
            }

            for (int j = 0; j < columnMaxWidthMapping.get(i) + PADDING_SIZE * 2; j++) {
                sb.append(TABLE_H_SPLIT_SYMBOL);
            }
            sb.append(TABLE_JOINT_SYMBOL);
        }
    }

    private Map<Integer, Integer> getMaximumWidhtofTable() {
        Map<Integer, Integer> columnMaxWidthMapping = new HashMap<>();

        for (int columnIndex = 0; columnIndex < headersList.size(); columnIndex++) {
            columnMaxWidthMapping.put(columnIndex, 0);
        }

        for (int columnIndex = 0; columnIndex < headersList.size(); columnIndex++) {
            if (headersList.get(columnIndex).length() > columnMaxWidthMapping.get(columnIndex)) {
                columnMaxWidthMapping.put(columnIndex, headersList.get(columnIndex).length());
            }
        }

        for (List<String> row : rowsList) {
            for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
                if (row.get(columnIndex).length() > columnMaxWidthMapping.get(columnIndex)) {
                    columnMaxWidthMapping.put(columnIndex, row.get(columnIndex).length());
                }
            }
        }

        for (int columnIndex = 0; columnIndex < headersList.size(); columnIndex++) {
            if (columnMaxWidthMapping.get(columnIndex) % 2 != 0) {
                columnMaxWidthMapping.put(columnIndex, columnMaxWidthMapping.get(columnIndex) + 1);
            }
        }

        return columnMaxWidthMapping;
    }

    private int getOptimumCellPadding(int cellIndex, int datalength, Map<Integer, Integer> columnMaxWidthMapping,
            int cellPaddingSize) {
        if (datalength % 2 != 0) {
            datalength++;
        }

        if (datalength < columnMaxWidthMapping.get(cellIndex)) {
            cellPaddingSize = cellPaddingSize + (columnMaxWidthMapping.get(cellIndex) - datalength) / 2;
        }

        return cellPaddingSize;
    }

    private void fillCell(StringBuilder sb, String cell, int cellIndex, Map<Integer, Integer> columnMaxWidthMapping) {

        int cellPaddingSize = getOptimumCellPadding(cellIndex, cell.length(), columnMaxWidthMapping, PADDING_SIZE);

        if (cellIndex == 0) {
            sb.append(TABLE_V_SPLIT_SYMBOL);
        }

        fillSpace(sb, cellPaddingSize);
        sb.append(cell);
        if (cell.length() % 2 != 0) {
            sb.append(" ");
        }

        fillSpace(sb, cellPaddingSize);

        sb.append(TABLE_V_SPLIT_SYMBOL);

    }

    public Table orderBy(String col) {
        // TODO
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Map<Integer, Integer> columnMaxWidthMapping = getMaximumWidhtofTable();

        sb.append(NEW_LINE);
        sb.append(NEW_LINE);
        createRowLine(sb, headersList.size(), columnMaxWidthMapping);
        sb.append(NEW_LINE);

        for (int headerIndex = 0; headerIndex < headersList.size(); headerIndex++) {
            fillCell(sb, headersList.get(headerIndex), headerIndex, columnMaxWidthMapping);
        }

        sb.append(NEW_LINE);

        createRowLine(sb, headersList.size(), columnMaxWidthMapping);

        for (List<String> row : rowsList) {
            sb.append(NEW_LINE);
            for (int cellIndex = 0; cellIndex < row.size(); cellIndex++) {
                fillCell(sb, row.get(cellIndex), cellIndex, columnMaxWidthMapping);
            }
        }

        sb.append(NEW_LINE);
        createRowLine(sb, headersList.size(), columnMaxWidthMapping);
        sb.append(NEW_LINE);
        sb.append(NEW_LINE);

        return sb.toString();
    }

}
