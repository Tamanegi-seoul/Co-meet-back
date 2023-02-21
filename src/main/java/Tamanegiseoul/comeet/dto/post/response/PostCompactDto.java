package Tamanegiseoul.comeet.dto.post.response;

import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.GroupType;
import Tamanegiseoul.comeet.domain.enums.RecruitStatus;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class PostCompactDto {

    @NonNull
    @ApiModelProperty(notes="게시글 ID", example="27", required = true)
    private Long postId;

    @NonNull
    @ApiModelProperty(notes="게시글 제목", example="자바 스터디 구해요", required = true)
    private String title;

    @NonNull
    @ApiModelProperty(notes="게시글의 그룹 모집 상태", example="DONE", required = true)
    @Enumerated(EnumType.STRING)
    private RecruitStatus recruitStatus;

    @NonNull
    @ApiModelProperty(notes="게시글의 그룹 타입", example="STUDY", required = true)
    @Enumerated(EnumType.STRING)
    private GroupType groupType;
    // private Boolean remote; // can be changed

    @NonNull
    @ApiModelProperty(notes="게시글의 모임 활동 시작일", example="2022-12-12 09:12:17", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    @NonNull
    @ApiModelProperty(notes="게시글의 작성자 ID", example="7", required = true)
    private Long posterId;

    @NonNull
    @ApiModelProperty(notes="게시글의 작성자 닉네임", example="John Doe", required = true)
    private String posterNickname;

    @NonNull
    @ApiModelProperty(notes="게시글의 모임 선호 기술스택", example="JAVA, SPRING", required = true)
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

    public static List<PostCompactDto> toCompactDtoList(List<Posts> postList) {
        List<PostCompactDto> list = new ArrayList<>();
        for(Posts post : postList) {
            PostCompactDto dto = PostCompactDto.toDto(post);
            dto.designatedStacks(post.exportTechStack());
            list.add(dto);
        }
        return list;
    }

    public PostCompactDto designatedStacks(List<TechStack> stacks) {
        this.designatedStacks = stacks;
        return this;
    }
}
