package vn.bin.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.bin.jobhunter.domain.Company;
import vn.bin.jobhunter.domain.User;
import java.util.List;

@Repository
public interface UserReposiriory extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);

    List<User> findByCompany(Company company);

    boolean existsByEmail(String email);

    User findByRefreshTokenAndEmail(String token, String email);
}
