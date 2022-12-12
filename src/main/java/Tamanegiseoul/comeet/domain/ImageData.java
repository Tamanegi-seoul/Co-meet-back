package Tamanegiseoul.comeet.domain;

import Tamanegiseoul.comeet.utils.ImageUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;

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
    private String fileName;

    @NotNull
    private String fileType;

    @OneToOne(fetch = FetchType.EAGER)
    private Member owner;

    @Lob
    @Column(name = "image_data", length = 1000)
    private byte[] imageData;

    public ImageData updateImageData(MultipartFile updatedFile) throws IOException {
        this.fileName = updatedFile.getOriginalFilename();
        this.fileType = updatedFile.getContentType();
        this.imageData = ImageUtil.compressImage(updatedFile.getBytes());

        return this;
    }
}
