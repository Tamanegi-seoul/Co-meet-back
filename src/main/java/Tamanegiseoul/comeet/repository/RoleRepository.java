package Tamanegiseoul.comeet.repository;

import Tamanegiseoul.comeet.domain.Role;
import Tamanegiseoul.comeet.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoleRepository {

    private final EntityManager em;

    public Role findByRoleName(String roleName) {
         return em.createQuery("select r from Role r where r.roleName = :roleName", Role.class)
                .setParameter("roleName", roleName)
                .getSingleResult();
    }


    public Role save(Role role) {
        em.persist(role);
        return role;
    }
}
