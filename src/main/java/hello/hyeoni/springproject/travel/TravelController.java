package hello.hyeoni.springproject.travel;

import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.account.CurrentUser;
import hello.hyeoni.springproject.board.Board;
import hello.hyeoni.springproject.board.BoardRepository;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;
    private final TravelRepository travelRepository;
    private final ModelMapper modelMapper;
    private final TravelFormValidator travelFormValidator;
    private final BoardRepository boardRepository;

    @InitBinder("travelForm")
    public void travelFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(travelFormValidator);
    }

    @GetMapping("/new-travel")
    public String newTravelForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new TravelForm());
        return "travel/form";
    }

    @PostMapping("/new-travel")
    public String newTravelSubmit(@CurrentUser Account account, @Valid TravelForm travelForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return "travel/form";
        }

        Travel newTravel = travelService.createNewTravel(modelMapper.map(travelForm, Travel.class), account);
        return "redirect:/travel/"+ newTravel.getEncodedPath();
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

    @PostMapping("/travel/{path}/join")
    public String joinTravel(@CurrentUser Account account, @PathVariable String path, Model model) {
        Travel travel = travelRepository.findTravelWithMembersByPath(path);
        travelService.addMember(travel, account);
        return "redirect:/travel/"+travel.getEncodedPath()+"/members";
    }

    @PostMapping("/travel/{path}/leave")
    public String leaveTravel(@CurrentUser Account account, @PathVariable String path, Model model) {
        Travel travel = travelRepository.findTravelWithMembersByPath(path);
        travelService.removeMember(travel, account);
        return "redirect:/travel/"+travel.getEncodedPath()+"/members";
    }

    @GetMapping("/travel/{path}/announcement")
    public String announceTravel(@CurrentUser Account account, @PathVariable String path, Model model) {
        Travel travel = travelRepository.findByPath(path);
        model.addAttribute(account);
        model.addAttribute(travel);
        List<Board> boardList = boardRepository.findByTravelOrderByCreatedDateTimeDesc(travel);
        model.addAttribute("boards", boardList);
        return "travel/announcement";
    }
}
