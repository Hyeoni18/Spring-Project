package hello.hyeoni.springproject.excel;

import java.util.List;

public class OneSheetExcelFile<T> extends SXSSFExcelFile<T>{

    private static final int ROW_START_INDEX = 0;
    private static final int COLUMN_START_INDEX = 0;
    private int currentRowIndex = ROW_START_INDEX;

    public OneSheetExcelFile(List<T> data, Class<T> type) {
        super(data, type);
    }

    @Override
    protected void renderExcel(List<T> data) {
        // 1. Create sheet and renderHeader
        sheet = wb.createSheet();
        renderHeadersWithNewSheet(sheet, currentRowIndex++, COLUMN_START_INDEX);

        if (data.isEmpty()) {
            return;
        }

        // 2. Render Body
        for (Object renderedData : data) {
            renderBody(renderedData, currentRowIndex++, COLUMN_START_INDEX);
        }
    }

    @Override
    public void addRows(List<T> data) {
        renderBody(data, currentRowIndex++, COLUMN_START_INDEX);
    }
}
