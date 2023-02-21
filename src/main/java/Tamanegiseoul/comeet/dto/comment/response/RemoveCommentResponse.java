package Tamanegiseoul.comeet.dto.comment.response;

import Tamanegiseoul.comeet.domain.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class RemoveCommentResponse {
    @Nullable
    @ApiModelProperty(notes="삭제된 덧글의 게시글 ID", example="12", required = true)
    private Long postId;

    @Nullable
    @ApiModelProperty(notes="삭제된 덧글 ID", example="27", required = true)
    private Long commentId;

    @Nullable
    @ApiModelProperty(notes="덧글 작성자 ID", example="16", required = true)
    private Long commenterId;

    @Nullable
    @ApiModelProperty(notes="삭제된 덧글 내용", example="Oops!", required = true)
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
