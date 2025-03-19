import java.io.*;
import java.util.*;

interface ComputationResultDisplay {               // Інтерфейс для відображення результатів
    void display(double result);
}

class TextResultDisplay implements ComputationResultDisplay { // Реалізація інтерфейсу для виведення в текстовому вигляді
    public void display(double result) {
        System.out.println("Computation Result: " + result);
    }
}

abstract class ComputationData implements Serializable {    // Абстрактний клас для обчислень
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
}       

class AdditionComputation extends ComputationData {                 // Конкретний клас обчислень (додавання)
    public AdditionComputation(double param1, double param2) {
        super(param1, param2);
    }

    
    public void compute() {
        result = param1 + param2;
    }
}
                                                          
interface ComputationFactory {                                 // Фабрика для створення обчислювальних об'єктів
    ComputationData createComputation(double param1, double param2);
}

                                                            
class AdditionComputationFactory implements ComputationFactory {        // Фабрика для створення додавання
    public ComputationData createComputation(double param1, double param2) {
        return new AdditionComputation(param1, param2);
    }
}

class ComputationProcessor {                                                // Клас обробки обчислень
    private ComputationFactory factory;
    private List<Double> results = new ArrayList<>();

    public ComputationProcessor(ComputationFactory factory) {
        this.factory = factory;
    }

    public double performComputation(double param1, double param2) {
        ComputationData computation = factory.createComputation(param1, param2);
        computation.compute();
        double result = computation.getResult();
        results.add(result);
        return result;
    }

    public List<Double> getResults() {
        return results;
    }
}

public class Main {
    public static void main(String[] args) {
        ComputationFactory factory = new AdditionComputationFactory();
        ComputationProcessor processor = new ComputationProcessor(factory);
        ComputationResultDisplay display = new TextResultDisplay();

        double result1 = processor.performComputation(10.5, 20.3);
        display.display(result1);

        double result2 = processor.performComputation(5.7, 3.3);
        display.display(result2);
    }
}
