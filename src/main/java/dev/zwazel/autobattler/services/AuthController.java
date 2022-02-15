package dev.zwazel.autobattler.services;

import dev.zwazel.autobattler.classes.utils.EnumUserRole;
import dev.zwazel.autobattler.classes.utils.User;
import dev.zwazel.autobattler.classes.utils.UserRole;
import dev.zwazel.autobattler.classes.utils.database.repositories.UserRepository;
import dev.zwazel.autobattler.classes.utils.database.repositories.UserRoleRepository;
import dev.zwazel.autobattler.security.jwt.JwtUtils;
import dev.zwazel.autobattler.security.payload.request.LoginRequest;
import dev.zwazel.autobattler.security.payload.request.SignupRequest;
import dev.zwazel.autobattler.security.payload.response.MessageResponse;
import dev.zwazel.autobattler.security.payload.response.UserInfoResponse;
import dev.zwazel.autobattler.security.services.UserDetailsImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
@Controller
public class AuthController {
    final AuthenticationManager authenticationManager;
    final UserRepository userRepository;
    final UserRoleRepository roleRepository;
    final PasswordEncoder encoder;
    final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, UserRoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid LoginRequest loginRequest, BindingResult result, Model model, HttpServletResponse response) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid username or password, or something else idk!"));
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setLastLogin(new Date());

            // old accounts getting updated, so value might not be true, but at least we have one
            if (user.getAccountCreated() == null) {
                user.setAccountCreated(user.getLastLogin());
            }

            userRepository.save(user);

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/secured/home.html")).header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new UserInfoResponse(userDetails.getId(),
                            userDetails.getUsername(),
                            roles));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid username or password"));
    }

    @PostMapping("/signup")
    public String registerUser(@Valid SignupRequest signUpRequest, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:/signup?error=form";
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return "redirect:/signup?error=username";
        }
        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<UserRole> roles = new HashSet<>();
        if (strRoles == null) {
            UserRole userRole = roleRepository.findByName(EnumUserRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        UserRole adminRole = roleRepository.findByName(EnumUserRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    default -> {
                        UserRole userRole = roleRepository.findByName(EnumUserRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }
        user.setRoles(roles);

        userRepository.save(user);
//        return ResponseEntity.ok().body(new MessageResponse("User registered successfully!"));
        return "redirect:/signin";
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}