package Tamanegiseoul.comeet.dto.comment.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class SearchCommentRequest {
    @Schema(description = "덧글 조회할 포스트 ID", example = "5")
    private Long postId;
}
