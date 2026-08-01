package student_wallet_ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import student_wallet_ai.model.Transaction;
import student_wallet_ai.service.TransactionService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class WalletController {

    @Autowired
    private TransactionService transactionService;

    // Save a transaction
    @PostMapping
    public Transaction saveTransaction(@RequestBody Transaction transaction) {
        return transactionService.saveTransaction(transaction);
    }

    // Get all transactions
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    // Get this month's transactions
    @GetMapping("/month")
    public List<Transaction> getThisMonthTransactions() {
        return transactionService.getThisMonthTransactions();
    }

    // Get monthly summary by category
    @GetMapping("/summary")
    public Map<String, Double> getMonthlySummary() {
        return transactionService.getMonthlySummary();
    }

    // Delete a transaction
    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return "Transaction deleted successfully";
    }
}