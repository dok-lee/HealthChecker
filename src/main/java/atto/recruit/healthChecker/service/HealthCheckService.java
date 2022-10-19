package atto.recruit.healthChecker.service;

import atto.recruit.healthChecker.domain.Health;
import atto.recruit.healthChecker.repository.HealthCheckRunnable;
import atto.recruit.healthChecker.repository.HealthRepository;
import atto.recruit.healthChecker.repository.HostRepository;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.net.Inet4Address;
import java.util.List;
import java.util.Optional;

@Transactional
public class HealthCheckService {

    private final HealthRepository healthRepository;
    private final HostRepository hostRepository;
    private final HealthCheckRunnable healthCheckRunnable;

    public HealthCheckService(HostRepository hostRepository, HealthRepository healthRepository, HealthCheckRunnable healthCheckRunnable) {
        this.hostRepository = hostRepository;
        this.healthRepository = healthRepository;
        this.healthCheckRunnable = healthCheckRunnable;
    }

    public void join(Inet4Address ip, Health health) {
        Long id = hostRepository.findByIp(ip).get().getId();
        health.setId(id);
        healthRepository.save(health);
    }

    public List<Health> findAll() {
        return healthRepository.findAll();
    }

    public Health findOne(Inet4Address ip) {
        Long id = hostRepository.findByIp(ip).get().getId();
        return healthRepository.findById(id);
    }

    public Health findOne(String name) {
        Long id = hostRepository.findByName(name).get().getId();
        return healthRepository.findById(id);
    }

    public Optional<Health> deleteOne(Inet4Address ip) {
        Long id = hostRepository.findByIp(ip).get().getId();
        return healthRepository.deleteById(id);
    }

    public Optional<Health> deleteOne(String name) {
        Long id = hostRepository.findByName(name).get().getId();
        return healthRepository.deleteById(id);
    }

    public void clear() {
        healthRepository.clear();
    }

    @Scheduled(fixedDelay = 1000)
    public void connectionCheck() {
        healthCheckRunnable.run();
    }
}