package Tamanegiseoul.comeet.dto.post.response;

import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.RecruitStatus;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreatePostResponse {
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
    private String posterNickname;
    @Enumerated(EnumType.STRING)
    private List<TechStack> designatedStacks;

    public static CreatePostResponse toDto(Posts post) {
        return CreatePostResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .contactType(post.getContactType())
                .contact(post.getContact())
                .expectedTerm(post.getExpectedTerm())
                .recruitCapacity(post.getRecruitCapacity())
                .recruitStatus(post.getRecruitStatus())
                .startDate(post.getStartDate())
                .expectedTerm(post.getExpectedTerm())
                .build();
    }

    public CreatePostResponse designatedStacks(List<TechStack> stacks) {
        this.designatedStacks = stacks;
        return this;
    }

    public CreatePostResponse posterNickname(String name) {
        this.posterNickname = name;
        return this;
    }
}
