package band.proxy.service;


import band.proxy.entity.UserEntity;
import band.proxy.exception.UserNotFoundException;
import band.proxy.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class UserService {
    UserRepository userRepository;

    public List<UserEntity> getAll() {
        log.info("Get all users");
        return userRepository.findAll();
    }

    public UserEntity save(UserEntity userEntity) {
        log.info("Saving UserEntity:{}", userEntity);
        return userRepository.save(userEntity);
    }

    public UserEntity update(UserEntity userEntity) {
        checkUserBy(userEntity.getId());
        log.info("Updating UserEntity:{}", userEntity);
        return userRepository.save(userEntity);
    }

    public void deleteBy(String id) {
        checkUserBy(id);
        log.info("Deleting UserEntity with id:{}", id);
        userRepository.deleteById(id);
    }

    private void checkUserBy(String id) {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

}