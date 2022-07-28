package hello.hyeoni.springproject.board;

import hello.hyeoni.springproject.account.Account;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    public Board processNewBoard(Account account, BoardForm boardForm) {
        Board board = modelMapper.map(boardForm, Board.class);
        board.setAuthor(account);
        return boardRepository.save(board);
    }

    public void deleteBoard(Board board) {
        boardRepository.delete(board);
    }
}
