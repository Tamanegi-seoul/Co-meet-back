package Tamanegiseoul.comeet.dto.post.response;

import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.GroupType;
import Tamanegiseoul.comeet.domain.enums.RecruitStatus;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.dto.comment.response.CommentDto;
import Tamanegiseoul.comeet.dto.member.response.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchPostResponse {
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
    private Long posterId;
    private String posterNickname;

    private ImageDto posterProfile;
    @Enumerated(EnumType.STRING)
    private List<TechStack> designatedStacks;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedTime;

    private List<CommentDto> comments;

    public static SearchPostResponse toDto(Posts findPost) {
        return SearchPostResponse.builder()
                .postId(findPost.getPostId())
                .title(findPost.getTitle())
                .content(findPost.getContent())
                .groupType(findPost.getGroupType())
                .posterId(findPost.getPoster().getMemberId())
                .posterNickname(findPost.getPoster().getNickname())
                .contactType(findPost.getContactType())
                .contact(findPost.getContact())
                .remote(findPost.getRemote())
                .recruitCapacity(findPost.getRecruitCapacity())
                .recruitStatus(findPost.getRecruitStatus())
                .startDate(findPost.getStartDate())
                .expectedTerm(findPost.getExpectedTerm())
                .createdTime(findPost.getCreatedTime())
                .modifiedTime(findPost.getModifiedTime())
                .build();
    }

    public SearchPostResponse designatedStacks(List<TechStack> stacks) {
        this.designatedStacks = stacks;
        return this;
    }

    public SearchPostResponse comments(List<CommentDto> list) {
        this.comments = list;
        return this;
    }

    public SearchPostResponse posterProfile(ImageDto imageData) {
        this.posterProfile = imageData;
        return this;
    }


}
