package ua.oleksii.bank.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import ua.oleksii.bank.constants.ApplicationConstants;
import ua.oleksii.bank.exceptionhandling.CustomAccessDeniedHandler;
import ua.oleksii.bank.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import ua.oleksii.bank.filter.*;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!prod")
public class ProjectSecurityConfig {

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsConfig -> corsConfig.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                    config.setAllowedMethods(Collections.singletonList(CorsConfiguration.ALL));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
                    config.setExposedHeaders(Collections.singletonList(ApplicationConstants.JWT_HEADER));
                    config.setMaxAge(3600L);
                    return config;
                }))
                .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/contact", "/register", "/apiLogin")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidtaionBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAt(new AuthorityLoggingFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthorityLoggingAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()) // Only HTTP
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/myAccount").hasRole("ADMIN")
                        .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/myLoans").authenticated()
                        .requestMatchers("/myCards").hasAuthority("USER")
                        .requestMatchers("/user").authenticated()
                        .requestMatchers(
                                "/notices",
                                "/contact",
                                "/error",
                                "/register",
                                "/invalidSession",
                                "/apiLogin"
                        ).permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }

//	@Bean
//	public UserDetailsService userDetailsService(DataSource dataSource) {
//		return new JdbcUserDetailsManager(dataSource);
//	}

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        var authenticationProvider = new BankUsernamePasswordAuthenticationProvider(userDetailsService);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


//	@Bean
//	public CompromisedPasswordChecker compromisedPasswordChecker() {
//		return new HaveIBeenPwnedRestApiPasswordChecker();
//	}
}
