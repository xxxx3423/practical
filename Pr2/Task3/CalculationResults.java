import java.io.*;

class MathOperations implements Serializable {    //Клас MathOperations для виконання простих математичних операцій та підтримки серіалізації/десеріалізації.
    private static final long serialVersionUID = 1L;
    private double result;

    public double add(double a, double b) {     //Виконує додавання двох чисел || param a перше число ||  param b друге число || return сума чисел                                                    
        result = a + b;
        return result;
    }

    public double multiply(double a, double b) {   //Виконує множення двох чисел || param a перше число ||  param b друге число || return добуток чисел
        result = a * b;
        return result;
    }

    public void serialize(String filename) throws IOException {                                   //Зберігає об'єкт у файл || param filename ім'я файлу для збереження || throws IOException якщо виникає помилка запису
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    public static MathOperations deserialize(String filename) throws IOException, ClassNotFoundException {    //Відновлює об'єкт з файлу || param filename ім'я файлу для зчитування || return відновлений об'єкт MathOperations || throws IOException якщо виникає помилка читання || throws ClassNotFoundException якщо клас не знайдено
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {                  
            return (MathOperations) ois.readObject();                                                           
        }
    }
}

public class CalculationResults {     //Клас для тестування MathOperations.
    public static void main(String[] args) {                  //Головний метод для виконання тестів || param args аргументи командного рядка
        MathOperations mathOps = new MathOperations();

        // Тестування операцій
        System.out.println("5 + 3 = " + mathOps.add(5, 3));
        System.out.println("5 * 3 = " + mathOps.multiply(5, 3));

        // Тестування серіалізації
        String filename = "mathOps.ser";
        try {
            mathOps.serialize(filename);
            MathOperations deserializedMathOps = MathOperations.deserialize(filename);
            System.out.println("Deserializing an object: 2 + 4 = " + deserializedMathOps.add(2, 4));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
