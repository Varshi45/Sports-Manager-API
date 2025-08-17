package com.sports.manager.Config;

import com.sports.manager.Service.AuthService;
import com.sports.manager.Utils.JwtUtils;
import com.sports.manager.Entity.Admin;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class AdminAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    public AdminAuthenticationFilter(@Lazy AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "No token provided.");
            return;
        }


        String token = authHeader.substring(7);

        try {
            Claims claims = JwtUtils.decodeToken(token);
            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    email, null, List.of(authority)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);


            // Only allow admins
            if (!"admin".equalsIgnoreCase(role)) {
                response.sendError(HttpStatus.FORBIDDEN.value(), "Access denied. Admins only.");
                return;
            }

            Optional<Admin> admin = authService.findAdminByEmail(email);



            if (admin.isEmpty()) {
                response.sendError(HttpStatus.FORBIDDEN.value(), "Access denied. Admins only.");
                return;
            }

            Admin safeAdmin = admin.get();
            safeAdmin.setPassword("dummy");

            request.setAttribute("user", safeAdmin);

            filterChain.doFilter(request, response);


        } catch (Exception e) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized. Invalid token.");
        }

    }
}
