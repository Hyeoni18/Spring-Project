package hello.hyeoni.springproject.board;

import hello.hyeoni.springproject.excel.ExcelHeader;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class LectureDto {

    @ExcelHeader(headerName = "subject")
    private String subject;

    @ExcelHeader(headerName = "price")
    private Integer price;

    @ExcelHeader(headerName = "opening")
    private LocalDateTime opening;

    @ExcelHeader(headerName = "instructor")
    private String instructor;
}
