package org.MIFI;

import lombok.Getter;
import org.MIFI.entity.Category;
import org.MIFI.entity.Transaction;
import org.MIFI.entity.User;
import org.MIFI.exceptions.LimitIsOverException;
import org.MIFI.exceptions.NotFoundMessageException;
import org.MIFI.service.CategoryService;
import org.MIFI.service.TransactionService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
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

    public Map<Category, Double> getBalances() {
        if (balances.isEmpty()) {
            throw new LimitIsOverException("Категории не наеденеы!");
        }
        return balances;
    }

    public List<Transaction> getTransactionsByCategory(String categoryName) {
        try {
            List<Transaction> list = user.getCategories().stream().filter(category -> category.getName().equals(categoryName)).findFirst().get().getTransactions().stream().toList();
            if (list.isEmpty()) {
                throw new NotFoundMessageException("Транзакции не наедены!");
            }
            return list;
        } catch (Exception e) {
            throw new NotFoundMessageException("Категории не существует!");
        }
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
        Double balancesCat = balances.get(transaction.getCategory());
        Double limitByCat = balances.keySet().stream().filter(c -> c.equals(transaction.getCategory())).findFirst().get().getLimit();
        if (limitByCat >= balancesCat + transaction.getMoney()) {
            transactionService.addTransaction(transaction);
        } else {
            throw new LimitIsOverException("Лимит превышен, транзакция не может быть проведена.");
        }
    }
}

