package Tamanegiseoul.comeet.dto.member.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@Builder
public class ValidateMemberRequest {
    @NonNull
    @ApiModelProperty(notes="사용가능한지 검증할 닉네임", example="newjohn", required=true)
    private String nickname;
    @NonNull
    @ApiModelProperty(notes="사용가능한지 검증할 이메일", example="other.john@gmail.com", required=true)
    private String email;
}
