package Tamanegiseoul.comeet.dto.member.response;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class JoinMemberResponse {

    @NonNull
    @ApiModelProperty(notes="신규 회원 ID", example="13", required=true)
    private Long memberId;

    @NonNull
    @ApiModelProperty(notes="신규 회원 닉네임", example="kenneth", required=true)
    private String nickname;

    @NonNull
    @ApiModelProperty(notes="신규 회원 이메일", example="k.park@gmail.com", required=true)
    private String email;

    @NonNull
    @ApiModelProperty(notes="신규 회원 선호 기술스택", example="JAVA, SPRING", required=true)
    private List<TechStack> preferStacks;

    @Nullable
    @ApiModelProperty(notes="등록된 회원 프로필 이미지")
    private ImageDto profileImage;

    @NonNull
    @ApiModelProperty(notes="회원 등록일", example="2022-12-12 09:12:17", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;

    @NonNull
    @ApiModelProperty(notes="회원 수정일", example="2022-12-12 09:12:17", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedTime;


    public static JoinMemberResponse toDto(Member member) {
        return JoinMemberResponse.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .createdTime(member.getCreatedTime())
                .modifiedTime(member.getModifiedTime())
                .build();
    }

    public JoinMemberResponse profileImage(ImageDto imageDto) {
        this.profileImage = imageDto;
        return this;
    }

    public JoinMemberResponse preferStacks(List<TechStack> stacks) {
        this.preferStacks = stacks;
        return this;
    }
}
