package com.sports.manager.Controller;

import com.sports.manager.Dto.SignupDto;
import com.sports.manager.Dto.SignupResponseDto;
import com.sports.manager.Dto.UpdateNameDto;
import com.sports.manager.Entity.Admin;
import com.sports.manager.Entity.Player;
import com.sports.manager.Service.AuthService;
import com.sports.manager.Utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtil;

    public AuthController(AuthService authService, PasswordEncoder passwordEncoder, JwtUtils jwtUtil) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    @GetMapping("/")
    public String testApi(){
        return "Hi hello namasthe";
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupDto signupDto) {
        String role = signupDto.getRole();

        if ("admin".equalsIgnoreCase(role)) {
            Admin admin = authService.registerAdmin(signupDto);

            SignupResponseDto response = new SignupResponseDto(
                    admin.getId(),
                    admin.getFirstName(),
                    admin.getLastName(),
                    admin.getEmail(),
                    "admin"
            );
            return ResponseEntity.status(201).body(response);

        } else if ("player".equalsIgnoreCase(role)) {
            Player player = authService.registerPlayer(signupDto);

            SignupResponseDto response = new SignupResponseDto(
                    player.getId(),
                    player.getFirstName(),
                    player.getLastName(),
                    player.getEmail(),
                    "player"
            );
            return ResponseEntity.status(201).body(response);
        }

        // if invalid role
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SignupDto loginDto) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();

        // 1. Check Admin first
        Optional<Admin> adminOpt = authService.findAdminByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();

            if (!passwordEncoder.matches(password, admin.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            String token = jwtUtil.generateToken(admin.getEmail(), "admin");
            SignupResponseDto response = new SignupResponseDto(
                    admin.getId(),
                    admin.getFirstName(),
                    admin.getLastName(),
                    admin.getEmail(),
                    "admin"
            );

            return ResponseEntity.ok(Map.of("token", token, "user", response));
        }

        // 2. Check Player if Admin not found
        Optional<Player> playerOpt = authService.findPlayerByEmail(email);
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();

            if (!passwordEncoder.matches(password, player.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            String token = jwtUtil.generateToken(player.getEmail(), "PLAYER");
            SignupResponseDto response = new SignupResponseDto(
                    player.getId(),
                    player.getFirstName(),
                    player.getLastName(),
                    player.getEmail(),
                    "player"
            );

            return ResponseEntity.ok(Map.of("token", token, "user", response));
        }

        // 3. User not found
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
    }


    @PutMapping("/update-names")
    public ResponseEntity<?> updateName(@RequestBody UpdateNameDto dto) {
        try {
            Optional<?> user = Optional.empty();

            if ("admin".equalsIgnoreCase(dto.getRole())) {
                user = authService.findAdminById(dto.getId());
            } else if ("player".equalsIgnoreCase(dto.getRole())) {
                user = authService.findPlayerById(dto.getId());
            }

            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "User not found"));
            }

            if (user.get() instanceof Admin admin) {
                admin.setFirstName(dto.getFirstName() != null ? dto.getFirstName() : admin.getFirstName());
                admin.setLastName(dto.getLastName() != null ? dto.getLastName() : admin.getLastName());
                authService.updateAdmin(admin);
                return ResponseEntity.ok(Map.of("message", "Names updated successfully", "user", admin));
            }

            if (user.get() instanceof Player player) {
                player.setFirstName(dto.getFirstName() != null ? dto.getFirstName() : player.getFirstName());
                player.setLastName(dto.getLastName() != null ? dto.getLastName() : player.getLastName());
                authService.updatePlayer(player);
                return ResponseEntity.ok(Map.of("message", "Names updated successfully", "user", player));
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid role"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while updating names"));
        }
    }


}
