import java.io.*;
import java.util.*;
import java.util.Scanner;

interface ComputationResultDisplay {          //Інтерфейс для відображення результатів
    void display(List<Double> results);
}

class TextResultDisplay implements ComputationResultDisplay {  //Відображення у вигляді простого тексту
    public void display(List<Double> results) {
        for (double result : results) {
            System.out.println("Computation Result: " + result);
        }
    }
}

class TableResultDisplay implements ComputationResultDisplay { //Відображення у вигляді таблиці
    public void display(List<Double> results) {
        System.out.println("+----------------+");
        System.out.println("|   Результати   |");
        System.out.println("+----------------+");
        for (double result : results) {
            System.out.printf("| %14.2f |\n", result);
        }
        System.out.println("+----------------+");
    }
}
 
abstract class ComputationData implements Serializable {  //Абстрактний клас для обчислень
    private static final long serialVersionUID = 1L;
    protected double param1;
    protected double param2;
    protected double result;

    public ComputationData(double param1, double param2) {
        this.param1 = param1;
        this.param2 = param2;
    }

    public abstract void compute();
    public double getResult() { return result; }

    // Перевантажений метод
    public void compute(double additionalValue) {
        this.result += additionalValue;
    }
}

//Додавання
class AdditionComputation extends ComputationData {
    public AdditionComputation(double param1, double param2) { super(param1, param2); }
    public void compute() { result = param1 + param2; }
}

//Віднімання
class SubtractionComputation extends ComputationData {
    public SubtractionComputation(double param1, double param2) { super(param1, param2); }
    public void compute() { result = param1 - param2; }
}

//Множення
class MultiplicationComputation extends ComputationData {
    public MultiplicationComputation(double param1, double param2) { super(param1, param2); }
    public void compute() { result = param1 * param2; }
}

//Ділення
class DivisionComputation extends ComputationData {
    public DivisionComputation(double param1, double param2) { super(param1, param2); }
    public void compute() { result = (param2 != 0) ? param1 / param2 : Double.NaN; }
}

//Фабрика обчислень
interface ComputationFactory {
    ComputationData createComputation(double param1, double param2);
}

class AdditionComputationFactory implements ComputationFactory {
    public ComputationData createComputation(double param1, double param2) { return new AdditionComputation(param1, param2); }
}

class SubtractionComputationFactory implements ComputationFactory {
    public ComputationData createComputation(double param1, double param2) { return new SubtractionComputation(param1, param2); }
}

class MultiplicationComputationFactory implements ComputationFactory {
    public ComputationData createComputation(double param1, double param2) { return new MultiplicationComputation(param1, param2); }
}

class DivisionComputationFactory implements ComputationFactory {
    public ComputationData createComputation(double param1, double param2) { return new DivisionComputation(param1, param2); }
}

//Клас обробки обчислень
class ComputationProcessor {
    private ComputationFactory factory;
    private List<Double> results = new ArrayList<>();

    public ComputationProcessor(ComputationFactory factory) { this.factory = factory; }

    public double performComputation(double param1, double param2) {
        ComputationData computation = factory.createComputation(param1, param2);
        computation.compute();
        double result = computation.getResult();
        results.add(result);
        return result;
    }

    public List<Double> getResults() { return results; }
}

//Клас для тестування функціональності
class ComputationTest {
    public static void runTests() {
        ComputationProcessor processor = new ComputationProcessor(new AdditionComputationFactory());
        assert processor.performComputation(2, 3) == 5.0 : "Test failed for addition!";
        
        processor = new ComputationProcessor(new SubtractionComputationFactory());
        assert processor.performComputation(5, 3) == 2.0 : "Test failed for subtraction!";
        
        processor = new ComputationProcessor(new MultiplicationComputationFactory());
        assert processor.performComputation(4, 3) == 12.0 : "Test failed for multiplication!";
        
        processor = new ComputationProcessor(new DivisionComputationFactory());
        assert processor.performComputation(10, 2) == 5.0 : "Test failed for division!";
        
        System.out.println("Всі тести зроблені!");
    }
}

//Основний клас для взаємодії з користувачем
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ComputationTest.runTests(); // Запуск тестів
        
        System.out.println("Оберіть операцію: +, -, *, /");
        String operation = scanner.next();
        System.out.println("Введіть два числа:");
        double param1 = scanner.nextDouble();
        double param2 = scanner.nextDouble();
        
        ComputationFactory factory;
        switch (operation) {
            case "+": factory = new AdditionComputationFactory(); break;
            case "-": factory = new SubtractionComputationFactory(); break;
            case "*": factory = new MultiplicationComputationFactory(); break;
            case "/": factory = new DivisionComputationFactory(); break;
            default: System.out.println("Невідома операція"); return;
        }
        
        ComputationProcessor processor = new ComputationProcessor(factory);
        double result = processor.performComputation(param1, param2);
        
        System.out.println("Оберіть формат виводу (text/table):");
        String format = scanner.next();
        ComputationResultDisplay display = format.equals("table") ? new TableResultDisplay() : new TextResultDisplay();
        
        display.display(processor.getResults());
    }
}