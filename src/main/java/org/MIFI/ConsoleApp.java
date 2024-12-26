package org.MIFI;

import org.MIFI.entity.Category;
import org.MIFI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Scanner;

@Component
public class ConsoleApp implements CommandLineRunner {

    @Autowired
    UserService userService;

    @Autowired
    public ConsoleApp(UserService serviceMaster) {
        this.userService = serviceMaster;
    }

    private final Scanner scanner = new Scanner(System.in);
    private boolean authorized = false;

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
            authorized = true;
        } else {
            System.out.println("Неверное имя пользователя или пороль.");
        }
    }

    private void worker() {
        String command = scanner.nextLine();
        Wallet wallet = new Wallet.WalletDefault(userService.getUser());
        switch (command) {
            case "exit":
                authorized = false;
                return;
            case "all":
                for (Category c : userService.getCategories()) {
                    System.out.println(c);
                }
                break;
            case "balance":
                System.out.println(wallet.getBalance());
                break;
            case "balanceCat":
                for (Map.Entry<Category, Double> v : wallet.getBalances().entrySet()) {
                    System.out.println(
                            "Категория: " + v.getKey().getName() +
                                    " лимит: " + v.getKey().getLimit() +
                                    " баланс: " + v.getValue() +
                                    " итого, осталось: " + (v.getKey().getLimit() - v.getValue()));
                }
                break;
            case "addCat":

                break;
            default:
                System.err.println("Команда: " + command + " не известна.");
        }
    }
}

