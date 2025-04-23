package com.example.liquorstore.config;

import com.example.liquorstore.helpers.JwtHelper;
import com.example.liquorstore.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;

    public JwtAuthFilter(UserDetailsServiceImpl userDetailsService, ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;

            // Extract token if present
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = JwtHelper.extractUsername(token);
            }

            // Handle requests without token
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // Validate token and authenticate user
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                try {
                    if (JwtHelper.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities() // Include user authorities
                                );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                } catch (Exception e) {
                    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token provided. Please login again.");
                    return;
                }
            }

            filterChain.doFilter(request, response);

        } catch (BadCredentialsException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials provided. Please try again.");
        } catch (UsernameNotFoundException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User not found. Please check your credentials.");
        } catch (AccessDeniedException e) {
            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Access denied. You do not have permission to access this resource.");
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        // Create a concrete response wrapper
        HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(response) {
            @Override
            public PrintWriter getWriter() throws IOException {
                // Return a PrintWriter to write the error response
                return new PrintWriter(getOutputStream());
            }
        };

        wrappedResponse.setContentType("application/json");
        wrappedResponse.setCharacterEncoding("UTF-8");
        wrappedResponse.setStatus(statusCode);

       wrappedResponse.getWriter().write(String.format(
                "{\"status\":%d,\"message\":\"%s\"}",
                statusCode,
                message
        ));
    }
}
