package uns.ac.rs.uks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.RegistrationRequest;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.exception.AlreadyExistsException;
import uns.ac.rs.uks.mapper.UserMapper;
import uns.ac.rs.uks.model.RepositoryRole;
import uns.ac.rs.uks.model.Role;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.repository.RoleRepository;
import uns.ac.rs.uks.repository.UserRepository;

import java.util.Base64;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO registerUser(RegistrationRequest registrationRequest){
        checkEmailAvailability(registrationRequest.getEmail());
        User user = UserMapper.toUserFromRequest(registrationRequest);
        String password = new String(Base64.getDecoder().decode(registrationRequest.getPassword()));
        user.setPassword(passwordEncoder.encode(password));
        Role role = roleRepository.findByName("ROLE_USER").get();
        user.setRole(role);
        return UserMapper.toDTO(userRepository.save(user));
    }

    private void checkEmailAvailability(String email) {
        if (userRepository.existsByEmail(email))
            throw new AlreadyExistsException("User already exists!");
    }
}
