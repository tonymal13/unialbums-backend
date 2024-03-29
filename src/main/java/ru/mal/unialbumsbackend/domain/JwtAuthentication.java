package ru.mal.unialbumsbackend.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
public class JwtAuthentication implements Authentication {

    private boolean authenticated;

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return Collections.singletonList(new SimpleGrantedAuthority(user.getRole())); }


    @Override
    public Object getCredentials() { return null; }

    @Override
    public Object getDetails() { return null; }

    @Override
    public Object getPrincipal() { return user.getLogin(); }

    @Override
    public boolean isAuthenticated() { return authenticated; }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() { return user.getFirstName(); }

    public void setRole(String  role) {
        user.setRole(role);
    }

    public void setFirstName(String firstName) {
        user.setFirstName(firstName);
    }

    public void setUsername(String subject) {
        user.setLogin(subject);
    }
}