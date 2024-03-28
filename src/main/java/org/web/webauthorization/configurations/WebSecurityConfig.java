package org.web.webauthorization.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.web.webauthorization.BankData.Accounts.Accounts;
import org.web.webauthorization.BankData.Accounts.UserAccount;
import org.web.webauthorization.BankDataRepository.Accounts.AccountRepository;
import org.web.webauthorization.BankDataRepository.Accounts.UserAccountRepository;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/", "/home", "/login", "/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/signup").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/main", true)
                        .permitAll()
                        .successHandler(new CustomAuthenticationSuccessHandler())
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionFixation().none()
                        .sessionConcurrency(concurrency -> concurrency
                                .maximumSessions(1)
                                .expiredUrl("/login?expired")
                        )
                )
                .csrf(AbstractHttpConfigurer::disable); // Отключение CSRF защиты для всего приложения

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserAccount account = userAccountRepository.findByAccountName(username);
            if (account == null) {
                throw new UsernameNotFoundException("Could not find user with username: " + username);
            }

            if (account.getAccountRole() == Accounts.AccountRole.USER) {
                return User.withUsername(username)
                        .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
                        .password(account.getAccountPassword())
                        .roles("USER")
                        .build();
            } else {
                return User.withUsername(username)
                        .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
                        .password(account.getAccountPassword())
                        .roles("ADMIN")
                        .build();
            }
        };
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/static/**");
    }
}


