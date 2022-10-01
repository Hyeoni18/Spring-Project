package hello.hyeoni.springproject.travel;

import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.account.CurrentUser;
import hello.hyeoni.springproject.travel.form.TravelDescriptionForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
@RequestMapping("/travel/{path}/settings")
public class TravelSettingsController {

    private final TravelService travelService;
    private final ModelMapper modelMapper;

    @GetMapping
    public String viewTravelSetting(@CurrentUser Account account, @PathVariable String path, Model model) {
        Travel travel = travelService.getTravelToUpdate(account, path);
        model.addAttribute(account);
        model.addAttribute(travel);
        model.addAttribute(modelMapper.map(travel, TravelDescriptionForm.class));
        return "travel/settings/description";
    }

    @PostMapping
    public String updateTravelInfo(@CurrentUser Account account, @PathVariable String path, @Valid TravelDescriptionForm travelDescriptionForm, Errors errors, RedirectAttributes redirectAttributes, Model model) {
        Travel travel = travelService.getTravelToUpdate(account, path);

        if (errors.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(travel);
            return "travel/settings/description";
        }

        travelService.updateTravelDescription(travel, travelDescriptionForm);
        redirectAttributes.addFlashAttribute("message", "모집 소개를 수정했습니다.");
        return "redirect:/travel/"+getPath(path)+"/settings/description";
    }

    private String getPath(String path) {
        return URLEncoder.encode(path, StandardCharsets.UTF_8);
    }


}
