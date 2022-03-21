package hello.hyeoni.springproject.board;

import hello.hyeoni.springproject.excel.ExcelHeader;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardDto {

    @ExcelHeader(headerName = "title")
    private String title;

    @ExcelHeader(headerName = "content")
    private String content;
}
