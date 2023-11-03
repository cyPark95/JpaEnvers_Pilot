package com.envers.enverspilot.board.interfaces.dto;

import com.envers.enverspilot.board.domain.reply.Reply;

public record ReplyRequest(
        String comment
) {

    public Reply toEntity() {
        return Reply.builder()
                .comment(comment)
                .build();
    }
}
