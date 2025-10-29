package com.vianeo.security;

import com.vianeo.model.entity.Compte;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CompteDetails implements UserDetails {
    
    private final Compte compte;

    public CompteDetails(Compte compte) {
        this.compte = compte;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(compte.getRole()));
    }

    @Override
    public String getPassword() {
        return compte.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return compte.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return compte.getActif();
    }

    public Compte getCompte() {
        return compte;
    }
}