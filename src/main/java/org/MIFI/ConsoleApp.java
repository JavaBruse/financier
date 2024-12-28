package org.MIFI;

import org.MIFI.entity.Category;
import org.MIFI.entity.Transaction;
import org.MIFI.entity.User;
import org.MIFI.exceptions.NotFoundMessageException;
import org.MIFI.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class ConsoleApp implements CommandLineRunner {

    private final UserService userService;
    private final Wallet wallet;

    public ConsoleApp(UserService userService, Wallet wallet) {
        this.userService = userService;
        this.wallet = wallet;
    }

    private final Scanner scanner = new Scanner(System.in);
    private boolean authorized = false;
    private User user;

    @Override
    public void run(String... args) throws InterruptedException {
        System.out.println("Добро пожаловать в приложение Финансы");
        while (true) {
            try {
                if (!authorized) {
                    auth();
                } else {
                    worker();
                }
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                Thread.sleep(100);
            }
        }
    }

    private void auth() throws RuntimeException {
        System.out.print("Введите login: ");
        String name = scanner.nextLine();
        System.out.print("Введите password: ");
        String password = scanner.nextLine();
        if (userService.validateUser(name, password)) {
            System.out.println("Привет " + name);
            this.authorized = true;
            user = userService.getUser(name);
            wallet.reload(user);
        } else {
            throw new NotFoundMessageException("Неверное имя пользователя или пороль.");
        }
    }

    private void worker() throws RuntimeException {
        String command = scanner.nextLine();
        command = command.toLowerCase();

        switch (command) {
            case "exit":  // выход из учетной записи
                authorized = false;
                user = null;
                return;
            case "balance": // общий баланс
                System.out.println(wallet.getBalance());
                break;
            case "budgets": // Бюджеты по всем категориям
                System.out.println("Бюджеты:");
                for (Map.Entry<Category, Double> v : wallet.getBalances().entrySet()) {
                    if (v.getKey().getLimit() == 0) continue;
                    System.out.println(v.getKey().getName() + ": " + v.getKey().getLimit() +
                            " сумма по транзакциям: " + v.getValue() +
                            " итого, осталось: " + (v.getKey().getLimit() + v.getValue()));
                }
                break;
            case "addc": // добавтиь категорию, лимит 0, условно не выделяет её в отдельную категорию.
                System.out.print("Имя категории: ");
                String name = scanner.nextLine();
                System.out.print("Лимит по категории: ");
                String limit = scanner.nextLine();
                Category category = new Category();
                category.setName(name);
                category.setUser(user);
                category.setLimit(Double.parseDouble(limit));
                wallet.addCategory(category);
                reloadWallet();
                break;
            case "addt": // добавить транзакцию в категорию
                System.out.print("Название категории: ");
                Category cat = wallet.getCategory(scanner.nextLine());
                System.out.print("Описание транзакции: ");
                String description = scanner.nextLine();
                System.out.print("Сумма по транзакции: ");
                String summ = scanner.nextLine();
                Transaction transaction = new Transaction();
                transaction.setMoney(Double.parseDouble(summ));
                transaction.setUser(user);
                transaction.setCategory(cat);
                transaction.setDescription(description);
                wallet.addTransaction(transaction);
                reloadWallet();
                break;
            case "gett": // получить транзакции по категории
                System.out.print("Название категории: ");
                String catName = scanner.nextLine();
                for (Transaction t : wallet.getTransactionsByCategory(catName)) {
                    System.out.println(t);
                }
                break;
            case "expenses": //расходы
                System.out.println("Расходы");
                printCategoryAndTransactions(wallet.getExpenses());
                break;
            case "income": //доходы
                System.out.println("Доходы");
                printCategoryAndTransactions(wallet.getIncome());
                break;
            case "calculate":

                break;
            default:
                System.err.println("Команда: " + command + " не известна.");
        }
    }

    private void reloadWallet() {
        user = userService.getUser(user.getName());
        wallet.reload(userService.getUser(user.getName()));
    }

    private void printCategoryAndTransactions(List<Category> list) {
        for (Category c : list) {
            System.out.println(c.getName());
            for (Transaction t : c.getTransactions()) {
                System.out.println(t);
            }
        }
    }
}

