package Tamanegiseoul.comeet.dto.comment.response;

import Tamanegiseoul.comeet.domain.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchCommentResponse {

    private List<CommentDto> commentList;

    @Builder
    private static class CommentDto {
        private Long postId;
        private Long commentId;
        private Long commenterId;
        private String commenterNickname;
        private String content;
    }

    public static List<CommentDto> commentToDto(List<Comment> commentList) {
        return commentList.stream().map(c -> CommentDto.builder()
                .postId(c.getPost().getId())
                .commentId(c.getId())
                .commenterId(c.getUser().getId())
                .commenterNickname(c.getUser().getNickname())
                .content(c.getContent())
                .build()).collect(Collectors.toList());
    }
}
