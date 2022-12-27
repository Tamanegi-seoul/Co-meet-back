package Tamanegiseoul.comeet.repository;

import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.ImageData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ImageDataRepository {
    private final EntityManager em;

    public ImageData save(ImageData file) {
        em.persist(file);
        return file;
    }

    public ImageData findOne(Long id) {
        return em.find(ImageData.class, id);
    }

    public ImageData findByMemberId(Long memberId) {
        return em.createQuery("select i from ImageData i where i.owner.memberId = :memberId", ImageData.class)
                .setParameter("memberId", memberId)
                .getResultStream().findFirst().orElse(null);
    }

    public int removeImageByMemberId(Long memberId) {
        return em.createQuery("delete from ImageData i where i.owner.memberId = :memberId")
                .setParameter("memberId", memberId)
                .executeUpdate();
    }


}
