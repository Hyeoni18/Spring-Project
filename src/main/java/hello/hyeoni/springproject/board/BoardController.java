package hello.hyeoni.springproject.board;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardRepository boardRepository;

    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @GetMapping
    public String getBoard(Model model) {
        model.addAttribute("listBoards", boardRepository.findAll());
        return "board/board";
    }

    @PostMapping //생성, 수정
    public String createOrUpdateBoard(Board board) {
        this.boardRepository.save(board);
        return "redirect:/board/" + board.getId();
    }

    @GetMapping("/new")
    public String newForm() {
        return "board/boardForm";
    }

    @GetMapping("/edit/{boardId}")
    public String editForm(@PathVariable int boardId, Model model) {
        model.addAttribute("board", boardRepository.getById(boardId));
        return "board/boardForm";
    }

    @GetMapping("/{boardId}")
    public String showBoard(@PathVariable int boardId, Model model) {
        model.addAttribute("board", boardRepository.getById(boardId));
        return "board/boardDetails";
    }

    @GetMapping("/delete/{boardId}")
    public String deleteBoard(@PathVariable int boardId) {
        boardRepository.deleteById(boardId);
        return "redirect:/board";
    }

}
