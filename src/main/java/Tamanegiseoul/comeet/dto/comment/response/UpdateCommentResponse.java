package Tamanegiseoul.comeet.dto.comment.response;

import Tamanegiseoul.comeet.domain.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateCommentResponse {
    @ApiParam(value = "덧글 ID")
    private Long commentId;
    @ApiParam(value = "덧글 포스트 ID")
    private Long postId;
    @ApiParam(value = "포스트 제목")
    private String postTitle;
    @ApiParam(value = "덧글 작성자 ID")
    private Long commenterId;
    @ApiParam(value = "덧글 작성자 닉네임")
    private String commenterNickname;
    @ApiParam(value = "덧글 내용")
    private String content;
    @ApiParam(value = "덧글 작성 시간")
    private LocalDateTime createdTime;
    @ApiParam(value = "최근 덧글 수정 시간")
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
