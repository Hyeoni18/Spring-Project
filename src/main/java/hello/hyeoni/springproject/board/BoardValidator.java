package hello.hyeoni.springproject.board;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BoardValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return BoardForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BoardForm boardForm = (BoardForm) target;
        if(boardForm.getTitle() == null) {
            errors.rejectValue("title", "wrong.value", "제목을 입력해주세요.");
        }
        if(boardForm.getContent() == null) {
            errors.rejectValue("content", "wrong.value", "내용을 입력해주세요.");
        }
    }
}
