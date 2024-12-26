package org.MIFI;

import lombok.Getter;
import org.MIFI.entity.Category;
import org.MIFI.entity.Transaction;
import org.MIFI.entity.User;
import org.MIFI.service.CategoryService;
import org.MIFI.service.TransactionService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class Wallet {
    private User user;
    private double balance;
    private Map<Category, Double> balances = new HashMap<>();

    private final CategoryService categoryService;
    private final TransactionService transactionService;

    public Wallet(CategoryService categoryService, TransactionService transactionService) {
        this.categoryService = categoryService;
        this.transactionService = transactionService;
    }


    private void calculateBalance() {
        this.balances.clear();
        for (Category c : user.getCategories()) {
            this.balances.put(c, c.getTransactions().stream().map(Transaction::getMoney).reduce(0., Double::sum));
        }
        this.balance = this.balances.values().stream().reduce(0., Double::sum);
    }

    public void reload(User user) {
        this.user = user;
        calculateBalance();
    }

    public void addCategory(Category category) {
        categoryService.addCategory(category);
    }

    public void addTransaction(Transaction transaction) {
        transactionService.addTransaction(transaction);
    }
}

