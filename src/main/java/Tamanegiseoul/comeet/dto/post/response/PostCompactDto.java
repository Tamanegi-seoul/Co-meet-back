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
public class PostCompactDto {

    private Long postId;

    private String title;

    @Enumerated(EnumType.STRING)
    private RecruitStatus recruitStatus;

    @Enumerated(EnumType.STRING)
    private GroupType groupType;
    // private Boolean remote; // can be changed
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    private Long posterId;

    private String posterNickname;

    @Enumerated(EnumType.STRING)
    private List<TechStack> designatedStacks;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedTime;

    public static PostCompactDto toDto(Posts findPost) {
        return PostCompactDto.builder()
                .postId(findPost.getPostId())
                .title(findPost.getTitle())
                .groupType(findPost.getGroupType())
                .posterId(findPost.getPoster().getMemberId())
                .posterNickname(findPost.getPoster().getNickname())
                .recruitStatus(findPost.getRecruitStatus())
                .startDate(findPost.getStartDate())
                .createdTime(findPost.getCreatedTime())
                .modifiedTime(findPost.getModifiedTime())
                .build();
    }

    public PostCompactDto designatedStacks(List<TechStack> stacks) {
        this.designatedStacks = stacks;
        return this;
    }
}
