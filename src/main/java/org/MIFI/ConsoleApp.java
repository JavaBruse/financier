package org.MIFI;

import org.MIFI.entity.Category;
import org.MIFI.service.CategoryService;
import org.MIFI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Scanner;

@Component
public class ConsoleApp implements CommandLineRunner {

    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public ConsoleApp(UserService serviceMaster, CategoryService categoryService) {
        this.userService = serviceMaster;
        this.categoryService = categoryService;
    }

    private final Scanner scanner = new Scanner(System.in);
    private boolean authorized = false;
    private String userName;

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
            this.userName = name;
        } else {
            System.out.println("Неверное имя пользователя или пороль.");
        }
    }

    private void worker() {
        String command = scanner.nextLine();
        Wallet wallet = new Wallet.WalletDefault(userService.getUser(userName));
        switch (command) {
            case "exit":
                authorized = false;
                userName = null;
                return;
            case "all":

                break;
            case "balance":
                System.out.println(wallet.getBalance());
                break;
            case "cat":
                for (Map.Entry<Category, Double> v : wallet.getBalances().entrySet()) {
                    System.out.println(
                            "Категория: " + v.getKey().getName() +
                                    " лимит: " + v.getKey().getLimit() +
                                    " баланс: " + v.getValue() +
                                    " итого, осталось: " + (v.getKey().getLimit() - v.getValue()));
                }
                break;
            case "addCat":
                System.out.print("Описание категории: ");
                String name = scanner.nextLine();
                System.out.print("Лимит по категории: ");
                String limit = scanner.nextLine();
                Category category = new Category();
                category.setName(name);
                category.setUser(userService.getUser(userName));
                category.setLimit(Double.parseDouble(limit));
                categoryService.addCategory(category);
                wallet.reload();
                break;
            default:
                System.err.println("Команда: " + command + " не известна.");
        }
    }
}

