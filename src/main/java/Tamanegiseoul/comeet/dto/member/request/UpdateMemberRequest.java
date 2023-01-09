package Tamanegiseoul.comeet.dto.member.request;

import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class UpdateMemberRequest {
    @Schema(description = "수정할 회원 ID", example = "5")
    private Long memberId;
    @Schema(description = "회원의 새로운 닉네임", example = "doej123")
    private String newNickname;
    @Schema(description = "회원의 새로운 선호 기술스택", example = "JAVA_SCRIPT, REACT")
    private List<TechStack> updatedStacks;
}
