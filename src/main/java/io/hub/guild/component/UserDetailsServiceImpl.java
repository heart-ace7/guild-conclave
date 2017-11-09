package io.hub.guild.component;

import com.google.common.base.Strings;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (Strings.isNullOrEmpty(username)) {
            throw new UsernameNotFoundException("username not found.");
        }
        return new org.springframework.security.core.userdetails.User(
                username,
                "$2a$10$jaYchwZTGuhVyKLUAwQGiOQlgcCEd40k3bp8LqWmf9roLTRIcgPjG",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}