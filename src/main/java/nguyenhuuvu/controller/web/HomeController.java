package nguyenhuuvu.controller.web;

import lombok.RequiredArgsConstructor;
import nguyenhuuvu.exception.UserHandleException;
import nguyenhuuvu.model.JwtRequest;
import nguyenhuuvu.model.JwtResponse;
import nguyenhuuvu.service.impl.JwtUserDetailsService;
import nguyenhuuvu.utils.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    final AuthenticationManagerBuilder authenticationManagerBuilder;
    final JwtTokenUtil jwtTokenUtil;
    final JwtUserDetailsService jwtUserDetailsService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // hàm này viết tạm để test
    @PostMapping("/login")
    public String createAuthenticationToken1(@ModelAttribute JwtRequest jwtRequest, HttpServletResponse response) throws UserHandleException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                jwtRequest.getEmail(), jwtRequest.getPassword()
        );
        Authentication authentication = null;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (DisabledException e) {
            return "redirect:/login?msg=login-false";
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenUtil.generateToken(authentication);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60*60*24*10);
        cookie.setSecure(true);
        response.addCookie(cookie);
        return "pages/chat";
    }


    @GetMapping
    public String homePage(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = WebUtils.getCookie(request, "token");

        return "pages/chat";
    }
}
