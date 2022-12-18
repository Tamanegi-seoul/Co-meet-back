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
    private Long commentId;
    private Long postId;
    private String postTitle;
    private Long commenterId;
    private String commenterNickname;
    private String content;
    private LocalDateTime createdTime;
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
