package uns.ac.rs.uks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.LoginRequest;
import uns.ac.rs.uks.dto.response.TokenResponse;
import uns.ac.rs.uks.security.TokenProvider;

import java.util.Base64;


@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider tokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public TokenResponse login(LoginRequest loginRequest) {
        logger.info("Try to login user with email {}", loginRequest.getEmail());
        TokenResponse token = authenticateRequest(loginRequest);
        logger.info("Successfully created token");
        return token;
    }

    private TokenResponse authenticateRequest(LoginRequest loginRequest) throws BadCredentialsException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                new String(Base64.getDecoder().decode(loginRequest.getPassword()))
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return createToken(authentication);
    }
    private TokenResponse createToken(Authentication authentication) {
        String accessToken = tokenProvider.generateToken(authentication);
        Long expiresAt = tokenProvider.getExpirationDateFromToken(accessToken).getTime();

        return new TokenResponse(accessToken, expiresAt);
    }


}
