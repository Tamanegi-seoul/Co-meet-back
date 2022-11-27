package Tamanegiseoul.comeet.dto.comment.response;

import Tamanegiseoul.comeet.domain.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateCommentResponse {
    private Long commentId;
    private Long postId;
    private String postTitle;
    private Long commenterId;
    private String commenterNickname;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public static CreateCommentResponse toDto(Comment comment) {
        return CreateCommentResponse.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .postTitle(comment.getPost().getTitle())
                .commenterId(comment.getUser().getUserId())
                .commenterNickname(comment.getUser().getNickname())
                .content(comment.getContent())
                .createdTime(comment.getCreatedTime())
                .modifiedTime(comment.getModifiedTime())
                .build();
    }
}
