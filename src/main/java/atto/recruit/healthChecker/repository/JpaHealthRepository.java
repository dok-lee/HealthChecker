package atto.recruit.healthChecker.repository;

import atto.recruit.healthChecker.domain.Health;
import atto.recruit.healthChecker.domain.Host;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class JpaHealthRepository implements HealthRepository {

    private final EntityManager em;

    public JpaHealthRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Health health) {
        LocalDateTime rawNowDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNowDateTime = dateTimeFormatter.format(rawNowDateTime);

        health.setConnection(true);
        health.setUpdatedTime(formattedNowDateTime);

        em.merge(health);
        em.flush();
    }

    @Override
    public Health findById(Long id) {
        return em.find(Health.class, id);
    }

    @Override
    public List<Health> findAll() {
        return em.createQuery("select h from Health h", Health.class).getResultList();
    }

    @Override
    public Health update(Health health) {
        return em.merge(health);
    }

    @Override
    public Optional<Health> deleteById(Long id) {
        em.remove(em.find(Health.class, id));
        return Optional.empty();
    }

    @Override
    public void clear() {
        em.createQuery("delete from Health h").executeUpdate();
    }
}
