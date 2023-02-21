package Tamanegiseoul.comeet.dto.comment.response;

import Tamanegiseoul.comeet.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class UpdateCommentResponse {
    @Nullable
    @ApiModelProperty(notes="수정할 덧글 ID", example="19", required = true)
    private Long commentId;

    @Nullable
    @ApiModelProperty(notes="수정할 덧글의 게시글 ID", example="24", required = true)
    private Long postId;

    @Nullable
    @ApiModelProperty(notes="수정할 덧글의 게시글 제목", example="Algorithm Study!", required = true)
    private String postTitle;

    @Nullable
    @ApiModelProperty(notes="수정할 덧글의 작성자 ID", example="7", required = true)
    private Long commenterId;

    @Nullable
    @ApiModelProperty(notes="수정할 덧글의 작성자 닉네임", example="John Doe", required = true)
    private String commenterNickname;

    @Nullable
    @ApiModelProperty(notes="수정할 덧글의 내용", example="Foo Boo", required = true)
    private String content;

    @Nullable
    @ApiModelProperty(notes="덧글 생성일", example="2022-12-12 09:12:17", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;

    @Nullable
    @ApiModelProperty(notes="덧글 수정일", example="2022-12-12 09:12:17", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedTime;

    public static UpdateCommentResponse toDto(Comment comment) {
        return UpdateCommentResponse.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .postTitle(comment.getPost().getTitle())
                .commenterId(comment.getMember().getMemberId())
                .commenterNickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .createdTime(comment.getCreatedTime())
                .modifiedTime(comment.getModifiedTime())
                .build();
    }
}
