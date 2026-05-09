import com.eightthreads.backend.common.enums.Role;
import com.eightthreads.backend.entity.User;
import com.eightthreads.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@8threads.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@8threads.com");
            admin.setFullName("System Admin");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Default Admin account created: admin@8threads.com");
        }
    }
}