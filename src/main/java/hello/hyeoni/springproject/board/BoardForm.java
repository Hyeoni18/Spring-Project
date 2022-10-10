package hello.hyeoni.springproject.board;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class BoardForm {

    @NotBlank
    @Length(max=50)
    private String title;

    private String fullDescription;

    private BoardType boardType = BoardType.TRAVEL;

}
