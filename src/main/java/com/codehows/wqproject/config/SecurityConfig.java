package com.codehows.wqproject.config;

import com.codehows.wqproject.auth.jwt.JwtAccessDeniedHandler;
import com.codehows.wqproject.auth.jwt.JwtAuthenticationEntryPoint;
import com.codehows.wqproject.auth.jwt.JwtAuthenticationFilter;
import com.codehows.wqproject.auth.jwt.JwtTokenProvider;
import com.codehows.wqproject.auth.oAuth.OAuth2AuthenticationFailureHandler;
import com.codehows.wqproject.auth.oAuth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.codehows.wqproject.auth.oAuth.OAuth2AuthenticationSuccessHandler;
import com.codehows.wqproject.auth.oAuth.OAuth2UserCustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.codehows.wqproject.constant.JwtTokenConstant.HEADER_AUTHORIZATION;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling( exceptionHandling -> exceptionHandling
                        .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/admin/**"))
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(it -> it
                                .baseUri("/oauth2/authorize")
                                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository))
                        .redirectionEndpoint(it -> it
                                .baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(it -> it
                                .userService(oAuth2UserCustomService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler))
                .logout(logout -> logout
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID"))
                .headers( headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests( request -> request
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers(antMatcher("/images/**")).permitAll()
                        .requestMatchers(antMatcher("/question/**")).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(antMatcher("/lecture/*")).hasAnyRole("ADMIN")
                        .requestMatchers(antMatcher("/role/**")).hasRole("ADMIN")
                        .requestMatchers(antMatcher("/lecture/image/*")).permitAll()
                        .requestMatchers(antMatcher("/lecture/limit/*")).permitAll()
                        .requestMatchers(antMatcher("/favicon.ico")).permitAll()
                        .requestMatchers(antMatcher("/auth/**")).permitAll()
                        .requestMatchers(antMatcher("/oauth2/**")).permitAll()
                        .requestMatchers(antMatcher("/ws")).permitAll()
                        .anyRequest().authenticated())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter tokenAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider);
    }

    @Value("${domainName}")
    private String domainName;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin(domainName);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        config.addExposedHeader(HEADER_AUTHORIZATION);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}