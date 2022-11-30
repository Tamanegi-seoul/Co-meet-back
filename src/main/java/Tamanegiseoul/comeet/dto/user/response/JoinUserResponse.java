package Tamanegiseoul.comeet.dto.user.response;

import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JoinUserResponse {
    private Long userId;
    private String nickname;
    private String email;
    private List<TechStack> preferStacks;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private ImageDto profileImage;
}
