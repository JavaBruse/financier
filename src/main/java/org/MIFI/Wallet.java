package org.MIFI;

import lombok.Getter;
import org.MIFI.entity.Category;
import org.MIFI.entity.Transaction;
import org.MIFI.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Wallet {

    double getBalance();

    Map<Category, Double> getBalances();

    void reload();

    final class WalletDefault implements Wallet {
        private final User user;
        private List<Category> categories;
        private double balance;
        private Map<Category, Double> balances = new HashMap<>();

        public WalletDefault(User user) {
            this.user = user;
            this.categories = user.getCategories().stream().toList();
            calculateBalance();
        }

        private void calculateBalance() {
            balances.clear();
            for (Category c : categories) {
                balances.put(c, c.getTransactions().stream().map(Transaction::getMoney).reduce(0., Double::sum));
            }
            this.balance = this.balances.values().stream().reduce(0., Double::sum);
        }

        @Override
        public double getBalance() {
            return this.balance;
        }

        @Override
        public Map<Category, Double> getBalances() {
            return balances;
        }

        @Override
        public void reload() {
            calculateBalance();
        }
    }
}
