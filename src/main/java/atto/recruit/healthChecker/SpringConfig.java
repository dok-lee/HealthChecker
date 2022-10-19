package atto.recruit.healthChecker;

import atto.recruit.healthChecker.repository.*;
import atto.recruit.healthChecker.service.HealthCheckService;
import atto.recruit.healthChecker.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class SpringConfig {

    private EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public RegisterService registerService() {
        return new RegisterService(hostRepository());
    }

    @Bean
    public HealthCheckService healthCheckService() {
        return new HealthCheckService(hostRepository(), healthRepository(), healthCheckRunnable());
    }

    @Bean
    public HostRepository hostRepository() {
        return new JpaHostRepository(em);
    }

    @Bean
    public HealthRepository healthRepository() {
        return new JpaHealthRepository(em);
    }

    @Bean
    public HealthCheckRunnable healthCheckRunnable() {
        return new HealthCheckRunnable(em);
    }
}
