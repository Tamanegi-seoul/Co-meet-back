package Tamanegiseoul.comeet.dto.member.response;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@NoArgsConstructor
public class SearchMemberResponse {
    private Long memberId;
    private String email;
    private String nickname;
    private List<TechStack> preferStacks;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedTime;
    private ImageDto profileImage;

    @Builder
    public SearchMemberResponse(Long memberId, String email, String nickname, List<TechStack> preferStacks, LocalDateTime createdTime, LocalDateTime modifiedTime, ImageDto profileImage) {
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.preferStacks = preferStacks;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.profileImage = profileImage;
    }

    public static SearchMemberResponse toDto(Member member, ImageDto image, List<TechStack> stacks) {
        return SearchMemberResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .preferStacks(stacks)
                .createdTime(member.getCreatedTime())
                .modifiedTime(member.getModifiedTime())
                .profileImage(image)
                .build();
    }
}
