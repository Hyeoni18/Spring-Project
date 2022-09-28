package hello.hyeoni.springproject.main;

import hello.hyeoni.springproject.account.CurrentUser;
import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.travel.Travel;
import hello.hyeoni.springproject.travel.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final TravelRepository travelRepository;

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        if(account != null) {
            model.addAttribute(account);
        }
        Page<Travel> travelList = travelRepository.findByPublished(pageable);
        model.addAttribute("travelList", travelList);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/search/travel")
    public String searchTravel(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, String keyword, Model model) {
        Page<Travel> travelList = travelRepository.findByKeyword(keyword, pageable);
        model.addAttribute("travelList", travelList);
        model.addAttribute("keyword", keyword);
        return "search";
    }
}
