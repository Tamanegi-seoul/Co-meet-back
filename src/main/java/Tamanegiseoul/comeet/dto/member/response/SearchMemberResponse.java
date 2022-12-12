package Tamanegiseoul.comeet.dto.member.response;

import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchMemberResponse {
    private Long memberId;
    private String email;
    private String nickname;
    private List<TechStack> preferStacks;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private ImageDto profileImage;
}
