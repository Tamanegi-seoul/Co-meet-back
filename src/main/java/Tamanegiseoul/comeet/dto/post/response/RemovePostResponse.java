package Tamanegiseoul.comeet.dto.post.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class RemovePostResponse {
    @NonNull
    @ApiModelProperty(notes="게시글 ID", example="12", required = true)
    private Long postId;
}
