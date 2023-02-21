package Tamanegiseoul.comeet.dto.comment.response;

import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.ImageData;
import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.dto.member.response.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Data @Slf4j
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class CommentDto {
    @NonNull
    @ApiModelProperty(notes="게시글 ID", example="8", required=true)
    private Long postId;

    @NonNull
    @ApiModelProperty(notes="덧글 ID", example="16", required=true)
    private Long commentId;

    @NonNull
    @ApiModelProperty(notes="덧글 작성자 ID", example="12", required=true)
    private Long commenterId;

    @NonNull
    @ApiModelProperty(notes="덧글 작성자 닉네임", example="John Doe", required=true)
    private String commenterNickname;

    @NonNull
    @ApiModelProperty(notes="덧글 내용", example="Hello World", required=true)
    private String content;

    @Nullable
    @ApiModelProperty(notes="덧글 작성자 프로필 이미지", required = false)
    private ImageDto commenterProfile;

    @Nullable
    @ApiModelProperty(notes="덧글 생성일", example="2022-12-12 09:12:17", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;

    @Nullable
    @ApiModelProperty(notes="덧글 수정일", example="2022-12-12 10:11:24", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedTime;

    public static CommentDto toDto(Member member, Posts post, ImageDto imageDto, Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .postId(post.getPostId())
                .commenterNickname(member.getNickname())
                .commenterId(member.getMemberId())
                .commenterProfile(imageDto)
                .createdTime(comment.getCreatedTime())
                .modifiedTime(comment.getModifiedTime())
                .build();
    }

}
