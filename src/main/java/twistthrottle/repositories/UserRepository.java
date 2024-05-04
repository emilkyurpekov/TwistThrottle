package twistthrottle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twistthrottle.models.entities.User;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByEmail(String email);

    boolean existsUserByPassword(String password);

}
