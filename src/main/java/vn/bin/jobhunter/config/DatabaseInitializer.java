package vn.bin.jobhunter.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.bin.jobhunter.domain.Permission;
import vn.bin.jobhunter.domain.Role;
import vn.bin.jobhunter.domain.User;
import vn.bin.jobhunter.repository.PermissionRepository;
import vn.bin.jobhunter.repository.RoleRepository;
import vn.bin.jobhunter.repository.UserReposiriory;
import vn.bin.jobhunter.util.constant.GenderEnum;

@Service
public class DatabaseInitializer implements CommandLineRunner {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    private final UserReposiriory userReposiriory;

    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(PermissionRepository permissionRepository, RoleRepository roleRepository,
            UserReposiriory userReposiriory, PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userReposiriory = userReposiriory;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println(">>>>>STRART INIT DATABASE");
        long countPermission = this.permissionRepository.count();
        long countRoles = this.roleRepository.count();
        long countUsers = this.userReposiriory.count();
        if (countPermission == 0) {
            ArrayList<Permission> arr = new ArrayList<>();
            arr.add(new Permission("create a company ", "/api/v1/companies", "POST", "COMPANIES"));
            arr.add(new Permission("update a company ", "/api/v1/companies", "PUT", "COMPANIES"));
            arr.add(new Permission("DELETE a company ", "/api/v1/companies/{id}", "DELETE", "COMPANIES"));
            arr.add(new Permission("GET a company by id ", "/api/v1/companies/{id}", "GET", "COMPANIES"));
            arr.add(new Permission("GET companies with pagination  ", "/api/v1/companies", "GET", "COMPANIES"));

            arr.add(new Permission("create a job", "/api/v1/jobs", "POST", "JOBS"));
            arr.add(new Permission("update a job", "/api/v1/jobs", "PUT", "JOBS"));
            arr.add(new Permission("DELETE a job", "/api/v1/jobs/{id}", "DELETE", "JOBS"));
            arr.add(new Permission("GET a job by id", "/api/v1/jobs/{id}", "GET", "JOBS"));
            arr.add(new Permission("GET jobs with pagination  ", "/api/v1/jobs", "GET", "JOBS"));

            arr.add(new Permission("create a permission", "/api/v1/permissions", "POST", "PERMISSIONS"));
            arr.add(new Permission("update a permission", "/api/v1/permissions", "PUT", "PERMISSIONS"));
            arr.add(new Permission("DELETE a permission", "/api/v1/permissions/{id}", "DELETE", "PERMISSIONS"));
            arr.add(new Permission("GET a permission by id", "/api/v1/permissions/{id}", "GET", "PERMISSIONS"));
            arr.add(new Permission("GET permissions with pagination  ", "/api/v1/permissions", "GET", "PERMISSIONS"));

            arr.add(new Permission("create a resume", "/api/v1/resumes", "POST", "RESUMES"));
            arr.add(new Permission("update a resume", "/api/v1/resumes", "PUT", "RESUMES"));
            arr.add(new Permission("DELETE a resume", "/api/v1/resumes/{id}", "DELETE", "RESUMES"));
            arr.add(new Permission("GET a resume by id", "/api/v1/resumes/{id}", "GET", "RESUMES"));
            arr.add(new Permission("GET resumes with pagination  ", "/api/v1/resumes", "GET", "RESUMES"));

            arr.add(new Permission("create a role", "/api/v1/roles", "POST", "ROLES"));
            arr.add(new Permission("update a role", "/api/v1/roles", "PUT", "ROLES"));
            arr.add(new Permission("DELETE a role", "/api/v1/roles/{id}", "DELETE", "ROLES"));
            arr.add(new Permission("GET a role by id", "/api/v1/roles/{id}", "GET", "ROLES"));
            arr.add(new Permission("GET roles with pagination  ", "/api/v1/roles", "GET", "ROLES"));

            arr.add(new Permission("create a user", "/api/v1/users", "POST", "USERS"));
            arr.add(new Permission("update a user", "/api/v1/users", "PUT", "USERS"));
            arr.add(new Permission("DELETE a user", "/api/v1/users/{id}", "DELETE", "USERS"));
            arr.add(new Permission("GET a users by id", "/api/v1/users/{id}", "GET", "USERS"));
            arr.add(new Permission("GET users with pagination  ", "/api/v1/users", "GET", "USERS"));

            arr.add(new Permission("create a subscriber", "/api/v1/subscribers", "POST", "SUBCRIBERS"));
            arr.add(new Permission("update a subscriber", "/api/v1/subscribers", "PUT", "SUBCRIBERS"));
            arr.add(new Permission("DELETE a subscriber", "/api/v1/subscribers/{id}", "DELETE", "SUBCRIBERS"));
            arr.add(new Permission("GET a subscriber by id", "/api/v1/subscribers/{id}", "GET", "SUBCRIBERS"));
            arr.add(new Permission("GET subscribers with pagination  ", "/api/v1/subscribers", "GET", "SUBCRIBERS"));

            arr.add(new Permission("Download a file", "/api/v1/files", "POST", "FILES"));
            arr.add(new Permission("Upload a file ", "/api/v1/files", "GET", "FILES"));
            this.permissionRepository.saveAll(arr);
        }

        if (countRoles == 0) {
            List<Permission> allPermissions = this.permissionRepository.findAll();
            Role adminRole = new Role();
            adminRole.setName("SUPER_ADMIN");
            adminRole.setDescription("Admin thi full permissions");
            adminRole.setActive(true);
            adminRole.setPermissions(allPermissions);
            this.roleRepository.save(adminRole);

        }

        if (countUsers == 0) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setAddress("ha noi");
            adminUser.setAge(25);
            adminUser.setGender(GenderEnum.MALE);
            adminUser.setName("I'm super admin");
            adminUser.setPassword(this.passwordEncoder.encode("123456"));
            Role adminRole = this.roleRepository.findByName("SUPER_ADMIN");
            if (adminRole != null) {
                adminUser.setRole(adminRole);
                this.userReposiriory.save(adminUser);
            }

        }
        if (countPermission > 0 && countRoles > 0 && countUsers > 0) {
            System.out.println(">>>>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");

        } else {
            System.out.println(">>>>>> END INIT DATABASE");
        }

    }
}
