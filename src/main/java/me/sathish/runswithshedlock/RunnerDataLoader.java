package me.sathish.runswithshedlock;

import me.sathish.runswithshedlock.runner.Runner;
import me.sathish.runswithshedlock.runner.RunnerRepository;
import me.sathish.runswithshedlock.security.UserRoles;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RunnerDataLoader implements ApplicationRunner {
    private final RunnerRepository runnerRepository;
    private final PasswordEncoder passwordEncoder;

    public RunnerDataLoader(RunnerRepository runnerRepository, PasswordEncoder passwordEncoder) {
        this.runnerRepository = runnerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (runnerRepository.count() == 0) {
            final List<SimpleGrantedAuthority> authoritiesRoles = List.of(new SimpleGrantedAuthority(UserRoles.USER));
            Runner runner = new Runner();
            runner.setUsername("sathish");
            runner.setHash(passwordEncoder.encode("password"));
            runner.setEmail("sathishk.dot@gmail.com");
            runnerRepository.saveAndFlush(runner);
        }
    }
}
