package hello.hyeoni.springproject.travel;

import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.account.CurrentUser;
import hello.hyeoni.springproject.travel.form.TravelForm;
import hello.hyeoni.springproject.travel.validator.TravelFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;
    private final ModelMapper modelMapper;
    private final TravelFormValidator travelFormValidator;

    @InitBinder("travelForm")
    public void travelFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(travelFormValidator);
    }

    @GetMapping("/new-travel")
    public String newTravelForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new TravelForm()); // Travel 화면에 필요한 정보
        return "travel/form";
    }

    @PostMapping("new-travel")
    public String newTravelSubmit(@CurrentUser Account account, @Valid TravelForm travelForm, Errors errors) {
        if (errors.hasErrors()) {
            return "travel/form";
        }

        Travel newTravel = travelService.createNewTravel(modelMapper.map(travelForm, Travel.class), account);
        return "redirect:/travel/"+ URLEncoder.encode(travelForm.getPath(), StandardCharsets.UTF_8);
    }
}
