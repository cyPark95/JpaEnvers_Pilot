package com.envers.enverspilot.board.application;

import com.envers.enverspilot.board.domain.Board;
import com.envers.enverspilot.board.domain.reply.Reply;
import com.envers.enverspilot.board.infrastructure.BoardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public Board findBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    @Transactional
    public Board save(Board board) {
        return boardRepository.save(board);
    }

    @Transactional
    public Board update(Long id, Board updateBoard) {
        Board board = findBoardById(id);
        board.update(updateBoard);
        return board;
    }

    @Transactional
    public void deleteBoardById(Long id) {
        boardRepository.deleteById(id);
    }

    public List<Board> findBoardHistoryById(Long id) {
        return boardRepository.findRevisions(id).stream()
                .map(Revision::getEntity)
                .toList();
    }

    @Transactional
    public Board saveReply(Long id, Reply reply) {
        Board board = findBoardById(id);
        reply.registerBoard(board);
        return board;
    }
}
