package atto.recruit.healthChecker.domain;

import javax.persistence.*;
import java.net.Inet4Address;

@Entity
public class RegHost {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private Inet4Address ip;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Inet4Address getIp() {
        return ip;
    }

    public void setIp(Inet4Address ip) {
        this.ip = ip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
