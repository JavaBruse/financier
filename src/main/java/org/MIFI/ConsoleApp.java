package org.MIFI;

import org.MIFI.entity.Category;
import org.MIFI.entity.Transaction;
import org.MIFI.entity.User;
import org.MIFI.exceptions.LimitIsOverException;
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
                    printHelp();
                    entrance();
                } else {
                    worker();
                }
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                if (authorized) reloadWallet();
                Thread.sleep(100);
            }
        }
    }

    private void entrance() throws RuntimeException {
        String command = scanner.nextLine();
        switch (command) {
            case "help":
                break;
            case "auth":
                auth();
                break;
            case "reg":
                registration();
                break;
            case "exit":
                System.exit(0);
                break;
            default:
                System.err.println("Команда: " + command + " не известна.");
        }
    }

    private void printHelp() {
        if (authorized) {
            System.out.println(
                    "help           - доступные команды.\n" +
                            "balance        - общий баланс доходов и расходов.\n" +
                            "budgets        - бюджет по категориям\n" +
                            "addc           - добавить категории.\n" +
                            "addt           - добавить транзакцию в категорию.\n" +
                            "gett           - получить транзакции по категории.\n" +
                            "expenses       - все расходы.\n" +
                            "income         - все доходы.\n" +
                            "calculate      - подсчет всего кошелька.\n" +
                            "exit           - выход из учетной записи.");
        } else {
            System.out.println(
                    "help           - доступные команды.\n" +
                            "auth           - авторизация по логину,паролю.\n" +
                            "reg            - регистрация нового пользователя\n" +
                            "exit           - выход их приложения");
        }
    }

    private void registration() throws RuntimeException {
        System.out.print("Придумайте login: ");
        String name = scanner.nextLine();
        if (userService.userExistByName(name)) {
            throw new LimitIsOverException("Пользователь существует, попробуйте войти!");
        }
        System.out.print("Придумайте password: ");
        String password = scanner.nextLine();
        user = new User();
        user.setName(name);
        user.setPassword(password);
        userService.addUser(user);
        user = userService.getUser(user.getName());
        wallet.reload(user);
        authorized = true;
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
                budgets();
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
                calculate();
                break;
            case "help":
                printHelp();
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
            System.out.println("    " + c.getName());
            for (Transaction t : c.getTransactions()) {
                System.out.print("          " + t);
            }
        }
    }

    private double getSum(List<Category> list) {
        return list.stream().flatMap(category -> category.getTransactions().stream()).mapToDouble(Transaction::getMoney).sum();
    }

    private void budgets() {
        Map<Category, Double> map = wallet.getBalances();
        System.out.println("Бюджет по категориям: ");
        for (Map.Entry<Category, Double> v : map.entrySet()) {
            if (v.getKey().getLimit() == 0) continue;
            System.out.println("    " + v.getKey().getName() + ": " + v.getKey().getLimit() +
                    " сумма по транзакциям: " + v.getValue() +
                    " итого, осталось: " + (v.getKey().getLimit() + v.getValue()));
        }
    }

    private void calculate() {
        try {
            System.out.println("Общий доход: " + getSum(wallet.getIncome()));
            System.out.println("Доходы по категориям: ");
            printCategoryAndTransactions(wallet.getIncome());
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
        try {
            System.out.println("Общие раходы: " + getSum(wallet.getExpenses()));
            System.out.println("Расходы по категориям: ");
            printCategoryAndTransactions(wallet.getExpenses());
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
        try {
            budgets();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}

