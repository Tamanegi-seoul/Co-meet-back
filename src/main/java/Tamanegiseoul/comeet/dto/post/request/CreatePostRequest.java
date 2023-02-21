package Tamanegiseoul.comeet.dto.post.request;

import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.GroupType;
import Tamanegiseoul.comeet.domain.enums.RecruitStatus;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@NoArgsConstructor
public class CreatePostRequest {
    @NonNull
    @ApiModelProperty(notes="등록할 게시글 제목", example="자바 알고리즘 스터디 모집", required = true)
    private String title;

    @NonNull
    @ApiModelProperty(notes="등록할 게시글 내용", example="백준 문제풀이 같이 하실분 구해요.", required = true)
    private String content;

    @NonNull
    @ApiModelProperty(notes="등록할 게시글의 모집 그룹 타입", example="STUDY", required = true)
    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    @NonNull
    @ApiModelProperty(notes="등록할 게시글의 모집 정원", example="6", required = true)
    private Long recruitCapacity;
    @NonNull
    @ApiModelProperty(notes="등록할 게시글의 모집 그룹 연락 수단", example="KAKAO_OPEN_CHAT", required = true)
    @Enumerated(EnumType.STRING)
    private ContactType contactType;
    @NonNull
    @ApiModelProperty(notes="등록할 게시글의 모집 그룹 연락처", example="https://open.kakao.com/o/gNqZKIle", required = true)
    private String contact;

    @NonNull
    @ApiModelProperty(notes="등록할 게시글의 모집 그룹 운영 방식", example="ture | false", required = true)
    private Boolean remote;
    @NonNull
    @ApiModelProperty(notes="등록할 게시글의 모집 그룹 활동 시작일", example="2021-08-15", required = true)
    private LocalDate startDate;
    @NonNull
    @ApiModelProperty(notes="등록할 게시글의 모집 그룹 활동기간(일)", example="14", required = true)
    private Long expectedTerm;
    @NonNull
    @ApiModelProperty(notes="등록할 게시글의 작성자 ID", example="14", required = true)
    private Long posterId;
    @NonNull
    @ApiModelProperty(notes="등록할 게시글의 모집 그룹의 기술스택", example="JAVA, SPRING", required = true)
    @Enumerated(EnumType.STRING)
    private List<TechStack> designatedStacks;

    @Builder
    public CreatePostRequest(String title, String content, GroupType groupType, Long recruitCapacity, ContactType contactType, String contact, Boolean remote, LocalDate startDate, Long expectedTerm, Long posterId, List<TechStack> designatedStacks) {
        this.title = title;
        this.content = content;
        this.groupType = groupType;
        this.recruitCapacity = recruitCapacity;
        this.contactType = contactType;
        this.contact = contact;
        this.remote = remote;
        this.startDate = startDate;
        this.expectedTerm = expectedTerm;
        this.posterId = posterId;
        this.designatedStacks = designatedStacks;
    }
}
