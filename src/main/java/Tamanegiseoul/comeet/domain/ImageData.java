package Tamanegiseoul.comeet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Builder
@Entity
@Getter
@Table(name = "image_data")
@NoArgsConstructor
@AllArgsConstructor
public class ImageData {
    @Id @GeneratedValue
    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @NotNull
    private String name;

    @NotNull
    private String type;

    @Lob
    @Column(name = "image_data", length = 1000)
    private byte[] imageData;
}
