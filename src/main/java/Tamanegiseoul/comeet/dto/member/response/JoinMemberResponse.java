package Tamanegiseoul.comeet.dto.member.response;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class JoinMemberResponse {
    private Long memberId;
    private String nickname;
    private String email;
    private List<TechStack> preferStacks;
    private ImageDto profileImage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
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
