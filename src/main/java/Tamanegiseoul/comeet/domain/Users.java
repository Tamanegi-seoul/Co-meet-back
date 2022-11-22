package Tamanegiseoul.comeet.domain;

import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.dto.user.response.SearchUserResponse;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotNull @Column(unique = true)
    private String nickname;

    @NotNull @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @OneToMany(mappedBy = "stackRelationId", fetch = FetchType.EAGER, cascade = ALL, orphanRemoval = true)
    private List<StackRelation> preferStacks = new ArrayList<>();

    @OneToMany(mappedBy = "postId", cascade = ALL, orphanRemoval = true)
    private List<Posts> wrotePosts = new ArrayList<>();

    @NotNull
    private LocalDate createdDate;

    @NotNull
    private LocalDate modifiedDate;

    @Builder
    public Users(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    // update status of Users
    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void changePassword(String newPassword) {
        this.password = password;
    }

    public void addPreferStack(TechStack ts) {
        //StackRelation newSt = StackRelation.createForUser(this, ts);
        this.preferStacks.add(StackRelation.builder()
                .user(this)
                .techStack(ts).build());
    }

    // set LocalTime for created, modified date
    public void updateCreatedDate() {
        this.createdDate = LocalDate.now();
    }

    public void updateModifiedDate() {
        this.modifiedDate = LocalDate.now();
    }

    @Transactional
    public void initPreferredTechStacks() {
        //this.preferStacks = new ArrayList<>();
        this.preferStacks.clear();
    }

}
