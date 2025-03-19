import java.io.*;
import java.util.*;

interface MathOperation extends Serializable {          //Інтерфейс для математичних операцій
    double calculate(double a, double b);
    double calculate(double a); // Перевантажений метод (overloading)
    String getOperationName();
    String formatResult(double a, double b);
}

class Addition implements MathOperation {           //Операція додавання
    private static final long serialVersionUID = 1L;
    
    public double calculate(double a, double b) {
        return a + b;
    }
    
    public double calculate(double a) {
        return a + a; // Перевантажений метод
    }
    
    public String getOperationName() {
        return "Додавання";
    }
    
    public String formatResult(double a, double b) {
        return String.format("| %-10s | %8.2f | %8.2f | %8.2f |", getOperationName(), a, b, calculate(a, b));
    }
}

class Multiplication implements MathOperation {           //Операція множення
    private static final long serialVersionUID = 1L;
    
    public double calculate(double a, double b) {
        return a * b;
    }
    
    public double calculate(double a) {
        return a * a; // Перевантажений метод
    }
    
    public String getOperationName() {
        return "Множення";
    }
    
    public String formatResult(double a, double b) {
        return String.format("| %-10s | %8.2f | %8.2f | %8.2f |", getOperationName(), a, b, calculate(a, b));
    }
}

interface MathOperationFactory {          //Фабрика для створення об'єктів операцій
    MathOperation createOperation();
}

class AdditionFactory implements MathOperationFactory {
    public MathOperation createOperation() {
        return new Addition();
    }
}

class MultiplicationFactory implements MathOperationFactory {
    public MathOperation createOperation() {
        return new Multiplication();
    }
}
                                              
class CalculationResultsManager {                  //Клас для роботи з обчисленнями та збереження результатів
    private List<String> results = new ArrayList<>();
    
    public void addResult(String result) {
        results.add(result);
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
    
    public void serialize(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(results);
        }
    }
    
    public void deserialize(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            @SuppressWarnings("unchecked")
            List<String> loadedResults = (List<String>) ois.readObject();
            results = loadedResults;
        }
    }
}


public class CalculationResults {           //Головний клас для роботи з користувачем
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CalculationResultsManager manager = new CalculationResultsManager();
        
        while (true) {
            System.out.println("Виберіть операцію: 1) Додавання 2) Множення 3) Показати результати 4) Зберегти результати 5) Завантажити результати 6) Вийти");
            int choice = scanner.nextInt();
            
            if (choice == 6) break;
            
            if (choice == 3) {
                manager.displayResults();
                continue;
            }
            
            if (choice == 4) {
                try {
                    manager.serialize("results.ser");
                    System.out.println("Результати збережено.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                continue;
            }
            
            if (choice == 5) {
                try {
                    manager.deserialize("results.ser");
                    System.out.println("Результати завантажено.");
                    manager.displayResults();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                continue;
            }
            
            System.out.println("Введіть два числа:");
            double a = scanner.nextDouble();
            double b = scanner.nextDouble();
            
            MathOperationFactory factory = (choice == 1) ? new AdditionFactory() : new MultiplicationFactory();
            MathOperation operation = factory.createOperation();
            
            String result = operation.formatResult(a, b);
            manager.addResult(result);
            System.out.println("Результат: \n" + result);
        }
        
        scanner.close();
    }
}
