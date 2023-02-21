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
import io.swagger.annotations.ApiModelProperty;
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
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class UpdatePostRequest {
    @NonNull
    @ApiModelProperty(notes="수정할 게시글 ID", example="3", required = true)
    private Long postId;
    @NonNull
    @ApiModelProperty(notes="수정할 게시글 제목", example="(수정)자바 알고리즘 스터디 구인", required = true)
    private String title;
    @NonNull
    @ApiModelProperty(notes="수정할 게시글의 그룹 타입", example="STUDY | PROJECT", required = true)
    @Enumerated(EnumType.STRING)
    private GroupType groupType;
    @NonNull
    @ApiModelProperty(notes="수정할 게시글의 내용", example="서울지역 자바 알고리즘 스터디원 모집", required = true)
    private String content;
    @NonNull
    @ApiModelProperty(notes="수정할 게시글의 그룹 모집 상태", example="DONE", required = true)
    @Enumerated(EnumType.STRING)
    private RecruitStatus recruitStatus;
    @NonNull
    @ApiModelProperty(notes="수정할 게시글의 그룹 모집 인원", example="4", required = true)
    private Long recruitCapacity;
    @NonNull
    @ApiModelProperty(notes="수정할 게시글의 그룹 연락 수단", example="GOOGLE_FORM", required = true)
    @Enumerated(EnumType.STRING)
    private ContactType contactType;
    @NonNull
    @ApiModelProperty(notes="수정할 게시글의 그룹 연락처", example="form.google.com/die3IK39KJL", required = true)
    private String contact;
    @NonNull
    @ApiModelProperty(notes="수정할 게시글의 그룹 온라인 진행 여부", example="true", required = true)
    private Boolean remote;
    @NonNull
    @ApiModelProperty(notes="수정할 게시글의 그룹 활동 시작일", example="2022-01-10", required = true)
    private LocalDate startDate;
    @NonNull
    @ApiModelProperty(notes="수정할 게시글의 그룹 활동 기간(일)", example="60", required = true)
    private Long expectedTerm;
    @NonNull
    @ApiModelProperty(notes="수정할 게시글의 그룹 기술스택", example="JAVA, SPRING", required = true)
    @Enumerated(EnumType.STRING)
    private List<TechStack> designatedStacks;

    @Builder
    public UpdatePostRequest(Long postId, String title, GroupType groupType, String content, RecruitStatus recruitStatus, Long recruitCapacity, ContactType contactType, String contact, Boolean remote, LocalDate startDate, Long expectedTerm, List<TechStack> designatedStacks) {
        this.postId = postId;
        this.title = title;
        this.groupType = groupType;
        this.content = content;
        this.recruitStatus = recruitStatus;
        this.recruitCapacity = recruitCapacity;
        this.contactType = contactType;
        this.contact = contact;
        this.remote = remote;
        this.startDate = startDate;
        this.expectedTerm = expectedTerm;
        this.designatedStacks = designatedStacks;
    }
}
