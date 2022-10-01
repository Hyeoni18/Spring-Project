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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;
    private final TravelRepository travelRepository;
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

    @PostMapping("/new-travel")
    public String newTravelSubmit(@CurrentUser Account account, @Valid TravelForm travelForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return "travel/form";
        }

        Travel newTravel = travelService.createNewTravel(modelMapper.map(travelForm, Travel.class), account);
        return "redirect:/travel/"+ URLEncoder.encode(travelForm.getPath(), StandardCharsets.UTF_8);
    }

    @GetMapping("/travel/{path}")
    public String viewTravel(@CurrentUser Account account, @PathVariable String path, Model model) {
        Travel travel = travelService.getTravel(path);
        model.addAttribute(account);
        model.addAttribute(travel);
        return "travel/view";
    }

    @GetMapping("/travel/{path}/members")
    public String viewTravelMembers(@CurrentUser Account account, @PathVariable String path, Model model) {
        Travel travel = travelService.getTravel(path);
        model.addAttribute(account);
        model.addAttribute(travel);
        return "travel/members";
    }

    @PostMapping("/travel/{path}/remove")
    public String removeTravel(@CurrentUser Account account, @PathVariable String path) {
        Travel travel = travelService.getTravel(path);
        travelService.remove(travel);
        return "redirect:/";
    }

}
