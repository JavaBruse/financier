package org.MIFI;

import org.MIFI.entity.Category;
import org.MIFI.entity.Transaction;
import org.MIFI.entity.User;
import org.MIFI.exceptions.LimitIsOverException;
import org.MIFI.exceptions.NotFoundMessageException;
import org.MIFI.service.CategoryService;
import org.MIFI.service.TransactionService;
import org.springframework.stereotype.Component;

import java.util.*;

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
            throw new LimitIsOverException("Категории не наедены!");
        }
        return balances;
    }


    public double getBalance() {
        return balance;
    }

    public Category getCategory(String name) {
        Optional<Category> cat = user.getCategories().stream().filter(category -> category.getName().equals(name)).findFirst();
        if (cat.isEmpty()) {
            throw new NotFoundMessageException("Категория не наедена");
        } else {
            return cat.get();
        }
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
        Double limitByCat = transaction.getCategory().getLimit();
        if (balance < Math.abs(transaction.getMoney())) {
            transactionService.addTransaction(transaction);
            throw new LimitIsOverException("Расходы превысили доходы, но транзакция проведена");
        }
        if (0 <= limitByCat + (balancesCat + transaction.getMoney()) || (limitByCat == 0 && transaction.getMoney() > 0)) {
            transactionService.addTransaction(transaction);
        } else {
            transactionService.addTransaction(transaction);
            throw new LimitIsOverException("Лимит превышен!, но транзакция проведена.");
        }
    }

    public List<Category> getExpenses() {
        List<Category> expenses = new ArrayList<>();
        for (Map.Entry<Category, Double> v : this.balances.entrySet()) {
            expenses.add(v.getKey().getExpenses());
        }
        return checkFromInit(expenses, "Расходов нет!");
    }

    public List<Category> getIncome() {
        List<Category> expenses = new ArrayList<>();
        for (Map.Entry<Category, Double> v : this.balances.entrySet()) {
            expenses.add(v.getKey().getIncome());
        }
        return checkFromInit(expenses, "Доходов нет!");
    }

    private List<Category> checkFromInit(List<Category> list, String message) {
        try {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTransactions().isEmpty()) {
                    list.remove(list.get(i));
                    i--;
                }
            }
            if (list.isEmpty()) throw new NotFoundMessageException(message);
            return list;
        } catch (RuntimeException e) {
            throw new NotFoundMessageException(message);
        }

    }
}

