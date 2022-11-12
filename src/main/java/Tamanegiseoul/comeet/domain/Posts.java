package Tamanegiseoul.comeet.domain;

import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.RecruitStatus;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.dto.post.request.UpdatePostRequest;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private RecruitStatus recruitStatus;

    @NotNull
    private Long recruitCapacity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    @NotNull
    private String contact;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private Long expectedTerm;

    @NotNull
    @ManyToOne @JoinColumn(name="user_id")
    private Users poster;

    @NotNull
    @OneToMany(mappedBy="post", cascade = ALL, orphanRemoval = true)
    private List<StackRelation> designatedStack = new ArrayList<>();

    @OneToMany(mappedBy="id", cascade = ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @NotNull
    private Long hits = 0L;

    @NotNull
    private String content;

    @NotNull
    private LocalDate createdDate;

    @NotNull
    private LocalDate modifiedDate;

    @Builder
    public Posts(String title, Long recruitCapacity, String contact, ContactType contactType, LocalDate startDate, Long expectedTerm, String content, Users poster) {
        this.title = title;
        this.recruitStatus = RecruitStatus.RECRUIT;
        this.recruitCapacity = recruitCapacity;
        this.startDate = startDate;
        this.expectedTerm = expectedTerm;
        this.content = content;
        this.poster = poster;
        this.contactType = contactType;
        this.contact = contact;
        this.hits = 0L;
        this.createdDate = LocalDate.now();
        this.modifiedDate = LocalDate.now();
    }

    public void addDesignateStack(TechStack ts) {
        this.designatedStack.add(StackRelation.builder()
                .post(this)
                .techStack(ts).build());
    }

    public void increaseHits() {
        this.hits++;
    }

//    public void updateDesignateStack(TechStack ...ts) {
//        for(TechStack stack : ts) {
//            this.designatedStack.add(StackRelation.builder()
//                    .post(this)
//                    .techStack(stack).build());
//        }
//    }

    public void updateDesignateStack(List<TechStack> tsArr) {
        for(TechStack stack : tsArr) {
            this.designatedStack.add(StackRelation.builder()
                    .post(this)
                    .techStack(stack).build());
        }
    }

    public void updateTitle(String title) {
        this.title = title;
    }
    public void updateRecruitCapacity(Long newCap) {
        this.recruitCapacity = newCap;
    }
    public void updateStartDate(LocalDate newDate) { this.startDate = newDate; }
    public void updateExpectedTerm(Long newExpTerm) { this.expectedTerm = newExpTerm; }
    public void updateContent(String newContent) { this.content = content; }
    public void updateContactType(ContactType newCT) { this.contactType = newCT; }
    public void updateContact(String newContact) { this.contact = contact; }
    public void updateCreatedDate() { this.createdDate = LocalDate.now(); }
    public void updateModifiedDate() { this.modifiedDate = LocalDate.now(); }
    public void updateRecruitStatus(RecruitStatus rs) { this.recruitStatus = rs; }

    public void initDesignateStack() {
        this.designatedStack = new ArrayList<>();
    }

    public void updatePost(UpdatePostRequest updatePost) {
        this.title = updatePost.getTitle();
        this.content = updatePost.getContent();
        this.contactType = updatePost.getContactType();
        this.contact = updatePost.getContact();
        this.recruitStatus = updatePost.getRecruitStatus();
        this.recruitCapacity = updatePost.getRecruitCapacity();
        this.startDate = updatePost.getStartDate();
        this.expectedTerm = updatePost.getExpectedTerm();
    }


    public String printout() {
        return String.format("title: %s\n content: %s\n recruit status: %s\n recruit capacity: %s\n start date: %s\n" +
                "contact: %s\n contact type: %s\n start date: %s\n expected term: %s\n created date: %s\n modified date: %s\n",
                this.title, this.content, this.recruitStatus, this.recruitCapacity, this.startDate, this.contact,
                this.contactType, this.startDate, this.expectedTerm, this.createdDate, this.modifiedDate);
    }
}
