package hello.hyeoni.springproject.board;

import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.account.CurrentUser;
import hello.hyeoni.springproject.account.validator.PasswordFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    @InitBinder("boardForm")
    public void passwordFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new BoardValidator());
    }

    @GetMapping
    public String viewBoard(@CurrentUser Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }

//        var pageSize = 5;
//        Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("id").descending());
//        Page<Board> boards = boardRepository.findByTitle(title, pageable);
//        Board boards = boardRepository.findByTitle(title);

        List<Board> boards = boardRepository.findAll();
        model.addAttribute("boards", boards);

        //return pagingModel(boards, model, pageSize, page);
        return "board/board";
    }

//    private String pagingModel(Page<Board> boards, Model model, int pageSize, int page) {
//
//        var total = boards.getTotalElements();
//        var totalPages = boards.getTotalPages();
//        var pageBlock = Math.ceil(totalPages / (double)pageSize);
//        var curBlock = Math.ceil(page / (double)pageSize);
//
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("currentPage", page);
//
//        var targetPage = (curBlock - 1) * pageSize - (pageSize - 1);
//        var previous = curBlock > 1 ? targetPage : 1;
//        targetPage = curBlock * pageSize + 1;
//        var next = pageBlock > curBlock ? targetPage : totalPages;
//
//        model.addAttribute("previous", (int)previous);
//        model.addAttribute("next", (int)next);
//
//        var pPage = targetPage-pageSize;
//        var nPage = pageBlock > curBlock ? next - 1 : totalPages;
//
//        model.addAttribute("pPage", pPage);
//        model.addAttribute("nPage", nPage);
//
//        return "board/board";
//    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute(new BoardForm());
        return "board/boardForm";
    }

    @GetMapping("/edit/{boardId}")
    public String editForm(@CurrentUser Account account, @PathVariable("boardId") Board board, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(board, BoardForm.class));
        return "board/boardForm";
    }

    @PostMapping
    public String createOrUpdateBoard(@CurrentUser Account account, @Valid BoardForm boardForm, Errors errors) {
        if(errors.hasErrors()) {
            return "board/boardForm";
        }
        Board board = boardService.processNewBoard(account, boardForm);
        return "redirect:/board/" + board.getId();
    }

    @GetMapping("/{boardId}")
    public String showBoard(@CurrentUser Account account, @PathVariable("boardId") Board board, Model model) {
        model.addAttribute(account);
        model.addAttribute(board);
        return "board/boardDetails";
    }

    @GetMapping("/delete/{boardId}")
    public String deleteBoard(@PathVariable("boardId") Board board) {
        boardService.deleteBoard(board);
        return "redirect:/board";
    }

}
