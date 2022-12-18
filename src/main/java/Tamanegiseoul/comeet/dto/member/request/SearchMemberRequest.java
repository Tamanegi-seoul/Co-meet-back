package Tamanegiseoul.comeet.dto.member.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchMemberRequest {
    @Schema(description = "조회할 회원 ID", example = "5")
    private Long memberId;
}
