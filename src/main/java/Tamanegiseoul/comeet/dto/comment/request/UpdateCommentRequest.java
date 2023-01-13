package Tamanegiseoul.comeet.dto.comment.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class UpdateCommentRequest {
    @Schema(description = "작성할 덧글의 포스트 ID", example = "5")
    private Long commentId;
    @Schema(description = "작성할 덧글의 포스트 ID", example = "5")
    private String content;

    @Builder
    public UpdateCommentRequest(Long commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }
}
