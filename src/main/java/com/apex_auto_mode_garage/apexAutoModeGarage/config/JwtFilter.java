package com.apex_auto_mode_garage.apexAutoModeGarage.config;

import com.apex_auto_mode_garage.apexAutoModeGarage.service.JWTService;
import com.apex_auto_mode_garage.apexAutoModeGarage.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final ApplicationContext context;

    private static final List<String> PUBLIC_URLS = Arrays.asList(
            "/api/v1/auth/login",
            "/api/v1/auth/register"
    );

    @Autowired
    public JwtFilter(JWTService jwtService, ApplicationContext context) {
        this.jwtService = jwtService;
        this.context = context;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();

        // Skip JWT validation for public endpoints
        if (isPublicUrl(requestPath)) {
            System.out.println("JwtFilter - Public endpoint, skipping JWT validation");
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName =  null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                userName = jwtService.getUsername(token);
            }

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = context.getBean(UserDetailService.class).loadUserByUsername(userName);


                if (jwtService.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,null,userDetails.getAuthorities()
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }else {
                    throw new RuntimeException("Token is invalid !");
                }
            }
        } catch (Exception e){
            System.err.println("JwtFilter - Exception: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Authentication failed\"}");
            return;
        }

        filterChain.doFilter(request,response);
    }

    private boolean isPublicUrl(String requestPath) {
        return PUBLIC_URLS.stream().anyMatch(requestPath::startsWith);
    }
}
