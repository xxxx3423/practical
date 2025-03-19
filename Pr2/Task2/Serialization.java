import java.io.*;
import java.util.Scanner;

// Клас Person, який реалізує інтерфейс Serializable для підтримки серіалізації
class Person implements Serializable {
    private static final long serialVersionUID = 1L; // Унікальний ідентифікатор для серіалізації
    private String name; // Ім'я користувача
    private int age; // Вік користувача
    private transient String password; // Поле, яке не буде серіалізоване

    // Конструктор для ініціалізації об'єкта
    public Person(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }

    // Метод для виводу інформації про користувача
    public void display() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        // Пароль не буде відновлений після десеріалізації через transient
        System.out.println("Password: " + (password != null ? password : "[Dont is saved]"));
    }
}

// Основний клас для демонстрації серіалізації та десеріалізації
public class Serialization {
    private static final String FILE_NAME = "person.ser"; // Ім'я файлу для збереження об'єкта

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Зчитуємо дані користувача
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Очищення буфера після введення числа
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Створюємо об'єкт класу Person
        Person person = new Person(name, age, password);

        // Зберігаємо об'єкт у файл
        saveToFile(person);
        System.out.println("Object is saved!\n");

        // Відновлюємо об'єкт із файлу
        Person restoredPerson = loadFromFile();
        if (restoredPerson != null) {
            System.out.println("Restored object:");
            restoredPerson.display();
        }
    }

    // Метод для серіалізації об'єкта та запису у файл
    private static void saveToFile(Person person) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(person); // Запис об'єкта у файл
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для десеріалізації об'єкта з файлу
    private static Person loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (Person) ois.readObject(); // Зчитування об'єкта з файлу
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null; // У разі помилки повертаємо null
        }
    }
}
