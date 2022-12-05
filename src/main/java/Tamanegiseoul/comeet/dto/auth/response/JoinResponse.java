package Tamanegiseoul.comeet.dto.auth.response;

import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.dto.user.response.ImageDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JoinResponse {
    private Long userId;
    private String nickname;
    private String email;
    private List<TechStack> preferStacks;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private ImageDto profileImage;
}
