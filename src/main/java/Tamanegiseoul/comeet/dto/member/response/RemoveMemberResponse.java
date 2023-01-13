package Tamanegiseoul.comeet.dto.member.response;

import Tamanegiseoul.comeet.domain.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class RemoveMemberResponse {
    private Long memberId;
    private String nickname;

    public static RemoveMemberResponse toDto(Member member) {
        return RemoveMemberResponse.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .build();
    }
}
