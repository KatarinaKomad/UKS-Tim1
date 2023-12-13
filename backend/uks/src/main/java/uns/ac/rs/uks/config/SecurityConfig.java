package uns.ac.rs.uks.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import uns.ac.rs.uks.security.CustomPermissionEvaluator;
import uns.ac.rs.uks.security.RestAuthenticationEntryPoint;
import uns.ac.rs.uks.security.TokenAuthenticationFilter;
import uns.ac.rs.uks.security.UserForbiddenErrorHandler;
import uns.ac.rs.uks.service.CustomUserDetailsService;
import uns.ac.rs.uks.service.MemberService;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private MemberService memberService;

    private final String[] patterns = {
            antMatcher("/websocket/**").getPattern(),
            antMatcher("/auth/login").getPattern(),
            antMatcher("/auth/logout/**").getPattern(),
            antMatcher("/auth/register").getPattern()
    };
    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
    @Bean
    public MethodSecurityExpressionHandler expressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator(memberService));
        return expressionHandler;
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(x-> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(x-> x.authenticationEntryPoint(new RestAuthenticationEntryPoint())
                         .accessDeniedHandler(new UserForbiddenErrorHandler())
                )
                .headers(x-> x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).disable())
                .httpBasic(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(exchange ->
                        exchange.requestMatchers(antMatcher("/websocket/**")).permitAll()
                                .requestMatchers(mvc.pattern("/auth/login")).permitAll()
                                .requestMatchers(mvc.pattern("/auth/register")).permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(tokenAuthenticationFilter(), BasicAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().
                requestMatchers(new AntPathRequestMatcher("/")).
                requestMatchers(new AntPathRequestMatcher("/*.html")).
                requestMatchers(new AntPathRequestMatcher("favicon.ico")).
                requestMatchers(new AntPathRequestMatcher("/*/*.html")).
                requestMatchers(new AntPathRequestMatcher("/*/*.css")).
                requestMatchers(new AntPathRequestMatcher("/*/*.js")).
                requestMatchers(new AntPathRequestMatcher("/v2/api-docs")).
                requestMatchers(new AntPathRequestMatcher("/webjars/*"));
    }

}
