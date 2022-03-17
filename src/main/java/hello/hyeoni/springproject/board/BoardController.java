package hello.hyeoni.springproject.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public String getBoard(@RequestParam(defaultValue = "1") int page, Board board, Model model) {
        System.out.println(board.getTitle());
        if(board.getTitle() == null) {
            board.setTitle("");
        }
        String title = board.getTitle();
        System.out.println(title);
        var pageSize = 5;
        Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("id").descending());
        Page<Board> boards = boardRepository.findByTitle(title, pageable);
        model.addAttribute("list", boards);
        return pagingModel(boards, model, pageSize, page);
    }

    private String pagingModel(Page<Board> boards, Model model, int pageSize, int page) {
        var total = boards.getTotalElements();
        var totalPages = boards.getTotalPages();
        var totPage = total % pageSize == 0 ? Math.floor(total/pageSize) : Math.floor(total/pageSize)+1 ;
        var pageBlock = Math.ceil(totPage / (double)pageSize);
        var curBlock = Math.ceil(page / (double)pageSize);
        var targetPage = (curBlock - 1) * pageSize - (pageSize - 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        var previous = curBlock > 1 ? targetPage : 1;
        targetPage = curBlock * pageSize + 1;
        var next = pageBlock > curBlock ? targetPage : totalPages;
        model.addAttribute("previous", (int)previous);
        model.addAttribute("next", (int)next);
        var pPage = targetPage-pageSize;
        var nPage = pageBlock > curBlock ? next - 1 : totalPages;
        model.addAttribute("pPage", pPage);
        model.addAttribute("nPage", nPage);
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
