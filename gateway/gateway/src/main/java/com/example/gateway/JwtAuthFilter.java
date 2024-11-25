package com.example.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    @Value("${jwt.secret}")
    private String secretKey;

    public JwtAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Extract the Authorization header
            String authHeader = extractTokenFromCookie(exchange.getRequest());
            System.out.println("header "+authHeader);
            if (authHeader == null) {
                // Reject if Authorization header is missing or invalid
                exchange.getResponse().setStatusCode(config.getUnauthorizedStatus());
                return exchange.getResponse().setComplete();
            }

            try {
                // Validate the JWT
                String token = authHeader;
//                System.out.println("token "+token +"\n secret "+secretKey);
                Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

                if(isTokenExpired(claims)){
                    exchange.getResponse().setStatusCode(config.getUnauthorizedStatus());
                }

                exchange.getRequest().mutate()
                        .header("X-User-Name", claims.getSubject())
                        .build();


            } catch (Exception e) {

                System.out.println("error"+e.toString());
                // Handle invalid JWT
                exchange.getResponse().setStatusCode(config.getUnauthorizedStatus());
                return exchange.getResponse().setComplete();
            }

            // Proceed with the request if the token is valid
            return chain.filter(exchange);
        };
    }
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    private String extractTokenFromCookie(ServerHttpRequest request) {
        List<HttpCookie> cookies = request.getCookies().get("Authorization");
        if (cookies != null && !cookies.isEmpty()) {
            return cookies.get(0).getValue();
        }
        return null; // Token not found
    }

    public static class Config {
        private org.springframework.http.HttpStatus unauthorizedStatus = org.springframework.http.HttpStatus.UNAUTHORIZED;

        public org.springframework.http.HttpStatus getUnauthorizedStatus() {
            return unauthorizedStatus;
        }

        public void setUnauthorizedStatus(org.springframework.http.HttpStatus unauthorizedStatus) {
            this.unauthorizedStatus = unauthorizedStatus;
        }
    }
}
