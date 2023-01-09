package Tamanegiseoul.comeet.dto.comment.response;

import Tamanegiseoul.comeet.domain.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiParam;
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
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class SearchCommentResponse {

    private List<CommentDto> commentList;

}
