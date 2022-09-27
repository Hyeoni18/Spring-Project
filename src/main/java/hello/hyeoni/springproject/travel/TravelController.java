package hello.hyeoni.springproject.travel;

import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.account.CurrentUser;
import hello.hyeoni.springproject.travel.form.TravelForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TravelController {

    @GetMapping("/new-travel")
    public String newTravelForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new TravelForm()); // Travel 화면에 보여줄 정보
        return "travel/form";
    }
}
