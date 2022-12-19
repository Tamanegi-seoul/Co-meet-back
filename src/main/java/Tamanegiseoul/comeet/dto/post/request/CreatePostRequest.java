package Tamanegiseoul.comeet.dto.post.request;

import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.GroupType;
import Tamanegiseoul.comeet.domain.enums.RecruitStatus;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreatePostRequest {
    @Schema(description = "게시글 제목", example = "자바 알고리즘 스터디 모집")
    private String title;
    @Schema(description = "게시글 내용", example = "백준문제풀이 같이하실분 구해요.")
    private String content;
    @Schema(description = "그룹 타입", example = "STUDY")
    @Enumerated(EnumType.STRING)
    private GroupType groupType;
    @Schema(description = "모집 정원", example = "6")
    private Long recruitCapacity;
    @Schema(description = "연락 수단", example = "KAKAO_OPEN_CHAT")
    @Enumerated(EnumType.STRING)
    private ContactType contactType;
    @Schema(description = "연락처", example = "https://open.kakao.com/o/gNqZKIle")
    private String contact;
    @Schema(description = "스터디 온라인진행 여부", example = "TRUE")
    private Boolean remote;
    @Schema(description = "스터디 시작일", example = "2021-08-15")
    private LocalDate startDate;
    @Schema(description = "스터디 운영기간(일)", example = "14")
    private Long expectedTerm;
    @Schema(description = "작성자 ID", example = "5")
    private Long posterId;
    @Schema(description = "사용될 또는 스터디할 기술스택", example = "JAVA, JAVA_SCRIPT")
    @Enumerated(EnumType.STRING)
    private List<TechStack> designatedStacks;
}
