package com.beatdrive.beatdrive.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.beatdrive.beatdrive.repository.AccountRepo;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
    }

}
