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
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class UpdatePostResponse {
    private Long postId;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private GroupType groupType;
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
    private Long posterId;
    private String posterNickname;
    @Enumerated(EnumType.STRING)
    private List<TechStack> designatedStacks;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedTime;

    public static UpdatePostResponse toDto(Posts findPost) {
        return UpdatePostResponse.builder()
                .postId(findPost.getPostId())
                .posterId(findPost.getPoster().getMemberId())
                .posterNickname(findPost.getPoster().getNickname())
                .title(findPost.getTitle())
                .groupType(findPost.getGroupType())
                .content(findPost.getContent())
                .contact(findPost.getContact())
                .contactType(findPost.getContactType())
                .remote(findPost.getRemote())
                .recruitCapacity(findPost.getRecruitCapacity())
                .recruitStatus(findPost.getRecruitStatus())
                .startDate(findPost.getStartDate())
                .createdTime(findPost.getCreatedTime())
                .modifiedTime(findPost.getModifiedTime())
                .build();
    }

    public UpdatePostResponse designatedStacks(List<TechStack> stacks) {
        this.designatedStacks = stacks;
        return this;
    }
}
