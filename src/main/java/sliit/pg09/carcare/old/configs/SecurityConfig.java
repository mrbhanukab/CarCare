package sliit.pg09.carcare.old.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sliit.pg09.carcare.old.repository.ClientRepository;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/scripts/**", "/css/**", "/images/**", "/next-image/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/")
                        .successHandler(customAuthenticationSuccessHandler)
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

    @Component
    static class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

        @Autowired
        ClientRepository clients;

        @Override

        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

            var user = ((OAuth2User) authentication.getPrincipal()).getAttributes();

            clients.findById(user.get("email").toString()).ifPresentOrElse(u -> {
            }, () -> clients.save(new sliit.pg09.carcare.old.model.Client(
                    user.get("name").toString(),
                    user.get("email").toString(),
                    user.get("picture").toString()
            )));
            response.sendRedirect("/client/dashboard");
        }
    }
}