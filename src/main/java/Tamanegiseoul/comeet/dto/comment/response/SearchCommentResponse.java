package Tamanegiseoul.comeet.dto.comment.response;

import Tamanegiseoul.comeet.domain.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor @Slf4j
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchCommentResponse {

    private List<CommentDto> commentList;

    public static List<CommentDto> commentListToDto(List<Comment> commentList) {
        log.warn("[SearchCommentResponse:commentToDto] commentList param's size is "+commentList.size());
        List<CommentDto> commentDtoList = new ArrayList<>();
        //commentList.stream().map(c -> commentDtoList.add(CommentDto.toDto(c)));
        for(Comment c : commentList) {
            commentDtoList.add(CommentDto.toDto(c));
        }
        return commentDtoList;
    }
}
