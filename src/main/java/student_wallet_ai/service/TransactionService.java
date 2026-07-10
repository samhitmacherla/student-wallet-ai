package student_wallet_ai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import student_wallet_ai.model.Transaction;
import student_wallet_ai.repository.TransactionRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Save a new transaction
    public Transaction saveTransaction(Transaction transaction) {
        transaction.setDate(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    // Get all transactions
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Get this month's transactions
    public List<Transaction> getThisMonthTransactions() {
        LocalDateTime start = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        LocalDateTime end = LocalDateTime.now();
        return transactionRepository.findByDateBetween(start, end);
    }

    // Get spending summary by category this month
    public Map<String, Double> getMonthlySummary() {
        LocalDateTime start = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        LocalDateTime end = LocalDateTime.now();
        List<Object[]> results = transactionRepository.findSpendingByCategory(start, end);
        Map<String, Double> summary = new HashMap<>();
        for (Object[] row : results) {
            String category = (String) row[0];
            Double total = (Double) row[1];
            summary.put(category, total);
        }
        return summary;
    }

    // Delete a transaction
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}