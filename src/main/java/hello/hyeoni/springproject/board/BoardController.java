package hello.hyeoni.springproject.board;

import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.account.CurrentUser;
import hello.hyeoni.springproject.travel.Travel;
import hello.hyeoni.springproject.travel.TravelService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final TravelService travelService;
    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final ModelMapper modelMapper;

    @GetMapping("/travel/{path}/board/form")
    public String BoardForm(@CurrentUser Account account, @PathVariable String path, Model model) {
        Travel travel = travelService.getTravel(path);
        model.addAttribute(account);
        model.addAttribute(travel);
        model.addAttribute(new BoardForm());
        return "board/form";
    }

    @PostMapping("/board/{path}/new")
    public String newBoardSubmit(@CurrentUser Account account, @PathVariable String path, @Valid BoardForm boardForm, Errors errors, Model model) {
        Travel travel = travelService.getTravel(path);
        if(errors.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(travel);
            return "board/form";
        }

        Board board = boardService.createBoard(modelMapper.map(boardForm, Board.class), travel, account);
        return "redirect:/travel/"+travel.getEncodedPath()+"/board/"+board.getId();
    }

    @GetMapping("/travel/{path}/board/{id}")
    public String getTravelBoard(@CurrentUser Account account, @PathVariable String path, @PathVariable("id")Board board, Model model) {
        model.addAttribute(account);
        model.addAttribute(board);
        model.addAttribute(travelService.getTravel(path));
        return "board/view";
    }
}
