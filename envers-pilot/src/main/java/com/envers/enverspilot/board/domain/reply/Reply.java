package com.envers.enverspilot.board.domain.reply;

import com.envers.enverspilot.board.domain.Board;
import com.envers.enverspilot.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Audited
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    private Reply(String comment) {
        this.comment = comment;
    }

    public void registerBoard(Board board) {
        if (this.board != null) {
            this.board.getReplies().remove(this);
        }
        board.addReply(this);
        this.board = board;
    }
}
