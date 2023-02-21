package Tamanegiseoul.comeet.dto.member.request;

import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@NoArgsConstructor
public class UpdateMemberRequest {
    @NonNull
    @ApiModelProperty(notes="수정할 회원 ID", example="5", required=true)
    private Long memberId;

    @NonNull
    @ApiModelProperty(notes="기존의 닉네임", example="johndoe", required=true)
    private String prevNickname;

    @NonNull
    @ApiModelProperty(notes="수정할 회원 닉네임", example="doej123", required=true)
    private String newNickname;
    @NonNull
    @ApiModelProperty(notes="회원의 새로운 선호 기술스택", example="JAVA_SCRIPT, REACT", required=true)
    private List<TechStack> updatedStacks;

    @Builder
    public UpdateMemberRequest(Long memberId, String prevNickname, String newNickname, List<TechStack> updatedStacks) {
        this.memberId = memberId;
        this.prevNickname = prevNickname;
        this.newNickname = newNickname;
        this.updatedStacks = updatedStacks;
    }
}
