package kpo.restaurant.repos;

import kpo.restaurant.domain.Session;
import kpo.restaurant.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SessionRepository extends JpaRepository<Session, Long> {

    Session findFirstByUser(User user);

}
