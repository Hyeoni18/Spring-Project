package hello.hyeoni.springproject.main;

import hello.hyeoni.springproject.account.CurrentUser;
import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.travel.Travel;
import hello.hyeoni.springproject.travel.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final TravelRepository travelRepository;

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {
        if(account != null) {
            model.addAttribute(account);
        }
        List<Travel> travelList = travelRepository.findAll();
        model.addAttribute(travelList);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/search/travel")
    public String searchTravel(String keyword, Model model) {
        List<Travel> travelList = travelRepository.findByKeyword(keyword);
        model.addAttribute(travelList);
        model.addAttribute("keyword", keyword);
        return "search";
    }
}
