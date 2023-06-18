package com.codegeek.hogwartsartifactsonline.hogwartsuser;

import com.codegeek.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<HogwartsUser> findAll() {
        return userRepository.findAll();
    }

    public HogwartsUser findById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }


    public HogwartsUser save(HogwartsUser hogwartsUser) {
        // TODO we need to encode plain text passwords before saving to the DB
        hogwartsUser.setPassword(passwordEncoder.encode(hogwartsUser.getPassword()));
        return userRepository.save(hogwartsUser);
    }

    public HogwartsUser update(Integer userId, HogwartsUser update) {
        return userRepository.findById(userId)
                .map(hogwartsUser -> {
                            hogwartsUser.setUsername(update.getUsername());
                            hogwartsUser.setEnabled(update.isEnabled());
                            hogwartsUser.setRoles(update.getRoles());
                            return userRepository.save(hogwartsUser);
                        }
                ).orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public void delete(Integer userId) {
        userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
        userRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username) // find user from data
                .map(MyUserPrincipal::new) // if found wrap returned value in MyUserPrincipal object
                .orElseThrow(() -> new UsernameNotFoundException("username "+username+ " is not found")); // else throw exception
    }
}
