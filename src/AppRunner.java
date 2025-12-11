import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final Payment payment;

    private static boolean isExit = false;

    private AppRunner(Payment payment) {
        this.payment = payment;

        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
    }

    public static void run() {
        Payment creditAcceptor = new MoneyAcceptor(0);
        AppRunner app = new AppRunner(creditAcceptor);
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        UniversalArray<Product> allowedProducts = getAllowedProducts();

        print("В автомате доступны:");
        showProducts(products);

        print("Баланс: " + payment.getBalance());
        showActions(allowedProducts);
        chooseAction(allowedProducts);
    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (payment.getBalance() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        print(" a - Пополнить баланс");
        print(" h - Выйти");

        String input = fromConsole().trim();
        if (input.isEmpty()) {
            print("Вы ничего не ввели. Попробуйте снова.");
            return;
        }

        String action = input.substring(0, 1);

        if ("h".equalsIgnoreCase(action)) {
            isExit = true;
            return;
        }

        if ("a".equalsIgnoreCase(action)) {
            print("Введите сумму для пополнения:");
            String sumInput = fromConsole().trim();
            if (sumInput.isEmpty()) {
                print("Вы не ввели сумму. Пополнение отменено.");
                return;
            }
            try {
                int amount = Integer.parseInt(sumInput);
                if (amount <= 0) {
                    print("Сумма должна быть положительной. Пополнение отменено.");
                    return;
                }
                payment.addPayment(amount);
                print("Баланс пополнен на " + amount);
            } catch (NumberFormatException e) {
                print("Некорректная сумма. Пополнение отменено.");
            }
            return;
        }

        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    if (payment.deduct(products.get(i).getPrice())) {
                        print("Вы купили " + products.get(i).getName());
                    } else {
                        print("Недостаточно средств!");
                    }
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            print("Недопустимая буква. Попробуйте еще раз.");
        }
    }

    private void showActions(UniversalArray<Product> products) {
        if (products.size() == 0) {
            print("Пока ничего не можете купить, пополните баланс.");
        } else {
            for (int i = 0; i < products.size(); i++) {
                print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
            }
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
