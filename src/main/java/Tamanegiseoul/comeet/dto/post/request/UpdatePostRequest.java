package Tamanegiseoul.comeet.dto.post.request;

import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.RecruitStatus;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
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
    private Long postId;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private RecruitStatus recruitStatus;
    private Long recruitCapacity;
    @Enumerated(EnumType.STRING)
    private ContactType contactType;
    private String contact;
    private LocalDate startDate;
    private Long expectedTerm;
    @Enumerated(EnumType.STRING)
    private List<TechStack> designatedStacks;


}
