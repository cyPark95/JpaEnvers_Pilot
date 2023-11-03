package com.envers.enverspilot.board.interfaces;

import com.envers.enverspilot.board.application.BoardService;
import com.envers.enverspilot.board.domain.Board;
import com.envers.enverspilot.board.interfaces.dto.BoardRequest;
import com.envers.enverspilot.board.interfaces.dto.BoardResponse;
import com.envers.enverspilot.board.interfaces.dto.ReplyRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponse> save(@RequestBody BoardRequest request) {
        Board board = boardService.save(request.toEntity());
        return ResponseEntity.ok(BoardResponse.from(board));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardResponse> update(
            @PathVariable Long id,
            @RequestBody BoardRequest request
    ) {
        Board board = boardService.update(id, request.toEntity());
        return ResponseEntity.ok(BoardResponse.from(board));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        boardService.deleteBoardById(id);
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<List<BoardResponse>> getHistory(@PathVariable Long id) {
        List<BoardResponse> histories = boardService.findBoardHistoryById(id).stream()
                .map(BoardResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(histories);
    }

    @PostMapping("/reply/{id}")
    public ResponseEntity<BoardResponse> save(@PathVariable Long id, ReplyRequest request) {
        Board board = boardService.saveReply(id, request.toEntity());
        return ResponseEntity.ok(BoardResponse.from(board));
    }
}
