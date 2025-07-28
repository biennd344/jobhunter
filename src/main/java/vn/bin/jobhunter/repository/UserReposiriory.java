package vn.bin.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.bin.jobhunter.domain.User;
import java.util.List;

@Repository
public interface UserReposiriory extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
