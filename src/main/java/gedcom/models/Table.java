package gedcom.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Table<T> {

    private static int PADDING_SIZE = 1;
    private static String NEW_LINE = "\n";
    private static String TABLE_JOINT_SYMBOL = "+";
    private static String TABLE_V_SPLIT_SYMBOL = "|";
    private static String TABLE_H_SPLIT_SYMBOL = "-";

    private static String DATE_FORMAT = "dd MMM yyyy";
    private static String NULL_SYMBOL = "NA";

    private static final SimpleDateFormat dateFmt = new SimpleDateFormat(DATE_FORMAT);

    private String name;

    private List<String> columns;
    private List<List<Object>> rows;

    private List<Function<? super T, ? extends Object>> expanders;

    public Table(String name, List<String> columns, List<T> data, List<Function<? super T, ? extends Object>> expanders) {
        this.name = name;

        if (columns.size() != expanders.size()) {
            throw new IllegalArgumentException(String.format("Not enough expanders to expand into %d columns.", columns.size()));
        }

        this.columns = columns;
        this.expanders = expanders;

        expand(data);
    }

    private void expand(List<T> data) {
        rows = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            rows.add(new ArrayList<>());
            for (int j = 0; j < columns.size(); j++) {
                rows.get(i).add(expanders.get(j).apply(data.get(i)));
            }
        }
    }

    private String stringify(Object obj) {
        if (obj == null) {
            return NULL_SYMBOL;
        }
            
        if (obj instanceof Date) {
            return dateFmt.format(obj);
        }

        return obj.toString();
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

        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            columnMaxWidthMapping.put(columnIndex, 0);
        }

        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            if (columns.get(columnIndex).length() > columnMaxWidthMapping.get(columnIndex)) {
                columnMaxWidthMapping.put(columnIndex, columns.get(columnIndex).length());
            }
        }

        for (List<Object> row : rows) {
            for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
                if (stringify(row.get(columnIndex)).length() > columnMaxWidthMapping.get(columnIndex)) {
                    columnMaxWidthMapping.put(columnIndex, stringify(row.get(columnIndex)).length());
                }
            }
        }

        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            if (columnMaxWidthMapping.get(columnIndex) % 2 != 0) {
                columnMaxWidthMapping.put(columnIndex, columnMaxWidthMapping.get(columnIndex) + 1);
            }
        }

        return columnMaxWidthMapping;
    }

    private int getOptimumCellPadding(int cellIndex, int datalength, Map<Integer, Integer> columnMaxWidthMapping, int cellPaddingSize) {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Map<Integer, Integer> columnMaxWidthMapping = getMaximumWidhtofTable();

        sb.append(NEW_LINE);
        sb.append(name);
        sb.append(NEW_LINE);
        createRowLine(sb, columns.size(), columnMaxWidthMapping);
        sb.append(NEW_LINE);

        for (int headerIndex = 0; headerIndex < columns.size(); headerIndex++) {
            fillCell(sb, columns.get(headerIndex), headerIndex, columnMaxWidthMapping);
        }

        sb.append(NEW_LINE);

        createRowLine(sb, columns.size(), columnMaxWidthMapping);

        for (List<Object> row : rows) {
            sb.append(NEW_LINE);
            for (int cellIndex = 0; cellIndex < row.size(); cellIndex++) {
                fillCell(sb, stringify(row.get(cellIndex)), cellIndex, columnMaxWidthMapping);
            }
        }

        sb.append(NEW_LINE);
        createRowLine(sb, columns.size(), columnMaxWidthMapping);
        sb.append(NEW_LINE);

        return sb.toString();
    }

}
