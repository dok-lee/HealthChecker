package atto.recruit.healthChecker.repository;

import atto.recruit.healthChecker.domain.Host;
import atto.recruit.healthChecker.domain.RegHost;

import javax.persistence.EntityManager;
import java.net.Inet4Address;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class JpaHostRepository implements HostRepository {

    private static long sequence = 0L;
    private final EntityManager em;

    public JpaHostRepository(EntityManager em) {
        this.em = em;
    }
    @Override
    public void save(RegHost regHost) {
        LocalDateTime rawNowDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNowDateTime = dateTimeFormatter.format(rawNowDateTime);

        Host host = new Host();
        // TODO: sequence id management (if over long max)
        host.setId(++sequence);
        host.setIp(regHost.getIp());
        host.setName(regHost.getName());
        host.setRegisteredTime(formattedNowDateTime);
        host.setUpdatedTime(formattedNowDateTime);

        em.merge(host);
        em.flush();
    }

    @Override
    public Optional<Host> findByIp(Inet4Address ip) {
        return em.createQuery("select h from Host h where h.ip = :ip", Host.class)
                .setParameter("ip", ip)
                .getResultList().stream().findAny();
    }

    @Override
    public Optional<Host> findByName(String name) {
        return em.createQuery("select h from Host h where h.name = :name", Host.class)
                .setParameter("name", name)
                .getResultList().stream().findAny();

    }
    @Override
    public List<Host> findAll() {
        return em.createQuery("select h from Host h", Host.class).getResultList();
    }

    @Override
    public int getSize() {
        return em.createQuery("select h from Host h", Host.class).getResultList().size();
    }

    @Override
    public Optional<Host> deleteByIp(Inet4Address ip) {
        Optional<Host> existHost = em.createQuery("select h from Host h where h.ip = :ip", Host.class)
                .setParameter("ip", ip)
                .getResultList().stream().findAny();

        existHost.ifPresent(host -> em.remove((existHost.get())));
        return Optional.empty();
    }

    @Override
    public Optional<Host> deleteByName(String name) {
        Optional<Host> existHost = em.createQuery("select h from Host h where h.name = :name", Host.class)
                .setParameter("name", name)
                .getResultList().stream().findAny();

        existHost.ifPresent(host -> em.remove((existHost.get())));

        return Optional.empty();
    }

    @Override
    public Host update(RegHost regHost) {
        LocalDateTime rawNowDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNowDateTime = dateTimeFormatter.format(rawNowDateTime);

        Optional<Host> existHost = em.createQuery("select h from Host h where h.ip = :ip", Host.class)
                .setParameter("ip", regHost.getIp())
                .getResultList().stream().findAny();

        Host host = new Host();

        if (existHost.isPresent()) {
            host.setId(existHost.get().getId());
            host.setRegisteredTime(existHost.get().getRegisteredTime());

            host.setIp(regHost.getIp());
            host.setName(regHost.getName());
            host.setUpdatedTime(formattedNowDateTime);
        }

        return em.merge(host);
    }

    @Override
    public void clear() {
        em.createQuery("delete from Host h").executeUpdate();
    }
}
