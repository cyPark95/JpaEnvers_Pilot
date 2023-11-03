package com.envers.enverspilot.board.infrastructure;

import com.envers.enverspilot.board.domain.Board;
import com.envers.enverspilot.common.config.MyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepository extends MyRepository<Board, Long, Long> {

    Page<Board> findAll(Pageable pageable);
}
