package dev.zwazel.autobattler.services;

import dev.zwazel.autobattler.classes.enums.UnitTypes;
import dev.zwazel.autobattler.classes.model.UnitModel;
import dev.zwazel.autobattler.classes.model.User;
import dev.zwazel.autobattler.classes.model.UserRole;
import dev.zwazel.autobattler.classes.utils.EnumUserRole;
import dev.zwazel.autobattler.classes.utils.database.repositories.FormationEntityRepository;
import dev.zwazel.autobattler.classes.utils.database.repositories.UnitModelRepository;
import dev.zwazel.autobattler.classes.utils.database.repositories.UserRepository;
import dev.zwazel.autobattler.classes.utils.database.repositories.UserRoleRepository;
import dev.zwazel.autobattler.security.jwt.JwtUtils;
import dev.zwazel.autobattler.security.payload.request.LoginRequest;
import dev.zwazel.autobattler.security.payload.request.SignupRequest;
import dev.zwazel.autobattler.security.payload.response.MessageResponse;
import dev.zwazel.autobattler.security.payload.response.UserInfoResponse;
import dev.zwazel.autobattler.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/api/auth")
@Controller
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final UnitModelRepository unitModelRepository;
    private final FormationEntityRepository formationEntityRepository;
    private final JwtUtils jwtUtils;

    @Value("${zwazel.app.maximumAmountUnitsPerUser}")
    private int MAXIMUM_AMOUNT_UNITS;

    @Value("${zwazel.app.maximumAmountFormationsPerUser}")
    private int MAXIMUM_AMOUNT_FORMATIONS;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, UserRoleRepository roleRepository, PasswordEncoder encoder, UnitModelRepository unitModelRepository, FormationEntityRepository formationEntityRepository, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.unitModelRepository = unitModelRepository;
        this.formationEntityRepository = formationEntityRepository;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {
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

            userRepository.save(user);

            return getResponseEntityWithCookie(userDetails, loginRequest.getRememberMeTime().getTime());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid username or password"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid username or password, or something else idk!"));
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Username is already taken!"));
        }

        if (!signUpRequest.getConfirmPassword().equals(signUpRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Passwords do not match!"));
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

//        user = userRepository.save(user);

        // add starter units to user
        user.addUnit(new UnitModel(1, UnitTypes.MY_FIRST_UNIT, user));
        user.addUnit(new UnitModel(1, UnitTypes.MY_FIRST_UNIT, user));
        user.addUnit(new UnitModel(1, UnitTypes.MY_FIRST_UNIT, user));

        userRepository.save(user);

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signUpRequest.getUsername(), signUpRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return getResponseEntityWithCookie(userDetails, signUpRequest.getRememberMeTime().getTime());
    }

    private ResponseEntity<?> getResponseEntityWithCookie(UserDetailsImpl userDetails, long jwtExpirationMs) {
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails, jwtExpirationMs);
        List<String> userRoles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userRoles));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @GetMapping("/checkIfLoggedIn")
    public ResponseEntity<?> checkIfLoggedIn(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        boolean valid = jwtUtils.validateJwtToken(jwt);
        if (valid) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            try {
                User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Error: User not found."));
                Long amountUnits = unitModelRepository.countByUser(user);
                Long amountFormations = formationEntityRepository.countByUser(user);
                return ResponseEntity.ok().body(new MessageResponse("{" +
                        "\"username\":" + "\"" + user.getUsername() + "\"" +
                        ",\"id\":" + user.getId() +
                        ",\"amountUnits\":" + amountUnits +
                        ",\"maxAmountUnits\":" + MAXIMUM_AMOUNT_UNITS +
                        ",\"amountFormations\":" + amountFormations +
                        ",\"maxAmountFormations\":" + MAXIMUM_AMOUNT_FORMATIONS +
                        "}"));
            } catch (RuntimeException e) {
                ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
                return ResponseEntity.status(201).header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(new MessageResponse("You've been signed out!"));
            }
        } else {
            return ResponseEntity.status(201).body(new MessageResponse("You're not logged in!"));
        }
    }
}