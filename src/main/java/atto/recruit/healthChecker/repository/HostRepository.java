package atto.recruit.healthChecker.repository;

import atto.recruit.healthChecker.domain.Host;
import atto.recruit.healthChecker.domain.RegHost;

import java.net.Inet4Address;
import java.util.List;
import java.util.Optional;

public interface HostRepository {
    void save(RegHost regHost);
    Optional<Host> findByIp(Inet4Address ip);
    Optional<Host> findByName(String name);
    List<Host> findAll();
    int getSize();
    Optional<Host> deleteByIp(Inet4Address ip);
    Optional<Host> deleteByName(String name);
    Host update(RegHost regHost);
    void clear();
}
