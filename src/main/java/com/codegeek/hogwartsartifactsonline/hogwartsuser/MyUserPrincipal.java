package com.codegeek.hogwartsartifactsonline.hogwartsuser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;

public class MyUserPrincipal implements UserDetails {

    private HogwartsUser hogwartsUser;

    public MyUserPrincipal(HogwartsUser hogwartsUser) {
        this.hogwartsUser = hogwartsUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /*
        * convert a user roles from space delimited string to a list of simpleGrantedAuthority objects
        * */
        return Arrays.stream(StringUtils.tokenizeToStringArray(hogwartsUser.getRoles(), " "))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }

    @Override
    public String getPassword() {
        return hogwartsUser.getPassword();
    }

    @Override
    public String getUsername() {
        return hogwartsUser.getUsername();
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
        return hogwartsUser.isEnabled();
    }

    public HogwartsUser getHogwartsUser() {
        return hogwartsUser;
    }
}
