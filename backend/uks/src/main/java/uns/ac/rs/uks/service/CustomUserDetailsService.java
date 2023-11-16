package uns.ac.rs.uks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.repository.UserRepository;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String email) {
        logger.info("Try to get user with email {}", email);
        return userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found!"));
    }

    public UserDetails loadUserById(UUID id) {
        logger.info("Try to get user by id");
        return userRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found!"));
    }
}
