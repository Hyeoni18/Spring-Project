package hello.hyeoni.springproject.board;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class BoardForm {

    @Length(max = 100)
    @NotBlank
    private String title;

    @NotBlank
    private String content;

}
