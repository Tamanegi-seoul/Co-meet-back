package Tamanegiseoul.comeet.service;
import Tamanegiseoul.comeet.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@RequiredArgsConstructor
public class PostService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final StackRelationService stackRelationService;

    @PersistenceContext
    EntityManager em;
}
