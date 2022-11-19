package Tamanegiseoul.comeet.dto.user.response;

import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.dto.user.request.UpdateUserRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateUserResponse {
    private Long userId;
    private String nickname;
    private String email;
    private List<TechStack> preferredStacks;
}
