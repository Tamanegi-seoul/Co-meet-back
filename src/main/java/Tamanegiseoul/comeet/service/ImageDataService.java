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
        log.info("[ImageDataService:uploadImage] upload image for {} member with file {}", member.getNickname(), file.getName());
        ImageData imageData = imageDataRepository.save(ImageData.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .imageData(ImageUtil.compressImage(file.getBytes()))
                .owner(member)
                .build());
        // entity relation convenience method
        member.addProfileImage(imageData);

        return ImageDto.toDto(imageData);
    }

    @Transactional
    public ImageDto updateImage(Member updatedMember, MultipartFile updatedFile) throws IOException {
        log.info("[ImageDataService:updateImage] update image for {} member with file {}", updatedMember.getNickname(), updatedFile.getName());
        ImageData dbImage = imageDataRepository.findByMemberId(updatedMember.getMemberId());
        if(dbImage != null) {
            log.info("[ImageDataService:updateImage] found {} profile image", updatedMember.getNickname());
            dbImage.updateImageData(updatedFile);
            log.info("[ImageDataService:updateImage] database profile image replace with {}", updatedFile.getName());
            em.flush();
            em.clear();
        } else {
            log.info("[ImageDataService:updateImage] {} profile image not exists", updatedMember.getNickname());
        }

        return ImageDto.toDto(dbImage);
    }

    @Transactional
    public void removeImage(Member targetMember) {
        ImageData findImage = imageDataRepository.findByMemberId(targetMember.getMemberId());
        if(findImage != null) {
            em.remove(findImage);
            log.info("[ImageDataService:removeImage] member {}'s profile image has been removed.", targetMember.getNickname());
        } else {
            log.info("[ImageDataService:removeImage] member {}'s profile image has not been registered yet.", targetMember.getNickname());
        }
    }

    @Transactional(readOnly = true)
    public ImageDto findImageByMemberId(Long memberId) {
        ImageData dbImage = imageDataRepository.findByMemberId(memberId);

        if(dbImage == null) {
            log.info("[ImageDataService:findImageByMemberId] member with member id {} has not profile image", memberId);
            //throw new ResourceNotFoundException("ImageData", "owner:memberId", memberId);
            return null;
        }
        log.info("[ImageDataService:findImageByMemberId] member with member id has profile image {}", dbImage.getFileName());

        return ImageDto.toDto(dbImage);
    }

}
