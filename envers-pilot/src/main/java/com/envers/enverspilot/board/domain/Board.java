package com.envers.enverspilot.board.domain;

import com.envers.enverspilot.board.domain.reply.Reply;
import com.envers.enverspilot.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Audited
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @NotAudited
    private String sub;

    @NotAudited
    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Reply> replies = new ArrayList<>();

    @Builder
    private Board(String title, String content, String sub) {
        this.title = title;
        this.content = content;
        this.sub = sub;
    }

    public void addReply(Reply reply) {
        replies.add(reply);
    }

    public void update(Board board) {
        title = board.getTitle();
        content = board.getContent();
        sub = board.getSub();
    }
}
