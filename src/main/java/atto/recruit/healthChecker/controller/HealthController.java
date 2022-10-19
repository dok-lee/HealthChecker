package atto.recruit.healthChecker.controller;

import atto.recruit.healthChecker.domain.Health;
import atto.recruit.healthChecker.service.HealthCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HealthController {

    private final HealthCheckService healthCheckService;

    @Autowired
    public HealthController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }


    @GetMapping("health")
    public List<Health> getHostAll() {
        return healthCheckService.findAll();
    }

    @GetMapping("health/ip/{ip}")
    @ResponseBody
    public Health getHealthByIp(@PathVariable("ip") String ip) {
        Inet4Address HostIp;

        try {
            HostIp = (Inet4Address) Inet4Address.getByName(ip);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        return healthCheckService.findOne(HostIp);
    }

    @GetMapping("health/name/{name}")
    @ResponseBody
    public Health getHealthByName(@PathVariable("name") String name) {
        return healthCheckService.findOne(name);
    }
}
