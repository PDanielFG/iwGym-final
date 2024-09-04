package es.uca.iw.fullstackwebapp;

import com.github.javafaker.Faker;
import es.uca.iw.fullstackwebapp.user.domain.Role;
import es.uca.iw.fullstackwebapp.user.domain.User;
import es.uca.iw.fullstackwebapp.user.services.UserManagementService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabasePopulator implements CommandLineRunner {

    UserManagementService userService;

    public DatabasePopulator(UserManagementService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        Faker faker = new Faker();

        // Creamos admin
        if (userService.count() == 0) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword("admin");
            user.setEmail("admin@uca.es");
            user.addRole(Role.ADMIN);
            userService.registerUser(user);
            userService.activateUser(user.getEmail(), user.getRegisterCode());
            System.out.println("Admin created");

            User user2 = new User();
            user2.setUsername("pepito");
            user2.setPassword("123");
            user2.setEmail("admin@uca.es");
            user2.addRole(Role.USER);
            userService.registerUser(user2);
            userService.activateUser(user2.getEmail(), user2.getRegisterCode());
            System.out.println("User created");

        }

    }
}
