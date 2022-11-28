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

    public void save(ImageData file) {
        em.persist(file);
    }

    public ImageData findOne(Long id) {
        return em.find(ImageData.class, id);
    }

    public Optional<ImageData> findByName(String name) {
        return Optional.ofNullable(em.createQuery("select i from ImageData i where i.name = :name", ImageData.class)
                .setParameter("name", name)
                .getSingleResult());
    }
}
