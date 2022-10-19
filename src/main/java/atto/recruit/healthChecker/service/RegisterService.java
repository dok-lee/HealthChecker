package atto.recruit.healthChecker.service;

import atto.recruit.healthChecker.domain.Host;
import atto.recruit.healthChecker.domain.RegHost;
import atto.recruit.healthChecker.repository.HostRepository;

import javax.transaction.Transactional;
import java.net.Inet4Address;
import java.util.List;
import java.util.Optional;

@Transactional
public class RegisterService {

    private final HostRepository hostRepository;

    public RegisterService(HostRepository hostRepository) {
        this.hostRepository = hostRepository;
    }

    public void join(RegHost host) {
        validateDuplicatedIp(host.getIp());
        validateDuplicatedName(host.getName());
        validateOverflowedHostNum();

        hostRepository.save(host);
    }

    public List<Host> findAll() {
        return hostRepository.findAll();
    }

    public Optional<Host> findOne(Inet4Address hostIp) {
        return hostRepository.findByIp(hostIp);
    }

    public Optional<Host> findOne(String name) {
        return hostRepository.findByName(name);
    }

    public Host updateOne(RegHost host) {
        validateNotExistIp(host.getIp());
        validateDuplicatedName(host.getName());
        return hostRepository.update(host);
    }

    public Optional<Host> deleteOne(Inet4Address ip) {
        validateNotExistIp(ip);
        return hostRepository.deleteByIp(ip);
    }

    public Optional<Host> deleteOne(String name) {
        validateNotExistName(name);
        return hostRepository.deleteByName(name);
    }

    public void clear() {
        hostRepository.clear();
    }

    private void validateDuplicatedIp(Inet4Address ip) {
        hostRepository.findByIp(ip)
                .ifPresent(h -> {
                    throw new IllegalStateException("Inserted IP address is already exist");
                });
    }

    private void validateDuplicatedName(String name) {
        hostRepository.findByName(name)
                .ifPresent(h -> {
                    throw new IllegalStateException("Inserted name is already exist");
                });
    }

    private void validateOverflowedHostNum() {
        if (hostRepository.getSize() > 100) {
            throw new IllegalStateException("Registered hosts can't be over than 100");
        }
    }

    private void validateNotExistIp(Inet4Address ip) {
        Optional<Host> host = hostRepository.findByIp(ip);

        if (host.isEmpty()) {
            throw new IllegalStateException("Inserted IP address is not exist");
        }
    }

    private void validateNotExistName(String name) {
        Optional<Host> host = hostRepository.findByName(name);

        if (host.isEmpty()) {
            throw new IllegalStateException("Inserted name is not exist");
        }
    }
}

