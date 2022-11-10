package Tamanegiseoul.comeet.domain;

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
    private String comment;

    @NotNull
    private LocalDate createdDate;

    @NotNull
    private LocalDate modifiedDate;

    @Builder
    public Comment(Posts post, Users user, String comment) {
        this.post = post;
        this.user = user;
        this.comment = comment;
        this.createdDate = LocalDate.now();
        this.modifiedDate = LocalDate.now();
    }

}
