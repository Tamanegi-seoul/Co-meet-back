package Tamanegiseoul.comeet.dto.comment.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class UpdateCommentRequest {
    @NonNull
    @ApiModelProperty(notes="수정할 덧글의 ID", example = "5", required=true)
    private Long commentId;

    @NonNull
    @ApiModelProperty(notes="수정할 덧글 내용", example = "5", required=true)
    private String content;

    @Builder
    public UpdateCommentRequest(Long commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }
}
