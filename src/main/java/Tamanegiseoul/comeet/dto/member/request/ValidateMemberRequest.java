package Tamanegiseoul.comeet.dto.member.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class ValidateMemberRequest {
    @Schema(description = "사용가능한지 검증할 닉네임", example = "newjohn")
    private String nickname;
    @Schema(description = "사용가능한지 검증할 이메일", example = "other.john@gmail.com")
    private String email;
}
