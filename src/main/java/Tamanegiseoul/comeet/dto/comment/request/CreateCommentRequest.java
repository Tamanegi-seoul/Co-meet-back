package Tamanegiseoul.comeet.dto.comment.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@NoArgsConstructor
public class CreateCommentRequest {
    @Schema(description = "작성할 덧글의 포스트 ID", example = "5")
    private Long postId;
    @Schema(description = "덧글 작성자 ID", example = "3")
    private Long memberId;
    @Schema(description = "적성할 덧글 내용", example = "예시 덧글입니다.")
    private String content;

    @Builder
    public CreateCommentRequest(Long postId, Long memberId, String content) {
        this.postId = postId;
        this.memberId = memberId;
        this.content = content;
    }
}
