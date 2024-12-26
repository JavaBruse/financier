package org.MIFI;

import org.MIFI.entity.Category;
import org.MIFI.entity.Transaction;
import org.MIFI.entity.User;
import org.MIFI.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
    public void run(String... args) {
        System.out.println("Добро пожаловать в приложение Финансы");
        while (true) {
            if (!authorized) {
                auth();
            } else {
                worker();
            }
        }
    }

    private void auth() {
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
            System.out.println("Неверное имя пользователя или пороль.");
        }
    }

    private void worker() {
        String command = scanner.nextLine();
        command = command.toLowerCase();

        switch (command) {
            case "exit":
                authorized = false;
                user = null;
                return;
            case "all":
                for (Category c : user.getCategories()) {
                    System.out.println(c);
                }
                break;
            case "balance":
                System.out.println(wallet.getBalance());
                break;
            case "cat":
                for (Map.Entry<Category, Double> v : wallet.getBalances().entrySet()) {
                    System.out.println(
                            "Категория: \"" + v.getKey().getName() +
                                    "\" лимит: " + v.getKey().getLimit() +
                                    " баланс: " + v.getValue() +
                                    " итого, осталось: " + (v.getKey().getLimit() - v.getValue()));
                }
                break;
            case "addc":
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
            case "addt":
                System.out.print("Название категории: ");
                String catName = scanner.nextLine();
                System.out.print("Описание транзакции: ");
                String description = scanner.nextLine();
                System.out.print("Сумма по транзакции: ");
                String summ = scanner.nextLine();
                Transaction transaction = new Transaction();
                transaction.setMoney(Double.parseDouble(summ));
                transaction.setUser(user);
                transaction.setCategory(user.getCategories().stream().filter(x -> x.getName().equals(catName)).findFirst().get());
                transaction.setDescription(description);
                wallet.addTransaction(transaction);
                reloadWallet();
                break;
            default:
                System.err.println("Команда: " + command + " не известна.");
        }
    }

    private void reloadWallet() {
        user = userService.getUser(user.getName());
        wallet.reload(userService.getUser(user.getName()));
    }
}

