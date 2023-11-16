package uns.ac.rs.uks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.RoleEnum;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.UserMapper;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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
    public boolean doesUserExists(String email) throws NotFoundException{
        logger.info("Check to user with email {} is exist", email);
        return userRepository.findByEmail(email).orElse(null) != null;
    }

    public boolean isUserAccountValid(User user) {
        logger.info("Checking whether the user account with email {} is valid", user.getEmail());
        return !user.getBlockedByAdmin() && !user.getDeleted();
    }

//    @Transactional
//    public void changePassword(PasswordChangeRequest passwordChangeRequest, User user) {
//        if(!passwordEncoder.matches(passwordChangeRequest.getCurrentPassword(), user.getPassword())) {
//            logger.warn("Failed to change password -- current password not valid");
//            throw new InvalidPasswordRequestException("Current password not valid.");
//        }
//        user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
//        user.setLastPasswordResetDate(DateUtil.getNow());
//        userRepository.save(user);
//        logger.info("Successfully saved user with email {} with new password", user.getEmail());
//    }

//    public void changeRole(RoleChangeRequest roleChangeRequest)  throws NotFoundException{
//        List<Role> roles = roleService.getRoles(roleChangeRequest.getRoles());
//        User user = getUserByEmail(roleChangeRequest.getEmail());
//        user.setRoles(roles);
//        userRepository.save(user);
//        logger.info("Successfully saved user with email {} new role", user.getEmail());
//    }
    public void blockUser(String email)  throws NotFoundException{
        logger.info("Try to block user with email {}", email);
        User user = getUserByEmail(email);
        user.setBlockedByAdmin(true);
        userRepository.save(user);
    }

    public void unblockUser(String email)  throws NotFoundException{
        logger.info("Try to unblock user with email {}", email);
        User user = getUserByEmail(email);
        user.setBlockedByAdmin(false);
        userRepository.save(user);
    }

    public void deleteUser(String email)  throws NotFoundException{
        logger.info("Try to delete user with email {}", email);
        User user = getUserByEmail(email);
        user.setDeleted(true);
        userRepository.save(user);
    }

    public List<UserDTO> getAll() {
        logger.info("Try to get all users");
        return UserMapper.toDTOs(userRepository.findAllActiveUsers());
    }

    public List<UserDTO> getAllById(List<UUID> ids) {
        logger.info("Try to get all users by id");
        return UserMapper.toDTOs(userRepository.findByIdIn(ids));
    }


    public List<UserDTO> getByRoles(List<RoleEnum> roles) {
        List<User> users = userRepository.findAll();
//        users = users.stream().filter(user-> new HashSet<>(user.getRoles()).containsAll(roles)).toList(); // ALL ROLES
        logger.info("Try to get all users by entered role");
        users = users.stream().filter(
                user->
                    user.getRoles().stream().map(role -> RoleEnum.valueOf(role.getName()))
                    .anyMatch(roles::contains)
                ).toList(); // ANY ROLES
        return UserMapper.toDTOs(users);
    }
}
