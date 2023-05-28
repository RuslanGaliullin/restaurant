package kpo.restaurant.repos;

import kpo.restaurant.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    // --Commented out by Inspection (28.05.2023, 11:51):User findByUsername(String username);

    // --Commented out by Inspection (28.05.2023, 11:51):boolean existsByUsername(String username);

}
