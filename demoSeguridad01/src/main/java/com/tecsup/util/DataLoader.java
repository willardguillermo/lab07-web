package com.tecsup.util;

import com.tecsup.model.Role;
import com.tecsup.model.User;
import com.tecsup.repository.RoleRepository;
import com.tecsup.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initData(UserRepository userRepo,
                               RoleRepository roleRepo,
                               PasswordEncoder encoder) {
        return args -> {
            // Crear roles si no existen
            Role roleUser = getOrCreateRole(roleRepo, "ROLE_USER");
            Role roleAdmin = getOrCreateRole(roleRepo, "ROLE_ADMIN");
            Role roleManager = getOrCreateRole(roleRepo, "ROLE_MANAGER");

            // Crear usuarios, o actualizar su contraseña si cambió (encriptada con BCrypt)
            createOrUpdateUser(userRepo, encoder, "user", "user2026", roleUser);
            createOrUpdateUser(userRepo, encoder, "admin", "admin2026", roleAdmin);
            createOrUpdateUser(userRepo, encoder, "manager", "manager2026", roleManager);

            System.out.println("✔ Datos iniciales cargados correctamente");
        };
    }

    private Role getOrCreateRole(RoleRepository roleRepo, String name) {
        return roleRepo.findByName(name)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName(name);
                    return roleRepo.save(r);
                });
    }

    private void createOrUpdateUser(UserRepository userRepo, PasswordEncoder encoder,
                                    String username, String rawPassword, Role role) {
        User user = userRepo.findByUsername(username).orElseGet(User::new);
        user.setUsername(username);
        if (user.getPassword() == null || !encoder.matches(rawPassword, user.getPassword())) {
            user.setPassword(encoder.encode(rawPassword));
        }
        user.setRoles(Set.of(role));
        userRepo.save(user);
    }
}
