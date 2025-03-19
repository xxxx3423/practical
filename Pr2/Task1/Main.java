import java.io.Serializable;
// Клас для зберігання даних обчислення та їх серіалізації
class ComputationData implements Serializable {     
    private static final long serialVersionUID = 1L; // Унікальний ідентифікатор для серіалізації
    private double param1; // Перший параметр обчислення
    private double param2; // Другий параметр обчислення
    private double result; // Збережений результат обчислення

    // Конструктор для ініціалізації параметрів
    public ComputationData(double param1, double param2) {
        this.param1 = param1;
        this.param2 = param2;
    }

    // Метод для виконання обчислення (сума двох параметрів)
    public void compute() {
        this.result = param1 + param2;
    }

    // Метод для отримання результату обчислення
    public double getResult() {
        return result;
    }
}
// Клас для обробки обчислень, використовує ComputationData
class ComputationProcessor {
    private ComputationData data;    // Об'єкт, що містить дані для обчислення

// Конструктор приймає два параметри та створює об'єкт ComputationData
    public ComputationProcessor(double param1, double param2) {
        this.data = new ComputationData(param1, param2);
    }
    // Виконує обчислення, використовуючи метод compute() з ComputationData
    public void performComputation() {
        data.compute();
    }
// Повертає результат виконаного обчислення
    public double getComputationResult() {
        return data.getResult();
    }
}
// Головний клас програми
public class Main {
    public static void main(String[] args) {
         // Створення об'єкта ComputationProcessor з двома вхідними параметрами
        ComputationProcessor processor = new ComputationProcessor(10.5, 20.3);
        // Виконання обчислення
        processor.performComputation();
        // Виведення результату в консоль
        System.out.println("Result: " + processor.getComputationResult());
    }
}
