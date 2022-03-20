package hello.hyeoni.springproject.excel;

import hello.hyeoni.springproject.board.Board;
import hello.hyeoni.springproject.board.BoardRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("excel")
public class ExcelController {

    @Autowired private BoardRepository boardRepository;

    @GetMapping
    public String excelMain() {
        return "excel";
    }

    @GetMapping("/api/download")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        // 엑셀 파일 생성
        Workbook workbook = new SXSSFWorkbook();
        // 시트 생성
        Sheet sheet = workbook.createSheet();
        //
        List<Board> boardList = boardRepository.findAll();
        // 셀 스타일
        CellStyle greyCellStyle = workbook.createCellStyle();
        applyCellStyle(greyCellStyle, new XSSFColor(Color.pink));
        // 헤더 생성
        int rowIndex = 0;
        Row headerRow = sheet.createRow(rowIndex++);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("제목");
        headerCell1.setCellStyle(greyCellStyle);
        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("내용");
        headerCell2.setCellStyle(greyCellStyle);
        Cell headerCell3 = headerRow.createCell(2);
        headerCell3.setCellValue("아이디");
        headerCell3.setCellStyle(greyCellStyle);
        //바디에 데이터 삽입
        for(Board board : boardList) {
            Row bodyRow = sheet.createRow(rowIndex++);
            Cell bodyCell1 = bodyRow.createCell(0);
            bodyCell1.setCellStyle(greyCellStyle);
            Cell bodyCell2 = bodyRow.createCell(1);
            bodyCell2.setCellValue(board.getContent());
            Cell bodyCell3 = bodyRow.createCell(2);
            bodyCell3.setCellValue(board.getId());
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"test.xlsx\"");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private void applyCellStyle(CellStyle cellStyle, XSSFColor color) {
        XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;
        xssfCellStyle.setFillForegroundColor(color);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
    }
}
