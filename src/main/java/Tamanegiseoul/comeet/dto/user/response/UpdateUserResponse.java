package Tamanegiseoul.comeet.dto.user.response;

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
public class UpdateUserResponse {
    private Long userId;
    private String nickname;
    private String email;
    private List<TechStack> preferredStacks;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private ImageDto profileImage;
}
