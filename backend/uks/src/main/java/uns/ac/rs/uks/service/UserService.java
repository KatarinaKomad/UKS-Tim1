package uns.ac.rs.uks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.UserUpdateRequest;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.UserMapper;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.repository.user.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User getById(UUID userId) throws NotFoundException{
        return userRepository.findById(userId).orElseThrow(()->new NotFoundException("User not found."));
    }

    public User getUserByEmail(String email) throws NotFoundException {
        logger.info("Try to get user with email {}", email);
        return userRepository.findByEmail(email).orElseThrow(()->new NotFoundException("User not found."));
    }

    public UserDTO findByEmail(String email) throws NotFoundException{
        logger.info("Try to get user with email {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(()->new NotFoundException("User not found."));
        logger.info("Successfully get user with email {}", email);
        return UserMapper.toDTO(user);
    }

    public boolean existsByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }
    public boolean existsByUsername(String username) {
        Optional<User> user = userRepository.findByCustomUsername(username);
        return user.isPresent();
    }

    public void blockUser(UUID id)  throws NotFoundException {
        logger.info("Block user with id {}", id);
        userRepository.updateBlockedByAdmin(id, true);
    }

    public void unblockUser(UUID id)  throws NotFoundException{
        logger.info("Unblock user with email {}", id);
        userRepository.updateBlockedByAdmin(id, false);
    }

    public void deleteUserByAdmin(UUID id)  throws NotFoundException{
        logger.info("Delete user with email {}", id);
        userRepository.updateDeletedByAdmin(id, true);
    }


    public User save(User user){
        try {
            return userRepository.save(user);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("Error saving user {}", user.getEmail());
            return null;
        }
    }

    @Cacheable(value = "profile", key = "#userID")
    public UserDTO getProfileInfo(UUID userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));
        return UserMapper.toDTO(user);
    }

    //@CacheEvict(value = "profile", allEntries = true)
    public UserDTO updateMyProfile(UUID userID, UserUpdateRequest request) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setCustomUsername(request.getUsername());

        userRepository.save(user);
        return UserMapper.toDTO(user);
    }

    public void deleteUserByUser(UUID id)  throws NotFoundException{
        userRepository.updateDeletedByUser(id, true);
    }

}
