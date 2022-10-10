package hello.hyeoni.springproject.board;

import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.travel.Travel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    public Board createBoard(Board board, Travel travel, Account account) {
        board.setCreatedBy(account);
        board.setTravel(travel);
        board.setCreatedDateTime(LocalDateTime.now());
        return boardRepository.save(board);
    }

    public void updateBoard(Board board, BoardForm boardForm) {
        modelMapper.map(boardForm, board);
    }
}
