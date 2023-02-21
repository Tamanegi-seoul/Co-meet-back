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

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class UpdateMemberResponse {
    @NonNull
    @ApiModelProperty(notes="수정한 회원 ID", example="3", required=true)
    private Long memberId;
    @NonNull
    @ApiModelProperty(notes="수정한 회원 닉네임", example="craig.w", required=true)
    private String nickname;
    @NonNull
    @ApiModelProperty(notes="수정한 회원의 기존 이메일", example="craig.w@gmail.com", required=true)
    private String email;
    @NonNull
    @ApiModelProperty(notes="수정한 회원의 선호 기술스택", example="3", required=true)
    private List<TechStack> preferStacks;
    @NonNull
    @ApiModelProperty(notes="수정한 회원 등록일", example="2022-12-12 09:12:17", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
    @NonNull
    @ApiModelProperty(notes="수정한 회원 수정일", example="2022-12-12 09:12:17", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedTime;
    @Nullable
    @ApiModelProperty(notes="수정한 회원 프로필 이미지")
    private ImageDto profileImage;

    public static UpdateMemberResponse toDto(Member member, ImageDto image) {
        return UpdateMemberResponse.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .preferStacks(member.exportPreferStack())
                .createdTime(member.getCreatedTime())
                .modifiedTime(member.getModifiedTime())
                .profileImage(image)
                .build();
    }
}
