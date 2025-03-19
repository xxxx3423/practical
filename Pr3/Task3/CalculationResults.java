import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Інтерфейс для математичних операцій
interface MathOperation extends Serializable {
    double calculate(double a, double b);
    String getOperationName();
}

// Класи операцій
class Addition implements MathOperation {
    private static final long serialVersionUID = 1L;
    
    
    public double calculate(double a, double b) {
        return a + b;
    }
    
    
    public String getOperationName() {
        return "Addition";
    }
}

class Multiplication implements MathOperation {
    private static final long serialVersionUID = 1L;
    
    
    public double calculate(double a, double b) {
        return a * b;
    }
    
    
    public String getOperationName() {
        return "Multiplication";
    }
}

// Фабрика для створення об'єктів операцій
interface MathOperationFactory {
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

// Клас для роботи з обчисленнями та збереження результатів
class CalculationResultsManager {
    private List<String> results = new ArrayList<>();
    
    public void addResult(String result) {
        results.add(result);
    }
    
    public void displayResults() {
        for (String result : results) {
            System.out.println(result);
        }
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

public class CalculationResults {
    public static void main(String[] args) {
        MathOperationFactory addFactory = new AdditionFactory();
        MathOperationFactory mulFactory = new MultiplicationFactory();
        
        MathOperation addition = addFactory.createOperation();
        MathOperation multiplication = mulFactory.createOperation();
        
        CalculationResultsManager manager = new CalculationResultsManager();
        
        double a = 5, b = 3;
        manager.addResult(addition.getOperationName() + ": " + a + " + " + b + " = " + addition.calculate(a, b));
        manager.addResult(multiplication.getOperationName() + ": " + a + " * " + b + " = " + multiplication.calculate(a, b));
        
        manager.displayResults();
        
        // Збереження результатів
        String filename = "results.ser";
        try {
            manager.serialize(filename);
            System.out.println("Results saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Завантаження результатів
        try {
            manager.deserialize(filename);
            System.out.println("Loaded results from file:");
            manager.displayResults();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}