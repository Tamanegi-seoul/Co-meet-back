package Tamanegiseoul.comeet.domain;

import Tamanegiseoul.comeet.dto.comment.request.UpdateCommentRequest;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id @GeneratedValue
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "post_id")
    private Posts post;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    private String content;

    @NotNull
    private LocalDateTime createdTime;

    @NotNull
    private LocalDateTime modifiedTime;

    @Builder
    public Comment(Posts post, Member member, String content) {
        this.post = post;
        this.member = member;
        this.content = content;
        this.createdTime = LocalDateTime.now();
        this.modifiedTime = LocalDateTime.now();
    }

    public void updateModifiedTime() {
        this.modifiedTime = LocalDateTime.now();
    }
    public void updateCreatedTime() { this.createdTime = LocalDateTime.now(); }

    public void updateComment(UpdateCommentRequest updatedComment) {
        this.content = updatedComment.getContent();
    }
}
