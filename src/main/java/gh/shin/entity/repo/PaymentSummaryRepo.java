package gh.shin.entity.repo;

import gh.shin.entity.PaymentSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentSummaryRepo extends JpaRepository<PaymentSummary, String> {
}
