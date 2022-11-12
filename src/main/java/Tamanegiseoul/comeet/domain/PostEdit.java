package Tamanegiseoul.comeet.domain;

import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.RecruitStatus;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostEdit {

    private String title;
    private String content;
    private ContactType contactType;
    private String contact;
    private RecruitStatus recruitStatus;
    private Long recruitCapacity;
    private LocalDate startDate;
    private Long hits;
    private Long expectedTerm;
    private LocalDate createdDate;
    private List<TechStack> stacks = new ArrayList<>();

    @Builder
    public PostEdit(String title, String content, ContactType contactType, String contact, RecruitStatus recruitStatus, Long recruitCapacity, LocalDate startDate, Long hits, Long expectedTerm, LocalDate createdDate, TechStack ...ts) {
        this.title = title;
        this.content = content;
        this.contactType = contactType;
        this.contact = contact;
        this.recruitStatus = recruitStatus;
        this.recruitCapacity = recruitCapacity;
        this.startDate = startDate;
        this.hits = hits;
        this.expectedTerm = expectedTerm;
        this.createdDate = createdDate;
        for(TechStack stack : ts) {
            stacks.add(stack);
        }
    }
}
