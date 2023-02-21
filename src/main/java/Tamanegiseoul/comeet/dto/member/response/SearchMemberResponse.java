package Tamanegiseoul.comeet.dto.member.response;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@NoArgsConstructor
public class SearchMemberResponse {

    @NonNull
    @ApiModelProperty(notes="조회된 회원 ID", example="24", required=true)
    private Long memberId;

    @NonNull
    @ApiModelProperty(notes="조회된 회원 이메일", example="j.doe@gmail.com", required=true)
    private String email;

    @NonNull
    @ApiModelProperty(notes="조회된 회원 닉네임", example="John Doe", required=true)
    private String nickname;

    @NonNull
    @ApiModelProperty(notes="조회된 회원 선호 기술스택", example="JAVA, SPRING", required=true)
    private List<TechStack> preferStacks;

    @NonNull
    @ApiModelProperty(notes="회원 등록일", example="2022-12-12 09:12:17", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;

    @NonNull
    @ApiModelProperty(notes="회원 수정일", example="2022-12-12 09:12:17", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedTime;

    @Nullable
    @ApiModelProperty(notes="조회된 회원 프로필 이미지")
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

    public static SearchMemberResponse toDto(Member member, ImageDto image, List<StackRelation> stacks) {
        return SearchMemberResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .preferStacks(preferStacks(stacks))
                .createdTime(member.getCreatedTime())
                .modifiedTime(member.getModifiedTime())
                .profileImage(image)
                .build();
    }

    private static List<TechStack> preferStacks(List<StackRelation> list) {
        List<TechStack> findStacks = new ArrayList<>();
        for(StackRelation sr : list) {
            findStacks.add(sr.getTechStack());
        }
        return findStacks;
    }
}
