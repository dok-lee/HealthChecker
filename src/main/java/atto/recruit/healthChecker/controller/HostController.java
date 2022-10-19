package atto.recruit.healthChecker.controller;

import atto.recruit.healthChecker.domain.Health;
import atto.recruit.healthChecker.domain.Host;
import atto.recruit.healthChecker.domain.RegHost;
import atto.recruit.healthChecker.service.HealthCheckService;
import atto.recruit.healthChecker.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class HostController {

    private final RegisterService registerService;
    private final HealthCheckService healthCheckService;

    @Autowired
    public HostController(RegisterService registerService, HealthCheckService healthCheckService) {
        this.registerService = registerService;
        this.healthCheckService = healthCheckService;
    }

    @PostMapping("host")
    @ResponseBody
    public String joinHost(@RequestParam("ip") String ip, @RequestParam("name") String name) {
        RegHost regHost = new RegHost();

        Inet4Address regHostIp;

        try {
            regHostIp = (Inet4Address) Inet4Address.getByName(ip);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        regHost.setIp(regHostIp);
        regHost.setName(name);

        registerService.join(regHost);
        Health health = new Health();
        healthCheckService.join(regHostIp, health);
        return "host ip: " + ip + ", name: " + name + " successfully joined";
    }

    @GetMapping("host")
    public List<Host> getHostAll() {
        return registerService.findAll();
    }

    @GetMapping("host/ip/{ip}")
    @ResponseBody
    public Optional<Host> getHostByIp(@PathVariable("ip") String ip) {
        Inet4Address HostIp;

        try {
            HostIp = (Inet4Address) Inet4Address.getByName(ip);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        return registerService.findOne(HostIp);
    }

    @GetMapping("host/name/{name}")
    @ResponseBody
    public Optional<Host> getHostByName(@PathVariable("name") String name) {
        return registerService.findOne(name);
    }

    @PutMapping("host")
    @ResponseBody
    public Host putHost(@RequestParam("ip") String ip, @RequestParam("name") String name) {
        RegHost regHost = new RegHost();

        Inet4Address regHostIp;

        try {
            regHostIp = (Inet4Address) Inet4Address.getByName(ip);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        regHost.setIp(regHostIp);
        regHost.setName(name);

        return registerService.updateOne(regHost);
    }

    @DeleteMapping("host/ip/{ip}")
    @ResponseBody
    public Optional<Host> deleteHostByIp(@PathVariable("ip") String ip) {
        Inet4Address hostIp;

        try {
            hostIp = (Inet4Address) Inet4Address.getByName(ip);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        healthCheckService.deleteOne(hostIp);
        return registerService.deleteOne(hostIp);
    }

    @DeleteMapping("host/name/{name}")
    @ResponseBody
    public Optional<Host> deleteHostByName(@PathVariable("name") String name) {
        healthCheckService.deleteOne(name);
        return registerService.deleteOne(name);
    }

    @DeleteMapping("host")
    @ResponseBody
    public void clear() {
        healthCheckService.clear();
        registerService.clear();
    }


}
