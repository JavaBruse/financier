package org.MIFI;

import org.MIFI.entity.Transaction;
import org.MIFI.entity.User;
import org.MIFI.repository.CategoryRepository;
import org.MIFI.repository.TransactionRepository;
import org.MIFI.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class test {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void testFindCategoriesByUserId() {


        Long userId = 1L; // ID пользователя

        List<User> users = userRepository.findAll();
        List<Transaction> transactions = transactionRepository.findAll();

        System.out.println(transactions);
        assertEquals(10, transactions.size());
        // Дополнительные проверки на значения категорий
    }

}