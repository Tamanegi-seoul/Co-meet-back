package Tamanegiseoul.comeet.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@RequiredArgsConstructor
public class StackRelationService {

    @PersistenceContext
    EntityManager em;


}
