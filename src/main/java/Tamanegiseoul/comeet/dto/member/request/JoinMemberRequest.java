package Tamanegiseoul.comeet.dto.member.request;

import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@NoArgsConstructor
public class JoinMemberRequest {
    @Schema(description = "등록할 회원 이메일", example = "john.doe@gmail.com")
    private String email;
    @Schema(description = "등록할 회원 비밀번호", example = "p@ssword")
    private String password;
    @Schema(description = "등록할 회원 닉네임", example = "john123")
    private String nickname;
    @Schema(description = "등록할 회원 선호 기술스택", example = "JAVA, SPRING")
    private List<TechStack> preferStacks;

    @Builder
    public JoinMemberRequest(String email, String password, String nickname, List<TechStack> preferStacks) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.preferStacks = preferStacks;
    }

}
