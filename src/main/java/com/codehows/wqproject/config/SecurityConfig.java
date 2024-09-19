package com.codehows.wqproject.config;

import com.codehows.wqproject.constant.Role;
import com.codehows.wqproject.jwt.JwtAccessDeniedHandler;
import com.codehows.wqproject.jwt.JwtAuthenticationEntryPoint;
import com.codehows.wqproject.jwt.JwtFilter;
import com.codehows.wqproject.jwt.TokenProvider;
import com.codehows.wqproject.oAuth2.CookieAuthorizationRequestRepository;
import com.codehows.wqproject.oAuth2.OAuth2AuthenticationFailureHandler;
import com.codehows.wqproject.oAuth2.OAuth2AuthenticationSuccessHandler;
import com.codehows.wqproject.oAuth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Value("${domainName}")
    private String frontDomain;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        http
                .httpBasic(
                        basic -> basic.disable()
                )
                .csrf(
                        csrf -> csrf.disable()
                )
                .oauth2Login(
                        oauth2 -> oauth2
                                .authorizationEndpoint(
                                        it -> it.baseUri("/oauth2/authorize")
                                                .authorizationRequestRepository(cookieAuthorizationRequestRepository)
                                )
                                .redirectionEndpoint(
                                        it -> it.baseUri("/oauth2/callback/*")
                                )
                                .userInfoEndpoint(
                                        it -> it.userService(customOAuth2UserService)
                                )
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)

                )
                .logout(
                        it -> it
                                .clearAuthentication(true)
                                .deleteCookies("JSESSIONID")
                )
                .exceptionHandling( exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .headers( headers ->
                            headers.frameOptions(
                                    it -> it.sameOrigin()
                            )
                        )
                .sessionManagement( sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        new JwtFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                )
                .authorizeHttpRequests( request -> {
                    request.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                            //TEST
//                            .requestMatchers(new MvcRequestMatcher(introspector, "/question/**")).hasAnyRole("ADMIN", "USER")
//                            .requestMatchers(new MvcRequestMatcher(introspector, "/lecture/**")).hasRole("ADMIN")
//                            .requestMatchers(new MvcRequestMatcher(introspector, "/role/**")).hasRole("ADMIN")
//                            .requestMatchers(new MvcRequestMatcher(introspector, "/favicon.ico")).permitAll()
//                            .requestMatchers(new MvcRequestMatcher(introspector, "/image/**")).permitAll()
//                            .requestMatchers(new MvcRequestMatcher(introspector, "/auth/**")).permitAll()
                            // DEPLOY
                            .requestMatchers(new MvcRequestMatcher(introspector, "/question/**")).hasAnyRole("ADMIN", "USER")
                            .requestMatchers(new MvcRequestMatcher(introspector, "/lecture/**")).hasAnyRole("ADMIN")
                            .requestMatchers(new MvcRequestMatcher(introspector, "/role/**")).hasRole("ADMIN")
                            .requestMatchers(new MvcRequestMatcher(introspector, "/favicon.ico")).permitAll()
                            .requestMatchers(new MvcRequestMatcher(introspector, "/image/**")).permitAll()
                            .requestMatchers(new MvcRequestMatcher(introspector, "/common/**")).hasAnyRole("ADMIN", "USER","TEMP")
                            .requestMatchers(new MvcRequestMatcher(introspector, "/auth/**")).permitAll()
                            .requestMatchers(new MvcRequestMatcher(introspector, "/oauth2/**")).permitAll()
                            .requestMatchers(new MvcRequestMatcher(introspector, "/ws")).permitAll()
                            .requestMatchers(new MvcRequestMatcher(introspector, "/**")).permitAll()
                            .anyRequest().authenticated();
                })
                .cors(
                        cors -> cors.configurationSource(corsConfigurationSource())
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin(frontDomain);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setMaxAge(3600l);
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}