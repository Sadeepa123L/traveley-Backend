package com.traveley.traveley_Backend.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String username;

        // 1. Header එක එනවද කියලා බලමු
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);

        try {
            username = jwtUtil.extractUsername(jwtToken);
            System.out.println("Extracted Username: " + username); // Debug Log

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 2. මෙන්න මෙතන තමයි ගොඩක් වෙලාවට Fail වෙන්නේ
                // validateToken එකට userDetails එකත් යවන්න ඕනෙද කියලා චෙක් කරන්න
                if (jwtUtil.validateToken(jwtToken)) {
                    System.out.println("Token is Valid for user: " + username); // Debug Log

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("Authentication set in SecurityContext for: " + username); // Debug Log
                } else {
                    System.out.println("Token Validation Failed!"); // Debug Log
                }
            }
        } catch (Exception e) {
            System.out.println("Error in JwtAuthFilter: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
