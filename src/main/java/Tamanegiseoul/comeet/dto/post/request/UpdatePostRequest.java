package Tamanegiseoul.comeet.dto.post.request;

import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.GroupType;
import Tamanegiseoul.comeet.domain.enums.RecruitStatus;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor // for implementing test code
@NoArgsConstructor // for implementing test code
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdatePostRequest {
    @Schema(description = "수정할 포스트 ID", example = "3")
    private Long postId;
    @Schema(description = "수정할 포스트 제목", example = "(수정)자바 알고리즘 스터디 구인")
    private String title;
    @Schema(description = "수정할 포스트의 모집 그룹 타입", example = "PROJECT")
    @Enumerated(EnumType.STRING)
    private GroupType groupType;
    @Schema(description = "수정할 포스트 내용", example = "서울지역 자바 알고리즘 스터디원 모집해요")
    private String content;
    @Schema(description = "모집 상태", example = "DONE")
    @Enumerated(EnumType.STRING)
    private RecruitStatus recruitStatus;
    @Schema(description = "모집 인원", example = "4")
    private Long recruitCapacity;
    @Schema(description = "연락 수단", example = "GOOGLE_FORM")
    @Enumerated(EnumType.STRING)
    private ContactType contactType;
    @Schema(description = "연락처", example = "form.google.com/die3IK39KJL")
    private String contact;
    @Schema(description = "온라인 진행 여부", example = "true")
    private Boolean remote;
    @Schema(description = "스터디 시작일", example = "2022-01-10")
    private LocalDate startDate;
    @Schema(description = "스터디 진행기간", example = "60")
    private Long expectedTerm;
    @Schema(description = "사용될 또는 스터디할 기술스택", example = "JAVA, JAVA_SCRIPT")
    @Enumerated(EnumType.STRING)
    private List<TechStack> designatedStacks;


}
