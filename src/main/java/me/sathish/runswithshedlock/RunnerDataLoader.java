package me.sathish.runswithshedlock;

import me.sathish.runswithshedlock.runner.Runner;
import me.sathish.runswithshedlock.runner.RunnerRepository;
import me.sathish.runswithshedlock.security.UserRoles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RunnerDataLoader implements ApplicationRunner {
    private final RunnerRepository runnerRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;
    public RunnerDataLoader(RunnerRepository runnerRepository, PasswordEncoder passwordEncoder, Environment environment) {
        this.runnerRepository = runnerRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("The username is: " + environment.getProperty("loginusername"));
        if (runnerRepository.count() == 0) {
            final List<SimpleGrantedAuthority> authoritiesRoles = List.of(new SimpleGrantedAuthority(UserRoles.USER));
            Runner runner = new Runner();
            runner.setUsername(environment.getProperty("loginusername"));
            runner.setHash(passwordEncoder.encode("password"));
            runner.setEmail("sathishk.dot@gmail.com");
            runnerRepository.saveAndFlush(runner);
        }
    }
}
