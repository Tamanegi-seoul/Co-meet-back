package Tamanegiseoul.comeet.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Posts {
    @Id @GeneratedValue
    @Column(name = "post_id", nullable = false)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Long recruitCapacity;

    @NotNull
    private String contact;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private Long expectedTerm;

    @OneToMany(mappedBy="post", cascade = ALL, orphanRemoval = true)
    private List<StackRelation> techStacks = new ArrayList<>();

    @OneToMany(mappedBy="id", cascade = ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @NotNull
    private Long hits;

    @NotNull
    private String content;

    @NotNull
    private LocalDate createdDate;

    @NotNull
    private LocalDate modifiedDate;

}
