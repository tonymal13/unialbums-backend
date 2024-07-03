package ru.mal.unialbumsbackend.web.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
public class JwtAuthentication implements Authentication {

    private boolean authenticated;

    private String firstName;

    private String role;

    private String username;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return Collections.singletonList(new SimpleGrantedAuthority(role)); }


    @Override
    public Object getCredentials() { return null; }

    @Override
    public Object getDetails() { return null; }

    @Override
    public Object getPrincipal() { return username; }

    @Override
    public boolean isAuthenticated() { return authenticated; }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() { return firstName; }

    public void setUsername(String subject) {
        username=subject;
    }
}