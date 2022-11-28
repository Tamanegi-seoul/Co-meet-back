package Tamanegiseoul.comeet.service;

import Tamanegiseoul.comeet.domain.ImageData;
import Tamanegiseoul.comeet.dto.user.response.ImageUploadResponse;
import Tamanegiseoul.comeet.repository.ImageDataRepository;
import Tamanegiseoul.comeet.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.Optional;

@Service @Slf4j
@RequiredArgsConstructor
public class ImageDataService {

    private final ImageDataRepository imageDataRepository;

    @PersistenceContext
    EntityManager em;

    @Transactional
    public ImageUploadResponse uploadImage(MultipartFile file) throws IOException {
        imageDataRepository.save(ImageData.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .imageData(ImageUtil.compressImage(file.getBytes()))
                .build());

        return new ImageUploadResponse("Image uploaded Successfully: " + file.getOriginalFilename());
    }

    @Transactional
    public ImageData getInfoByImageByName(String name) {
        Optional<ImageData> dbImage = imageDataRepository.findByName(name);

        return ImageData.builder()
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .imageData(ImageUtil.decompressImage(dbImage.get().getImageData()))
                .build();
    }

    @Transactional
    public byte[] getImage(String name) {
        Optional<ImageData> dbImage = imageDataRepository.findByName(name);
        byte[] image = ImageUtil.decompressImage(dbImage.get().getImageData());
        return image;
    }
}
