package me.sathish.runswithshedlock.security;

import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UsersecurityUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(final String username) {
        if ("admin".equals(username)) {
            final List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(UserRoles.ADMIN));
            return User.withUsername(username)
                    .password("{bcrypt}$2a$10$FMzmOkkfbApEWxS.4XzCKOR7EbbiwzkPEyGgYh6uQiPxurkpzRMa6")
                    .authorities(authorities)
                    .build();
        } else if ("user".equals(username)) {
            final List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(UserRoles.USER));
            return User.withUsername(username)
                    .password("{bcrypt}$2a$10$FMzmOkkfbApEWxS.4XzCKOR7EbbiwzkPEyGgYh6uQiPxurkpzRMa6")
                    .authorities(authorities)
                    .build();
        }
        throw new UsernameNotFoundException("User " + username + " not found");
    }

}
