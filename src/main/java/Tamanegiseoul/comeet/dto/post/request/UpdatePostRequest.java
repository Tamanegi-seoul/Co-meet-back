package Tamanegiseoul.comeet.dto.post.request;

import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.RecruitStatus;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdatePostRequest {
    private Long postId;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private RecruitStatus recruitStatus;
    private Long recruitCapacity;
    @Enumerated(EnumType.STRING)
    private ContactType contactType;
    private String contact;
    private LocalDate startDate;
    private Long expectedTerm;
    @Enumerated(EnumType.STRING)
    private List<TechStack> designatedStacks;
}
