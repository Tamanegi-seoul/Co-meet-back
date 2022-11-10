package Tamanegiseoul.comeet.domain;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Null;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(name = "stack_relation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StackRelation {
    @Id @GeneratedValue
    @Column(name = "stack_relation_id")
    private Long id;

    @Nullable
    @ManyToOne(fetch = LAZY) @JoinColumn(name = "post_id")
    private Posts post;

    @Nullable
    @ManyToOne(fetch = LAZY) @JoinColumn(name = "user_id")
    private Users user;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TechStack techStack;

    @Builder
    public StackRelation(Users user, Posts post, TechStack techStack) {
        this.user = user;
        this.post = post;
        this.techStack = techStack;
    }


//    public static StackRelation createForUser(Users user, TechStack ts) {
//        StackRelation newSt = new StackRelation();
//        newSt.user = user;
//        newSt.techStack = ts;
//        return newSt;
//    }
//
//    public static StackRelation createForPost(Posts post, TechStack ts) {
//        StackRelation newSt = new StackRelation();
//        newSt.post = post;
//        newSt.techStack = ts;
//        return newSt;
//    }


}
