package nguyenhuuvu.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import nguyenhuuvu.entity.RoleEntity;
import nguyenhuuvu.entity.UserEntity;
import nguyenhuuvu.repository.UserRepository;
import nguyenhuuvu.service.impl.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil implements Serializable {
    @Value("${nguyenhuuvu.system.jwt.secret}")
    private String secret;

    final UserRepository userRepository;

    private static final String AUTHORITIES_KEY = "auth";

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public boolean isTokenExpired(String token) {
        final Date dateInToken = getExpirationDateFromToken(token);
        return dateInToken.before(new Date());
    }

    public String generateToken(Authentication userDetails) {
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return doGenerateToken(userDetails.getName(), authorities);
    }

    public String generateToken(String username) {
        UserEntity user = userRepository.findUserEntityByUsername(username);
        String authorities = user.getRoles().stream()
                .map(RoleEntity::getName)
                .collect(Collectors.joining(","));
        return doGenerateToken(username, authorities);
    }


    public String doGenerateToken(String subject, String granted) {
        return Jwts.builder()
                    .claim(AUTHORITIES_KEY, granted)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + Constant.JWT_TOKEN_VALIDITY))
                    .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", new ArrayList<>());

        return new UsernamePasswordAuthenticationToken(principal, token, new ArrayList<>());
    }
}
