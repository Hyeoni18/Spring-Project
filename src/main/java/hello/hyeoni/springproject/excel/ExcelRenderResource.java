package hello.hyeoni.springproject.excel;

import java.util.List;
import java.util.Map;

public class ExcelRenderResource {

    private Map<String, String> excelHeaderNames;
    private List<String> dataFieldNames;

    public ExcelRenderResource(Map<String, String> excelHeaderNames, List<String> dataFieldNames) {
        this.excelHeaderNames = excelHeaderNames;
        this.dataFieldNames = dataFieldNames;
    }

    public String getExcelHeaderName(String dataFieldName) {
        return excelHeaderNames.get(dataFieldName);
    }

    public List<String> getDataFieldNames() {
        return dataFieldNames;
    }
}
