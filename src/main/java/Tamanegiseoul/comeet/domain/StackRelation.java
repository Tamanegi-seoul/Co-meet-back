package Tamanegiseoul.comeet.domain;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;

@Getter
@Entity
@Table(name = "stack_relation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StackRelation {
    @Id @GeneratedValue
    @Column(name = "stack_relation_id")
    private Long stackRelationId;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "post_id")
    private Posts post;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TechStack techStack;

    @Builder
    public StackRelation(Member member, Posts post, TechStack techStack) {
        this.member = member;
        this.post = post;
        this.techStack = techStack;
    }
}
