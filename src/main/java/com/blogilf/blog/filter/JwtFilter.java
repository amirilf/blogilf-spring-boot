package com.blogilf.blog.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.blogilf.blog.model.User;
import com.blogilf.blog.model.UserPrincipal;
import com.blogilf.blog.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{

    private final JwtService jwtService;

    JwtFilter(JwtService jwtService){
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            
            String header = request.getHeader("Authorization");
            String token = null;
            String username = null;

            if (header != null && header.startsWith("Bearer")) {
                token = header.substring(7);
                username = jwtService.extractUsername(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // TODO: fetch from CustomUserDetailService.loadUserByUsername ?
                UserPrincipal userPrincipal = new UserPrincipal(new User(null, null,username, null, null));

                if (jwtService.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userPrincipal,null,userPrincipal.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        // handle possible exceptions from jwt
        } catch (SignatureException ex) {
            handleException(response, "Invalid JWT signature", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (MalformedJwtException ex) {
            handleException(response, "Malformed JWT token", HttpServletResponse.SC_BAD_REQUEST);
        } catch (ExpiredJwtException ex) {
            handleException(response, "Expired JWT token", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (UnsupportedJwtException ex) {
            handleException(response, "Unsupported JWT token", HttpServletResponse.SC_BAD_REQUEST);
        } catch (IllegalArgumentException ex) {
            handleException(response, "JWT token is missing or invalid", HttpServletResponse.SC_BAD_REQUEST);
        } catch (JwtException ex) {
            handleException(response, "Invalid JWT token", HttpServletResponse.SC_UNAUTHORIZED);
        }
    }   

    private void handleException(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}