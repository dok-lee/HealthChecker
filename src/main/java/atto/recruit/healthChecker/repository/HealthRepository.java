package atto.recruit.healthChecker.repository;

import atto.recruit.healthChecker.domain.Health;

import java.util.List;
import java.util.Optional;

public interface HealthRepository {
    void save(Health health);
    Health findById(Long id);
    List<Health> findAll();
    Health update(Health health);
    Optional<Health> deleteById(Long id);
    void clear();
}
