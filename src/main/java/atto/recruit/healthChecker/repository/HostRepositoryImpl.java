package atto.recruit.healthChecker.repository;

import atto.recruit.healthChecker.domain.Host;
import atto.recruit.healthChecker.domain.RegHost;

import java.net.Inet4Address;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HostRepositoryImpl implements HostRepository {

    private static final Map<Inet4Address, Host> storedHosts = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public void save(RegHost reghost) {
        LocalDateTime rawNowDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNowDateTime = dateTimeFormatter.format(rawNowDateTime);

        Host host = new Host();
        host.setId(++sequence);
        host.setIp(reghost.getIp());
        host.setName(reghost.getName());
        host.setRegisteredTime(formattedNowDateTime);
        host.setUpdatedTime(formattedNowDateTime);

        storedHosts.put(host.getIp(), host);
    }

    @Override
    public Optional<Host> findByIp(Inet4Address ip) {
        return Optional.ofNullable(storedHosts.get(ip));
    }

    @Override
    public Optional<Host> findByName(String name) {
        return storedHosts.values().stream()
                .filter(host -> host.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Host> findAll() {
        return new ArrayList<>(storedHosts.values());
    }

    @Override
    public int getSize() {
        return storedHosts.size();
    }

    @Override
    public Optional<Host> deleteByIp(Inet4Address ip) {
        Host deletingHost = storedHosts.get(ip);

        if (deletingHost != null) {
            storedHosts.remove(ip);
        }

        return Optional.ofNullable(deletingHost);
    }

    @Override
    public Optional<Host> deleteByName(String name) {
        Optional<Host> deletingHost = storedHosts.values().stream()
                    .filter(host -> host.getName().equals(name))
                    .findAny();

        deletingHost.ifPresent(host -> storedHosts.remove(host.getIp()));

        return deletingHost;
    }

    @Override
    public Host update(RegHost regHost) {
        Host oldHost = storedHosts.get(regHost.getIp());

        if (oldHost != null) {
            LocalDateTime rawNowDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedNowDateTime = dateTimeFormatter.format(rawNowDateTime);

            Host newHost;
            newHost = oldHost;

            newHost.setName(regHost.getName());
            newHost.setUpdatedTime(formattedNowDateTime);

            storedHosts.remove(oldHost.getIp());
            storedHosts.put(newHost.getIp(), newHost);
        } else {
            return null;
        }

        return oldHost;
    }

    @Override
    public void clear() {
        storedHosts.clear();
    }
}
