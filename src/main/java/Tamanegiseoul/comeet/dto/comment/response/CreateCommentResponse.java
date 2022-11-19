package Tamanegiseoul.comeet.dto.comment.response;

import Tamanegiseoul.comeet.domain.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

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

    public static CreateCommentResponse toDto(Comment comment) {
        return CreateCommentResponse.builder()
                .commentId(comment.getId())
                .postId(comment.getPost().getId())
                .postTitle(comment.getPost().getTitle())
                .commenterId(comment.getUser().getId())
                .commenterNickname(comment.getUser().getNickname())
                .content(comment.getContent())
                .build();
    }
}
