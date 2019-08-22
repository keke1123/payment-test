package gh.shin.entity.repo;

import gh.shin.entity.AccountEnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends JpaRepository<AccountEnt, Long> {
}
