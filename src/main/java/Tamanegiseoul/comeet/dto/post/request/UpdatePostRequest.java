package Tamanegiseoul.comeet.dto.post.request;

import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.RecruitStatus;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class UpdatePostRequest {
    private String title;
    private String content;
    private ContactType contactType;
    private String contact;
    private RecruitStatus recruitStatus;
    private Long recruitCapacity;
    private LocalDate startDate;
    private Long expectedTerm;
    private List<TechStack> stacks = new ArrayList<>();
}
