package Tamanegiseoul.comeet.dto.comment.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@NoArgsConstructor
public class CreateCommentRequest {

    @NonNull
    @ApiModelProperty(notes="게시글 ID", example="12", required=true)
    private Long postId;

    @NonNull
    @ApiModelProperty(notes="덧글 작성자 ID", example="6", required=true)
    private Long memberId;

    @NonNull
    @ApiModelProperty(notes="적성할 덧글 내용", example="예시 덧글입니다.", required=true)
    private String content;

    @Builder
    public CreateCommentRequest(Long postId, Long memberId, String content) {
        this.postId = postId;
        this.memberId = memberId;
        this.content = content;
    }
}
