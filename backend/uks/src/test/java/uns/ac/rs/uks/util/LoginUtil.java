package uns.ac.rs.uks.util;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import uns.ac.rs.uks.dto.request.LoginRequest;
import uns.ac.rs.uks.dto.response.TokenResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginUtil {

    public static HttpHeaders login(String username, String password, TestRestTemplate restTemplate) {
        LoginRequest request = new LoginRequest();
        request.setEmail(username);
        request.setPassword(password);
        ResponseEntity<TokenResponse> tokenResp = restTemplate
                .exchange("/auth/login", HttpMethod.POST, new HttpEntity<>(request), TokenResponse.class);

        assertNotNull(tokenResp.getBody());
        String token = tokenResp.getBody().getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }
}
