package Tamanegiseoul.comeet.dto.comment.response;

import Tamanegiseoul.comeet.domain.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data @Slf4j
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentDto {
    @ApiParam(value = "덧글의 포스트 ID")
    private Long postId;
    @ApiParam(value = "덧글 ID")
    private Long commentId;
    @ApiParam(value = "덧글 작성자 ID")
    private Long commenterId;
    @ApiParam(value = "덧글 작성자 닉네임")
    private String commenterNickname;
    @ApiParam(value = "덧글 내용")
    private String content;
    @ApiParam(value = "덧글 생성 시간")
    private LocalDateTime createdTime;
    @ApiParam(value = "최근 덧글 수정 시간")
    private LocalDateTime modifiedTime;

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getCommentId())
                .commenterNickname(comment.getMember().getNickname())
                .commenterId(comment.getMember().getMemberId())
                .content(comment.getContent())
                .createdTime(comment.getCreatedTime())
                .modifiedTime(comment.getModifiedTime())
                .build();
    }

}
