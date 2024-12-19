package org.MIFI;

import org.MIFI.entity.User;
import org.MIFI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Scanner;

@Component
public class ConsoleApp implements CommandLineRunner {

    private final UserRepository userRepository;

    @Autowired
    public ConsoleApp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Добро пожаловать в приложение Финансы");
            //String command = scanner.nextLine();
            for (User user : userRepository.findAll()) {
                System.out.println(user);
            }
            return;

        }
    }
}

