package project.profileservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@RequiredArgsConstructor
public class BadgeRepository {

    @PersistenceContext
    private final EntityManager em;


}
