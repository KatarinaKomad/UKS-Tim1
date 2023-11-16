package uns.ac.rs.uks.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import uns.ac.rs.uks.apiResponse.ResponseError;
import uns.ac.rs.uks.exception.InvalidAccessTokenException;
import uns.ac.rs.uks.service.CustomUserDetailsService;

import java.io.IOException;

//@Transactional
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenProvider.readTokenFromRequest(request);

        if (!StringUtils.hasLength(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String email = tokenProvider.getUsernameFromToken(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            if(tokenProvider.validateToken(token, userDetails)){
                TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (InvalidAccessTokenException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error has occurred.");
            e.printStackTrace();
        }


    }


    private void sendResponse(HttpServletResponse response, Integer status, String message) throws IOException {
        ResponseError responseError = new ResponseError(status, message);
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), responseError);
    }

}
