package atto.recruit.healthChecker.repository;

import atto.recruit.healthChecker.domain.Health;
import atto.recruit.healthChecker.domain.Host;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

public class HealthCheckRunnable implements Runnable {

    private final EntityManager em;

    public HealthCheckRunnable(EntityManager em) {
        this.em = em;
    }

    private static int sequence = 0;

    @Override
    public void run() {

        List<Host> Hosts = em.createQuery("select h from Host h", Host.class).getResultList();

        boolean connectivity = false;

        for (Host host : Hosts) {

            if (host.getId() / 10 != sequence) {
                continue;
            }
            try {
                connectivity = host.getIp().isReachable(1000);
            } catch (IOException e) {
                connectivity = false;
            }

            LocalDateTime rawNowDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedNowDateTime = dateTimeFormatter.format(rawNowDateTime);

            Health health = new Health();
            health.setId(host.getId());
            health.setConnection(connectivity);
            health.setUpdatedTime(formattedNowDateTime);

            em.merge(health);
            em.flush();
        }

        ++sequence;
        if (sequence > 10) {
            sequence = 0;
        }
    }
}
