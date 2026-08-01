package student_wallet_ai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import student_wallet_ai.model.User;
import student_wallet_ai.repository.TransactionRepository;
import student_wallet_ai.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getSurvivalInfo(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Map<String, Object> result = new HashMap<>();

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            double budget = user.getMonthlyBudget();

            LocalDateTime start = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
            LocalDateTime end = LocalDateTime.now();

            double spent = transactionRepository
                    .findByDateBetween(start, end)
                    .stream()
                    .mapToDouble(t -> t.getAmount())
                    .sum();

            double remaining = budget - spent;
            int daysLeft = YearMonth.now().lengthOfMonth() - LocalDateTime.now().getDayOfMonth();
            double dailyLimit = daysLeft > 0 ? remaining / daysLeft : 0;

            result.put("monthlyBudget", budget);
            result.put("totalSpent", spent);
            result.put("remaining", remaining);
            result.put("daysLeft", daysLeft);
            result.put("dailyLimit", Math.round(dailyLimit * 100.0) / 100.0);
            result.put("message", "You have ₹" + Math.round(dailyLimit) + "/day left for " + daysLeft + " days");
        }
        return result;
    }
}