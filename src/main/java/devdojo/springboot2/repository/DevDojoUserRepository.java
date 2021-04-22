package devdojo.springboot2.repository;

import devdojo.springboot2.domain.DevDojoUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevDojoUserRepository extends JpaRepository<DevDojoUser, Long> {
    DevDojoUser findByUsername(String name);
}
