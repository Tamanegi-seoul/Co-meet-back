package Tamanegiseoul.comeet.dto.post.response;

import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.GroupType;
import Tamanegiseoul.comeet.domain.enums.RecruitStatus;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreatePostResponse {
    private Long postId;
    private String title;
    @Enumerated(EnumType.STRING)
    private GroupType groupType;
    private String content;
    @Enumerated(EnumType.STRING)
    private RecruitStatus recruitStatus;
    private Long recruitCapacity;
    @Enumerated(EnumType.STRING)
    private ContactType contactType;
    private String contact;
    private Boolean remote;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;
    private Long expectedTerm;
    private String posterNickname;
    @Enumerated(EnumType.STRING)
    private List<TechStack> designatedStacks;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedTime;

    public static CreatePostResponse toDto(Posts post) {
        return CreatePostResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .groupType(post.getGroupType())
                .contactType(post.getContactType())
                .contact(post.getContact())
                .remote(post.getRemote())
                .expectedTerm(post.getExpectedTerm())
                .recruitCapacity(post.getRecruitCapacity())
                .recruitStatus(post.getRecruitStatus())
                .startDate(post.getStartDate())
                .expectedTerm(post.getExpectedTerm())
                .createdTime(post.getCreatedTime())
                .modifiedTime(post.getModifiedTime())
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
