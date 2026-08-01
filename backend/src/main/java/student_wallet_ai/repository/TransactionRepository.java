package student_wallet_ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import student_wallet_ai.model.Transaction;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository 
    extends JpaRepository<Transaction, Long> {

    // Get all transactions between two dates
    List<Transaction> findByDateBetween(
        LocalDateTime start, 
        LocalDateTime end
    );

    // Get total spending per category
    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t " +
           "WHERE t.date BETWEEN :start AND :end " +
           "GROUP BY t.category")
    List<Object[]> findSpendingByCategory(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
}