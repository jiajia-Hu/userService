package com.user.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.user.entity.Group;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class JwtUser implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final String email;
    private final Group group;

    public JwtUser(
            Long id,
            String username,
            String password,
            String email,
            Group group) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.group = group;
    }



    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(group.getGroupRole());
        Set<GrantedAuthority> mapped = new HashSet<GrantedAuthority>();
        mapped.add(simpleGrantedAuthority);
        return mapped;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }



}
