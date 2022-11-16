package Tamanegiseoul.comeet.dto.comment.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateCommentRequest {
    private Long commentId;
    private String content;
}
