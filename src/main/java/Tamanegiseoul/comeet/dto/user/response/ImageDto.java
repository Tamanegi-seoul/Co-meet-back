package Tamanegiseoul.comeet.dto.user.response;

import Tamanegiseoul.comeet.domain.ImageData;
import Tamanegiseoul.comeet.utils.ImageUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImageDto {
    private Long imageId;
    private String fileName;
    private String fileType;
    private byte[] imageData;

    public static ImageDto toDto(ImageData file) {
        return ImageDto.builder()
                .imageId(file.getImageId())
                .fileName(file.getFileName())
                .fileType(file.getFileType())
                .imageData(ImageUtil.decompressImage(file.getImageData()))
                .build();
    }
}
