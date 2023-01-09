package Tamanegiseoul.comeet.dto.member.response;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedTime;
    private ImageDto profileImage;
}
