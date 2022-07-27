package hello.hyeoni.springproject.account;

import hello.hyeoni.springproject.account.form.Profile;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static hello.hyeoni.springproject.account.SettingsController.ROOT;
import static hello.hyeoni.springproject.account.SettingsController.SETTINGS;

@Controller
@RequiredArgsConstructor
@RequestMapping(ROOT + SETTINGS)
public class SettingsController {

    static final String ROOT = "/";
    static final String SETTINGS = "settings";
    static final String PROFILE = "/profile";
    static final String PASSWORD = "/password";
    static final String NOTIFICATIONS = "/notifications";
    static final String ACCOUNT = "/account";
    static final String TAGS = "/tags";
    static final String ZONES = "/zones";

    private final ModelMapper modelMapper;
    private final AccountService accountService;

    @GetMapping(PROFILE)
    public String updateProfileForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Profile.class));
        return SETTINGS+PROFILE;
    }

    @PostMapping(PROFILE)
    public String updateProfile(@CurrentUser Account account, @Valid Profile profile, Errors errors
            , Model model, RedirectAttributes attributes) {
        if(errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS+PROFILE;
        }

        accountService.updateProfile(account,profile);
        attributes.addFlashAttribute("message","프로필을 수정했습니다.");
        return "redirect:/"+ SETTINGS + PROFILE;
    }
}
