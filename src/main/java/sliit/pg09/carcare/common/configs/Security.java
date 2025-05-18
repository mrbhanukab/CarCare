package sliit.pg09.carcare.common.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sliit.pg09.carcare.admin.AdminService;
import sliit.pg09.carcare.client.Client;
import sliit.pg09.carcare.client.ClientRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class Security {
    private final AdminService adminService;

    public Security(AdminService adminService) {
        this.adminService = adminService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(2)
    SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(adminService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return http
                .securityMatcher("/admin/**")
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/admin/login/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/admin/appointments/reject").hasRole("ADMIN")
                                .anyRequest().hasRole("ADMIN")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .usernameParameter("username")
                        .defaultSuccessUrl("/admin")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/admin/login"))
                .authenticationProvider(authProvider)
                .build();
    }

    @Bean
    @Order(3)
    SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/",
                            "/scripts/**", "/css/**", "/images/**", "/next-image/**", "/error").permitAll();
                })
                .build();
    }

    @Component
    public static class ClientSecurityConfig {
        private final CustomClientAuthenticationSuccessHandler customClientAuthenticationSuccessHandler;

        public ClientSecurityConfig(CustomClientAuthenticationSuccessHandler customClientAuthenticationSuccessHandler) {
            this.customClientAuthenticationSuccessHandler = customClientAuthenticationSuccessHandler;
        }

        @Bean
        @Order(1)
        SecurityFilterChain clientSecurityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .securityMatcher("/client/**", "/oauth2/**", "/login/**", "/logout/**")
                    .authorizeHttpRequests(request -> request
                            .requestMatchers("/oauth2/**", "/login/**", "/logout/**").permitAll()
                            .anyRequest().hasRole("CLIENT"))
                    .oauth2Login(oauth2 -> oauth2
                            .loginPage("/")
                            .successHandler(customClientAuthenticationSuccessHandler)
                            .permitAll()
                    )
                    .logout(logout -> logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                    .build();
        }
    }

    @Component
    static class CustomClientAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

        @Autowired
        ClientRepository clients;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws IOException {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            var user = oAuth2User.getAttributes();

            clients.findById(user.get("email").toString()).ifPresentOrElse(u -> {
            }, () -> clients.save(new Client(
                    user.get("name").toString(),
                    user.get("email").toString(),
                    user.get("picture").toString()
            )));

            // Add ROLE_CLIENT authority to the authentication
            Set<GrantedAuthority> authorities = new HashSet<>(oAuth2User.getAuthorities());
            authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));

            // Create new authentication token with added authority
            OAuth2AuthenticationToken newAuth = new OAuth2AuthenticationToken(
                    new DefaultOAuth2User(authorities, user, "email"),
                    authorities,
                    authentication.getName()
            );

            // Update the security context
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            response.sendRedirect("/client");
        }
    }
}