package Tamanegiseoul.comeet.dto.member.response;

import Tamanegiseoul.comeet.domain.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class RemoveMemberResponse {

    @NonNull
    @ApiModelProperty(notes="삭제된 회원 ID", example="24", required=true)
    private Long memberId;

    @NonNull
    @ApiModelProperty(notes="삭제된 회원 닉네임", example="kenneth", required=true)
    private String nickname;

    public static RemoveMemberResponse toDto(Member member) {
        return RemoveMemberResponse.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .build();
    }
}
