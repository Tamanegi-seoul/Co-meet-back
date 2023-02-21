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
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class SearchPostResponse {
    @NonNull
    @ApiModelProperty(notes="조회된 게시글 ID", example="12", required = true)
    private Long postId;
    @NonNull
    @ApiModelProperty(notes="조회된 게시글 제목", example="스터디원 모집합니다", required = true)
    private String title;
    @NonNull
    @ApiModelProperty(notes="조회된 게시글의 그룹 타입", example="STUDY", required = true)
    @Enumerated(EnumType.STRING)
    private GroupType groupType;
    @NonNull
    @ApiModelProperty(notes="조회된 게시글 내용", example="연락주세요~", required = true)
    private String content;
    @NonNull
    @ApiModelProperty(notes="조회된 게시글의 그룹 모집 상태", example="DONE", required = true)
    @Enumerated(EnumType.STRING)
    private RecruitStatus recruitStatus;

    @NonNull
    @ApiModelProperty(notes="조회된 게시글의 모집 정원", example="4", required = true)
    private Long recruitCapacity;

    @NonNull
    @ApiModelProperty(notes="조회 게시글의 연락 수단", example="GOOGLE_FORM", required = true)
    @Enumerated(EnumType.STRING)
    private ContactType contactType;
    @NonNull
    @ApiModelProperty(notes="조회 게시글의 연락처", example="form.google.com/die3IK39KJL", required = true)
    private String contact;
    @NonNull
    @ApiModelProperty(notes="조회된 게시글의 그룹 온라인 진행 여부", example="true", required = true)
    private Boolean remote;
    @NonNull
    @ApiModelProperty(notes="조회된 게시글의 그룹 활동 시작일", example="2022-01-10", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;
    @NonNull
    @ApiModelProperty(notes="조회된 게시글의 그룹 활동 기간(일)", example="60", required = true)
    private Long expectedTerm;

    @NonNull
    @ApiModelProperty(notes="조회된 게시글의 작성자 ID", example="20", required = true)
    private Long posterId;
    @NonNull
    @ApiModelProperty(notes="조회된 게시글의 작성자 닉네임", example="20", required = true)
    private String posterNickname;

    @ApiModelProperty(notes="조회된 게시글의 작성자 프로필 이미지")
    private ImageDto posterProfile;
    @NonNull
    @ApiModelProperty(notes="조회된 게시글의 그룹 기술스택", example="JAVA, SPRING", required = true)
    @Enumerated(EnumType.STRING)
    private List<TechStack> designatedStacks;
    @NonNull
    @ApiModelProperty(notes="게시글 등록일", example="2022-12-12 09:12:17", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
    @NonNull
    @ApiModelProperty(notes="게시글 수정일", example="2022-12-12 09:12:17", required = true)
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
