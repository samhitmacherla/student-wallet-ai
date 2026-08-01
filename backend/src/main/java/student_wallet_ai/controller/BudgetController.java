package student_wallet_ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import student_wallet_ai.service.BudgetService;
import java.util.Map;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping("/{userId}")
    public Map<String, Object> getSurvivalInfo(@PathVariable Long userId) {
        return budgetService.getSurvivalInfo(userId);
    }
}