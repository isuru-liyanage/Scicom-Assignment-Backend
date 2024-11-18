package com.example.demo.config.filters;


import com.example.demo.services.UserService;
import com.example.demo.util.JwtHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {




    private JwtHelper jwtUtil;
    private UserService UserDetailsService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {

                if ("Authorization".equals(cookie.getName())) {
                    token = cookie.getValue();

                }
            }
        }
        if (token == null) {
            token = request.getHeader("Authorization");

        }

        String username = null;
        if (token != null) {
            try {
                username = jwtUtil.extractUsername(token);
            } catch (IllegalArgumentException e) {
                logger.info("Illegal Argument while fetching the username !!");

            } catch (ExpiredJwtException e) {
                logger.info("Given jwt token is expired !!");
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                logger.info("Some changes have been made to the token !! Invalid Token");

            } catch (Exception e) {

            }
        } else {
            logger.info("JWT Token not found in cookies !!");
        }

//        String authorizationHeader = request.getHeader("Authorization");
//        if (authorizationHeader == null ) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String token = authorizationHeader;
//        String username = jwtUtil.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails= UserDetailsService.loadUserByUsername(username);

            if(jwtUtil.isTokenValid(token, userDetails)){
                UsernamePasswordAuthenticationToken AuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                AuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(AuthenticationToken);

            }
        }
        filterChain.doFilter(request, response);
    }
}
