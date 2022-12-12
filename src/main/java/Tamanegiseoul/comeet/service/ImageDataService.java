package Tamanegiseoul.comeet.service;

import Tamanegiseoul.comeet.domain.ImageData;
import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.dto.member.response.ImageDto;
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

@Service @Slf4j
@RequiredArgsConstructor
public class ImageDataService {

    private final ImageDataRepository imageDataRepository;

    @PersistenceContext
    EntityManager em;

    @Transactional
    public ImageDto uploadImage(Member member, MultipartFile file) throws IOException {
        ImageData imageData = imageDataRepository.save(ImageData.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .imageData(ImageUtil.compressImage(file.getBytes()))
                .owner(member)
                .build());

        return ImageDto.toDto(imageData);
    }

    @Transactional
    public ImageDto updateImage(Member updatedMember, MultipartFile updatedFile) throws IOException {
        ImageData dbImage = imageDataRepository.findByMemberId(updatedMember.getMemberId());
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

    public ImageDto findImageByMemberId(Long memberId) {
        ImageData dbImage = imageDataRepository.findByMemberId(memberId);

        if(dbImage == null) {
            //throw new ResourceNotFoundException("ImageData", "owner:memberId", memberId);
            return null;
        }

        return ImageDto.toDto(dbImage);


    }
}
