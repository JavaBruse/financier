Приложение ФИНАНСИСТ

## Актуальная версия: [financier-1.0.jar](target/financier-1.0.jar)
Или можете собрать проект командой:

```shell
mvn clean install
```

Для использвоания приложения, следует иметь в виду, что тип транзакции определеяется по знаку. 

Если число отрицательное, то это расходы, если положительное то это доходы.
```shell
Транзакция: Премия, Деньги: 3000.0, Дата: 29.12.24г.
Транзакция: Заплатил коммуналку, Деньги: -3000.0, Дата: 29.12.24г.
```
1. Хранение данных
    Все данные должны храниться в памяти приложения.

В приложении подключена БД, PostgreSQL, по org.h2.Driver, url: jdbc:h2:mem:mydatabase;MODE=PostgreSQL
```yaml
  datasource:
    driverClassName: org.h2.Driver
    url: jdbc:h2:mem:mydatabase;MODE=PostgreSQL
```
```xml
<dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
</dependency>
```
Работы с БД обеспечивается в Spring Data JPA.
```xml
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
БД мигнарция осуществляется с помощью:
```xml
<dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
</dependency>
```

2. Авторизация пользователей

Реализовать функциональность для авторизации пользователей по логину и паролю. Приложение должно поддерживать несколько пользователей.

Реализована авторизация и регистрация нового пользователя: 
```java
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
```
Обеспечивается командами:
```shell
auth           - авторизация по логину,паролю.
reg            - регистрация нового пользователя
```
3. Функционал управления финансами

Разработать логику для добавления доходов и расходов. Пользователь должен иметь возможность создавать категории для планирования бюджета.
Предусмотреть функциональность для установления бюджета на каждую категорию расходов.

```shell
help           - доступные команды.
balance        - общий баланс доходов и расходов.
budgets        - бюджет по категориям
addc           - добавить категории.
addt           - добавить транзакцию в категорию.
gett           - получить транзакции по категории.
expenses       - все расходы.
income         - все доходы.
calculate      - подсчет всего кошелька.
trans          - перевод другому пользователю.
exit           - выход из учетной записи.
```
При создании категории, предложит ввести лимит по ней:
```shell
addc
Имя категории: Еда
Лимит по категории: 4000
```
если лимит 0, тогда категория будет принимать только доходной, например категория зарплата, где будут доходы, премии и прочие.
```shell
Общий доход: 63000.0
Доходы по категориям:
    Зарплата
            Транзакция: зарплата за январь, Деньги: 20000.0, Дата: 29.12.24г.
            Транзакция: Зарплата за февраль, Деньги: 40000.0, Дата: 29.12.24г.
            Транзакция: Премия, Деньги: 3000.0, Дата: 29.12.24г.
```
4. Работа с кошельком пользователя:

Привязать кошелёк к авторизованному пользователю. Кошелёк должен хранить информацию о текущих финансах и всех операциях (доходах и расходах).
Сохранять установленный бюджет по категориям.

Класс кошелек создан и хранит пользователя, информацию о балансе, балансе по категориям. В сущности пользователь хранится все его категории,
у каждой категории все её транзакции.
```java
public class Wallet {
    private User user;
    private double balance;
    private Map<Category, Double> balances = new HashMap<>();
```
Бюджет по категориям сохраняется в категориях и называется limit
```java
public class Category {
    private Double limit;
```
5. Вывод информации:

Реализовать возможность отображения общей суммы доходов и расходов, а также данных по каждой категории.
Выводить информацию о текущем состоянии бюджета для каждой категории, а также оставшийся лимит.
Поддерживать вывод информации в терминал или в файл.

В БД сохранена синтетика, данная в задании:
```jql
INSERT INTO users (name, password)
VALUES ('bob', '123'),
       ('john', '123');
INSERT INTO categories (user_id, name, max_limit)
VALUES (1, 'Еда', 4000.00),
       (1, 'Развлечения', 3000.00),
       (1, 'Коммунальные услуги', 2500.00),
       (1, 'Прочие расходы', 10000.00),
       (1, 'Зарплата', 0.00);
INSERT INTO transactions (user_id, category_id, description, type, money)
VALUES
    (1, 1, 'Купил еды', 'OUT', -300.00),
    (1, 1, 'Купил еды', 'OUT', -500.00),
    (1, 2, 'Погулял в клубе', 'OUT', -3000.00),
    (1, 3, 'Заплатил коммуналку', 'OUT', -3000.00),
    (1, 4, 'За такси', 'OUT', -1500.00),
    (1, 5, 'зарплата за январь', 'IN', 20000.00),
    (1, 5, 'Зарплата за февраль', 'IN', 40000.00),
    (1, 5, 'Премия', 'IN', 3000.00);
```
После авторизации и вводе команды calculate, будет выведен требуемый результат: 
```shell
calculate
Общий доход: 63000.0
Доходы по категориям:
    Зарплата
            Транзакция: зарплата за январь, Деньги: 20000.0, Дата: 29.12.24г.
            Транзакция: Зарплата за февраль, Деньги: 40000.0, Дата: 29.12.24г.
            Транзакция: Премия, Деньги: 3000.0, Дата: 29.12.24г.
Общие раходы: -8300.0
Расходы по категориям:
    Коммунальные услуги
            Транзакция: Заплатил коммуналку, Деньги: -3000.0, Дата: 29.12.24г.
    Прочие расходы
            Транзакция: За такси, Деньги: -1500.0, Дата: 29.12.24г.
    Развлечения
            Транзакция: Погулял в клубе, Деньги: -3000.0, Дата: 29.12.24г.
    Еда
            Транзакция: Купил еды, Деньги: -300.0, Дата: 29.12.24г.
            Транзакция: Купил еды, Деньги: -500.0, Дата: 29.12.24г.
Бюджет по категориям:
    Коммунальные услуги: 2500.0 сумма по транзакциям: -3000.0 итого, осталось: -500.0
    Прочие расходы: 10000.0 сумма по транзакциям: -1500.0 итого, осталось: 8500.0
    Развлечения: 3000.0 сумма по транзакциям: -3000.0 итого, осталось: 0.0
    Еда: 4000.0 сумма по транзакциям: -800.0 итого, осталось: 3200.0

```

6. Подсчет расходов и доходов:

Разработать методы, подсчитывающие общие расходы и доходы, а также по категориям.
Поддержать возможность подсчета по нескольким выбранным категориям. Если категория не найдена, уведомлять пользователя.

Все расходы и доходы выводит по команде:

Расходы expenses
```shell
expenses
Расходы
    Коммунальные услуги
            Транзакция: Заплатил коммуналку, Деньги: -3000.0, Дата: 29.12.24г.
    Прочие расходы
            Транзакция: За такси, Деньги: -1500.0, Дата: 29.12.24г.
    Развлечения
            Транзакция: Погулял в клубе, Деньги: -3000.0, Дата: 29.12.24г.
    Еда
            Транзакция: Купил еды, Деньги: -300.0, Дата: 29.12.24г.
            Транзакция: Купил еды, Деньги: -500.0, Дата: 29.12.24г.

```
Доходы income
```shell
income
Доходы
    Зарплата
            Транзакция: зарплата за январь, Деньги: 20000.0, Дата: 29.12.24г.
            Транзакция: Зарплата за февраль, Деньги: 40000.0, Дата: 29.12.24г.
            Транзакция: Премия, Деньги: 3000.0, Дата: 29.12.24г.

```
7. Проверка вводимых данных:

Валидация пользовательского ввода и уведомление о некорректных данных.

Весь пользовательский ввод проверяется, если ошибка, то выдает exception, которая перехватывается в самом начале приложения, 
где печатается причина, и ожидается повторный ввод команды.

```java
@Override
    public void run(String... args) throws InterruptedException {
        System.out.println("Добро пожаловать в приложение Финансы");
        while (true) {
            try {
                if (!authorized) { // если не авторизован, то выводит возможные команды, и отправляет на варинты регистрации, авторизации или вызода из приложения.
                    printHelp();
                    entrance();
                } else { // если авторизован, то работа с кошельком.
                    worker();
                }
            } catch (RuntimeException e) {
                System.err.println(e.getMessage()); // Выводится ошибка, ввода или по другой причине.
                Thread.sleep(100); // сон основного потока, т.к. err и out это разные потоки, и без паузы, происходит наложение текста в консоли.
            }
        }
    }
```

8. Оповещения:

Оповещать пользователя, если превышен лимит бюджета по категории или расходы превысили доходы.

Пользователь будет оповещен, если попытается провести транзакцию по категории, лимит по каторой исчерпан.

Есть две ошибки, одна на превышение доходов, над расходами, вторая превышение лимита по категории.
```java
    public void addTransaction(Transaction transaction) {
    Double balancesCat = balances.get(transaction.getCategory());
    Double limitByCat = transaction.getCategory().getLimit();
    if (limitByCat == 0) {
        if (balance < Math.abs(transaction.getMoney()) && transaction.getMoney() < 0) {
            transactionService.addTransaction(transaction);
            throw new LimitIsOverException("Расходы превысили доходы, но транзакция проведена"); // для доходной категории с 0
        }
        transactionService.addTransaction(transaction);
    }

    if (balance < Math.abs(transaction.getMoney())) { // для расходны категорий
        transactionService.addTransaction(transaction);
        throw new LimitIsOverException("Расходы превысили доходы, но транзакция проведена");
    } else if (0 < limitByCat + (balancesCat + transaction.getMoney())) {
        transactionService.addTransaction(transaction);
    } else {
        transactionService.addTransaction(transaction);
        throw new LimitIsOverException("Лимит превышен!, но транзакция проведена.");
    }
}
```

9. Сохранение данных:

При выходе из приложения сохранять данные кошелька пользователя в файл.
При авторизации загружать данные кошелька из файла. 

Все данные хранятся в БД, см.Пункт: 1.Хранение данных
10. Чтение команд пользователя в цикле:

Реализовать цикл для постоянного чтения команд пользователя. Поддержать возможность выхода из приложения.

Цикл реализован, есть 2 уровня нахождения пользователя,

1 ур. пользователь не авторизован.

2 ур. пользователь авторизован.


1. Реализация авторизации пользователей (2 балла):

Реализовано.

2. Взаимодействие с пользователем (2 балла):

Реалихзовано в командной строке

3. Управление доходами и расходами (3 балла):

Реализовано.

4. Работа с кошельком пользователя (3 балла):

Реализовано.

5. Вывод информации (4 балла):

Реализовано в командную строку.

6. Оповещения пользователя (4 балла):

Реализовано.

7. Сохранение и загрузка данных (3 балла):

Реализовано в БД.

8. Чтение команд в цикле (3 балла):

Реализовано.

9. Валидация данных (3 балла):

Реалиховано 

10. Дополнительные возможности (2 балла):

Креативные решения или дополнительные функции (например, переводы между кошельками)

Реализован функционал перевода другим пользователям, по имени пользователя.

Реализована, можно даже перевести самому себе.
```shell
trans          - перевод другому пользователю.
```

12. Разделение функционала по классам (2 балла):

Разделено.