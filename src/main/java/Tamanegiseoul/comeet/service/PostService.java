package Tamanegiseoul.comeet.service;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.dto.post.request.UpdatePostRequest;
import Tamanegiseoul.comeet.repository.CommentRepository;
import Tamanegiseoul.comeet.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final StackRelationService stackRelationService;

    @PersistenceContext
    EntityManager em;

    @Transactional
    public Long registerPost(Posts post) {
        postRepository.save(post);
        return post.getId();
    }

    /***********************
     * UPDATE POST METHODS *
     ***********************/

    @Transactional
    public void updatePost(Long id, UpdatePostRequest updatedPost) {
        Posts findPost = postRepository.findOne(id);
        findPost.updatePost(updatedPost);
        findPost.initDesignateStack();
        findPost.updateDesignateStack(updatedPost.getStacks());
    }

    /***********************
     * SEARCH POST METHODS *
     ***********************/

    @Transactional(readOnly = true)
    public List<Posts> findAll() {
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Posts> findPostByUserId(Long userId) {
        return postRepository.findPostByUserId(userId);
    }


    /***********************
     * POST UPDATE METHODS *
     ***********************/
    @Transactional
    public void updateDesignateStacks(Long postId, TechStack...techStacks) {
        Posts findPost = postRepository.findOne(postId);
        findPost.initDesignateStack();
        for(TechStack stack : techStacks) {
            findPost.addDesignateStack(stack);
        }
    }

}
