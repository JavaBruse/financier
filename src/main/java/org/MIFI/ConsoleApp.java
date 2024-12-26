package org.MIFI;

import org.MIFI.entity.Category;
import org.MIFI.service.ServiceMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleApp implements CommandLineRunner {

    @Autowired
    ServiceMaster serviceMaster;

    @Autowired
    public ConsoleApp(ServiceMaster serviceMaster) {
        this.serviceMaster = serviceMaster;
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
        if (serviceMaster.validateUser(name, password)) {
            System.out.println("Привет " + name);
            authorized = true;
        } else {
            System.out.println("Неверное имя пользователя или пороль.");
        }
    }

    private void worker() {
        String command = scanner.nextLine();

        switch (command) {
            case "exit":
                authorized = false;
                return;
            case "all":
                for (Category c : serviceMaster.getCategories()) {
                    System.out.println(c);
                }
                break;
            default:
                System.err.print("Команда: " + command + " не известна.");
        }
    }
}

