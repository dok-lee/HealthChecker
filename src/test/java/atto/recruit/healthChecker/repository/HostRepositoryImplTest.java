package atto.recruit.healthChecker.repository;

import atto.recruit.healthChecker.domain.Host;
import atto.recruit.healthChecker.domain.RegHost;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

class HostRepositoryImplTest {
    HostRepository repository = new HostRepositoryImpl();

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
        repository.clear();
    }

    @Test
    public void save_findByIp() {
        repository.save(regHost1);

        Optional<Host> result = repository.findByIp(regHost1.getIp());
        Assertions.assertThat(regHost1.getName()).isEqualTo(result.get().getName());
        System.out.println("Registered Time: " + result.get().getRegisteredTime());
        System.out.println("Updated Time: " + result.get().getUpdatedTime());
    }

    @Test
    public void findByName() {
        repository.save(regHost1);
        repository.save(regHost2);

        Host result = repository.findByName("host1").get();

        Assertions.assertThat(result.getIp()).isEqualTo(regHost1.getIp());
        System.out.println("Registered Time: " + result.getRegisteredTime());
        System.out.println("Updated Time: " + result.getUpdatedTime());
    }

    @Test
    public void findAll_getSize() {
        repository.save(regHost1);
        repository.save(regHost2);

        List<Host> result = repository.findAll();

        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void deleteByIp() {
        repository.save(regHost1);
        repository.save(regHost2);

        repository.deleteByIp(regHost1.getIp());

        List<Host> result = repository.findAll();
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void deleteByName() {
        repository.save(regHost1);
        repository.save(regHost2);

        repository.deleteByName(regHost1.getName());

        List<Host> result = repository.findAll();
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void update() {
        repository.save(regHost1);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        RegHost updateHost = new RegHost();
        updateHost.setIp(regHost1.getIp());
        updateHost.setName(regHost2.getName());

        repository.update(updateHost);
        Optional<Host> result = repository.findByIp(regHost1.getIp());

        if (result.isEmpty()) {
            Assertions.assertThatNullPointerException();
        }

        Assertions.assertThat(result.get().getName()).isEqualTo(regHost2.getName());
        System.out.println("Registered Time: " + result.get().getRegisteredTime());
        System.out.println("Updated Time: " + result.get().getUpdatedTime());
    }
}
