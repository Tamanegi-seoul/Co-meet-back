package Tamanegiseoul.comeet.dto.comment.response;

import Tamanegiseoul.comeet.domain.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class RemoveCommentResponse {
    private Long postId;
    private Long commentId;
    private Long commenterId;
    private String deletedContent;

    public static RemoveCommentResponse toDto(Comment findComment) {
        return RemoveCommentResponse.builder()
                .commentId(findComment.getCommentId())
                .commenterId(findComment.getMember().getMemberId())
                .deletedContent(findComment.getContent())
                .postId(findComment.getPost().getPostId())
                .build();
    }
}
