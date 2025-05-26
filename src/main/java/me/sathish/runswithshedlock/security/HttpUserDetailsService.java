package me.sathish.runswithshedlock.security;

import java.util.Collection;
import java.util.List;

import me.sathish.runswithshedlock.runner.Runner;
import me.sathish.runswithshedlock.runner.RunnerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class HttpUserDetailsService implements UserDetailsService {
    private final RunnerRepository runnerRepository;

    public HttpUserDetailsService(RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(UserRoles.ADMIN));
        final List<SimpleGrantedAuthority> authoritiesRoles = List.of(new SimpleGrantedAuthority(UserRoles.USER));
        final Runner runner = runnerRepository.findByEmail(email);
        if (runner != null) {
            return runner;
        } else
            throw new UsernameNotFoundException("User with email" + email + " not found");
    }

}
