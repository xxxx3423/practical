import java.io.*;
import java.util.*;

// Інтерфейс для математичних операцій, що підтримує серіалізацію
interface MathOperation extends Serializable {
    double calculate(double a, double b); // Метод для виконання операції з двома аргументами
    double calculate(double a); // Метод для виконання операції з одним аргументом
    String getOperationName(); // Метод для отримання назви операції
    String formatResult(double a, double b); // Форматований вивід результату операції
}

// Клас реалізації додавання
class Addition implements MathOperation {
    private static final long serialVersionUID = 1L;
    
    public double calculate(double a, double b) {
        return a + b;
    }
    
    public double calculate(double a) {
        return a + a;
    }
    
    public String getOperationName() {
        return "Додавання";
    }
    
    public String formatResult(double a, double b) {
        return String.format("| %-10s | %8.2f | %8.2f | %8.2f |", getOperationName(), a, b, calculate(a, b));
    }
}

// Клас реалізації множення
class Multiplication implements MathOperation {
    private static final long serialVersionUID = 1L;
    
    public double calculate(double a, double b) {
        return a * b;
    }
    
    public double calculate(double a) {
        return a * a;
    }
    
    public String getOperationName() {
        return "Множення";
    }
    
    public String formatResult(double a, double b) {
        return String.format("| %-10s | %8.2f | %8.2f | %8.2f |", getOperationName(), a, b, calculate(a, b));
    }
}

// Фабричний інтерфейс для створення операцій
interface MathOperationFactory {
    MathOperation createOperation();
}

// Фабрика для створення об'єктів додавання
class AdditionFactory implements MathOperationFactory {
    public MathOperation createOperation() {
        return new Addition();
    }
}

// Фабрика для створення об'єктів множення
class MultiplicationFactory implements MathOperationFactory {
    public MathOperation createOperation() {
        return new Multiplication();
    }
}

// Синглтон для управління результатами обчислень
class CalculationResultsManager {
    private static CalculationResultsManager instance;
    private List<String> results = new ArrayList<>();
    private Stack<String> undoStack = new Stack<>();
    
    private CalculationResultsManager() {}
    
    public static CalculationResultsManager getInstance() {
        if (instance == null) {
            instance = new CalculationResultsManager();
        }
        return instance;
    }
    
    public void addResult(String result) {
        results.add(result);
        undoStack.push(result);
    }
    
    public void undoLastOperation() {
        if (!undoStack.isEmpty()) {
            results.remove(undoStack.pop());
            System.out.println("Останню операцію скасовано.");
        } else {
            System.out.println("Немає операцій для скасування.");
        }
    }
    
    public void displayResults() {
        if (results.isEmpty()) {
            System.out.println("Немає результатів.");
            return;
        }
        System.out.println("+------------+----------+----------+----------+");
        System.out.println("| Операція   |    A     |    B     |  Результат  |");
        System.out.println("+------------+----------+----------+----------+");
        for (String result : results) {
            System.out.println(result);
        }
        System.out.println("+------------+----------+----------+----------+");
    }
}

// Клас для реалізації макрокоманд
class MacroCommand {
    private List<Runnable> commands = new ArrayList<>();
    
    public void addCommand(Runnable command) {
        commands.add(command);
    }
    
    public void execute() {
        for (Runnable command : commands) {
            command.run();
        }
    }
}

// Основний клас програми
public class CalculationResults {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CalculationResultsManager manager = CalculationResultsManager.getInstance();
        
        while (true) {
            System.out.println("Виберіть операцію: 1) Додавання 2) Множення 3) Показати результати 4) Скасувати 5) Вийти");
            int choice = scanner.nextInt();
            
            if (choice == 5) break;
            
            if (choice == 3) {
                manager.displayResults();
                continue;
            }
            
            if (choice == 4) {
                manager.undoLastOperation();
                continue;
            }
            
            System.out.println("Введіть два числа:");
            double a = scanner.nextDouble();
            double b = scanner.nextDouble();
            
            MathOperationFactory factory = (choice == 1) ? new AdditionFactory() : new MultiplicationFactory();
            MathOperation operation = factory.createOperation();
            
            String result = operation.formatResult(a, b);
            manager.addResult(result);
            
            MacroCommand macro = new MacroCommand();
            macro.addCommand(() -> System.out.println("Результат: \n" + result));
            macro.addCommand(() -> System.out.println("Операція додана в історію."));
            macro.execute();
        }
        
        scanner.close();
    }
}