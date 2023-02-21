package Tamanegiseoul.comeet.dto.member.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class ValidateMemberResponse {

    @Nullable
    @ApiModelProperty(notes="사용가능한지 검증한 회원 닉네임", example="John Doe")
    private String nickname;

    @Nullable
    @ApiModelProperty(notes="사용가능한지 검증한 회원 닉네임", example="john.doe@gmail.com")
    private String email;
}
