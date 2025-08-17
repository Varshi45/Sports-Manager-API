package com.sports.manager.Service;


import com.sports.manager.Dto.SignupDto;
import com.sports.manager.Entity.Admin;
import com.sports.manager.Entity.Player;
import com.sports.manager.Repository.AdminRepository;
import com.sports.manager.Repository.PlayerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, PlayerRepository playerRepository) {
        this.adminRepository = adminRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private final PlayerRepository playerRepository;


    public Admin registerAdmin(SignupDto signupDto){
        Admin admin = new Admin();
        admin.setFirstName(signupDto.getFirstName());
        admin.setLastName(signupDto.getLastName());
        admin.setEmail(signupDto.getEmail());
        admin.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        return adminRepository.save(admin);
    }

    public Player registerPlayer(SignupDto signupDto) {
        Player player = new Player();
        player.setFirstName(signupDto.getFirstName());
        player.setLastName(signupDto.getLastName());
        player.setEmail(signupDto.getEmail());
        player.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        return playerRepository.save(player);
    }



    public Optional<Admin> findAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Optional<Player> findPlayerByEmail(String email) {
        return playerRepository.findByEmail(email);
    }

    public Optional<?> findAdminById(int id) {
        return adminRepository.findById(id);
    }

    public Optional<?> findPlayerById(int id) {
        return adminRepository.findById(id);
    }

    public void updateAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    public void updatePlayer(Player player){
        playerRepository.save(player);
    }
}
