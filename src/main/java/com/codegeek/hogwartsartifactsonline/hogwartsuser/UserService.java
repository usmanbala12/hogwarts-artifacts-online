package com.codegeek.hogwartsartifactsonline.hogwartsuser;

import com.codegeek.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<HogwartsUser> findAll() {
        return userRepository.findAll();
    }

    public HogwartsUser findById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }


    public HogwartsUser save(HogwartsUser hogwartsUser) {
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
}
