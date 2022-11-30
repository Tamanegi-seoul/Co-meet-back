package Tamanegiseoul.comeet.service;

import Tamanegiseoul.comeet.domain.ImageData;
import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.user.response.ImageDto;
import Tamanegiseoul.comeet.dto.user.response.ImageUploadResponse;
import Tamanegiseoul.comeet.repository.ImageDataRepository;
import Tamanegiseoul.comeet.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ImageDto uploadImage(Users user, MultipartFile file) throws IOException {
        ImageData imageData = imageDataRepository.save(ImageData.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .imageData(ImageUtil.compressImage(file.getBytes()))
                .owner(user)
                .build());

        return ImageDto.toDto(imageData);
    }

    @Transactional
    public ImageDto updateImage(Users updatedUser, MultipartFile updatedFile) throws IOException {
        ImageData dbImage = imageDataRepository.findByUserId(updatedUser.getUserId());
        if(dbImage != null) {
            dbImage.updateImageData(updatedFile);
            em.flush();
            em.clear();
        }

        return ImageDto.toDto(dbImage);
    }

    public byte[] findImageDataByImageId(Long imageId) {
        ImageData findImageData = imageDataRepository.findOne(imageId);
        if(findImageData==null) {
            return null;
        }
        return ImageUtil.decompressImage(findImageData.getImageData());

    }

    public ImageDto findImageByUserId(Long userId) {
        ImageData dbImage = imageDataRepository.findByUserId(userId);

        if(dbImage == null) {
            //throw new ResourceNotFoundException("ImageData", "owner:userId", userId);
            return null;
        }

        return ImageDto.toDto(dbImage);


    }
}
