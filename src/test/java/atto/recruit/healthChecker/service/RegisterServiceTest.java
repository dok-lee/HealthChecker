package atto.recruit.healthChecker.service;

import atto.recruit.healthChecker.domain.Host;
import atto.recruit.healthChecker.domain.RegHost;
import atto.recruit.healthChecker.repository.HostRepository;
import atto.recruit.healthChecker.repository.HostRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

class RegisterServiceTest {

    private final HostRepository hostRepository = new HostRepositoryImpl();

    Inet4Address hostIp1;
    Inet4Address hostIp2;
    RegHost regHost1 = new RegHost();
    RegHost regHost2 = new RegHost();

    @BeforeEach
    public void setHosts() {
        try {
            hostIp1 = (Inet4Address) Inet4Address.getByName("10.10.10.10");
            hostIp2 = (Inet4Address) Inet4Address.getByName("20.20.20.20");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        regHost1.setIp(hostIp1);
        regHost1.setName("host1");

        regHost2.setIp(hostIp2);
        regHost2.setName("host2");
    }

    @AfterEach
    public void afterEach() {
        hostRepository.clear();
    }

    @Test
    void join_findAll() {
        validateDuplicatedIp(hostIp1);
        validateDuplicatedName(regHost1.getName());
        validateOverflowedHostNum();

        hostRepository.save(regHost1);

        List<Host> result = hostRepository.findAll();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void join_duplicatedIp() {
        hostRepository.save(regHost1);

        try {
            validateDuplicatedIp(hostIp1);
            hostRepository.save(regHost1);
        } catch (IllegalStateException e) {
            System.out.println(e.toString());
        }

        List<Host> result = hostRepository.findAll();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void join_duplicatedName() {
        hostRepository.save(regHost1);

        try {
            validateDuplicatedName(regHost1.getName());
            hostRepository.save(regHost1);
        } catch (IllegalStateException e) {
            System.out.println(e.toString());
        }

        List<Host> result = hostRepository.findAll();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void join_overFlow() {
        for (Integer i = 1; i <= 100; i++) {
            RegHost tempHost = new RegHost();

            Inet4Address tempIp;
            try {
                tempIp = (Inet4Address) Inet4Address.getByName(i.toString()+"."+i.toString()+"."+i.toString()+"."+i.toString());
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }

            tempHost.setIp(tempIp);
            tempHost.setName(i.toString());
            hostRepository.save(tempHost);
        }

        try {
            validateOverflowedHostNum();
            hostRepository.save(regHost2);
        } catch (IllegalStateException e) {
            System.out.println(e.toString());
        }

        List<Host> result = hostRepository.findAll();
        Assertions.assertEquals(100, result.size());
    }

    @Test
    void findByIp() {
        validateDuplicatedIp(hostIp1);
        validateDuplicatedName(regHost1.getName());
        validateOverflowedHostNum();

        hostRepository.save(regHost1);

        Optional<Host> result = hostRepository.findByIp(hostIp1);
        Assertions.assertEquals(regHost1.getName(), result.get().getName());

        System.out.println("Registered Time: " + result.get().getRegisteredTime());
        System.out.println("Updated Time: " + result.get().getUpdatedTime());
    }

    @Test
    void updateOne() {
        validateDuplicatedIp(hostIp1);
        validateDuplicatedName(regHost1.getName());
        validateOverflowedHostNum();

        hostRepository.save(regHost1);

        RegHost updateHost = new RegHost();
        updateHost.setIp(hostIp1);
        updateHost.setName(regHost2.getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        hostRepository.update(updateHost);

        Optional<Host> result = hostRepository.findByIp(hostIp1);
        Assertions.assertEquals(regHost2.getName(), result.get().getName());

        System.out.println("Registered Time: " + result.get().getRegisteredTime());
        System.out.println("Updated Time: " + result.get().getUpdatedTime());
    }

    @Test
    void deleteByIp() {
        validateDuplicatedIp(hostIp1);
        validateDuplicatedName(regHost1.getName());
        validateOverflowedHostNum();

        hostRepository.save(regHost1);

        validateDuplicatedIp(hostIp2);
        validateDuplicatedName(regHost2.getName());
        validateOverflowedHostNum();

        hostRepository.save(regHost2);

        hostRepository.deleteByIp(hostIp1);

        List<Host> result = hostRepository.findAll();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void deleteByName() {
        validateDuplicatedIp(hostIp1);
        validateDuplicatedName(regHost1.getName());
        validateOverflowedHostNum();

        hostRepository.save(regHost1);

        validateDuplicatedIp(hostIp2);
        validateDuplicatedName(regHost2.getName());
        validateOverflowedHostNum();

        hostRepository.save(regHost2);

        hostRepository.deleteByName(regHost2.getName());

        List<Host> result = hostRepository.findAll();
        Assertions.assertEquals(1, result.size());
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
        if (hostRepository.getSize() >= 100) {
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