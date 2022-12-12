package Tamanegiseoul.comeet.dto.comment.response;

import Tamanegiseoul.comeet.domain.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data @Slf4j
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentDto {
    private Long postId;
    private Long commentId;
    private Long commenterId;
    private String commenterNickname;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getCommentId())
                .commenterNickname(comment.getMember().getNickname())
                .commenterId(comment.getMember().getMemberId())
                .content(comment.getContent())
                .createdTime(comment.getCreatedTime())
                .modifiedTime(comment.getModifiedTime())
                .build();
    }

}
