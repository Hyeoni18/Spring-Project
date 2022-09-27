package hello.hyeoni.springproject.travel.validator;

import hello.hyeoni.springproject.travel.Travel;
import hello.hyeoni.springproject.travel.TravelRepository;
import hello.hyeoni.springproject.travel.form.TravelForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class TravelFormValidator implements Validator {

    private final TravelRepository travelRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return TravelForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TravelForm travelForm = (TravelForm) target;
        if (travelRepository.existsByPath(travelForm.getPath())) {
            errors.rejectValue("path", "wrong.path", "해당 경로 값을 사용할 수 없습니다.");
        }
    }
}
