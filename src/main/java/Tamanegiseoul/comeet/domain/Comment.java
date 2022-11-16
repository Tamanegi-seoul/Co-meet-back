package Tamanegiseoul.comeet.domain;

import Tamanegiseoul.comeet.dto.comment.request.UpdateCommentRequest;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id @GeneratedValue
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "post_id")
    private Posts post;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "user_id")
    private Users user;

    @NotNull
    private String content;

    @NotNull
    private LocalDate createdDate;

    @NotNull
    private LocalDate modifiedDate;

    @Builder
    public Comment(Posts post, Users user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.createdDate = LocalDate.now();
        this.modifiedDate = LocalDate.now();
    }

    public void updateModifiedDate() {
        this.modifiedDate = LocalDate.now();
    }
    public void updateCreatedDate() { this.createdDate = LocalDate.now(); }

    public void updateComment(UpdateCommentRequest updatedComment) {
        this.content = updatedComment.getContent();
    }
}
