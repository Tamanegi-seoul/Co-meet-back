package Tamanegiseoul.comeet.dto.user.response;

import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateUserResponse {
    private Long userId;
    private String nickname;
    private String email;
    private List<TechStack> preferredStacks;
}
