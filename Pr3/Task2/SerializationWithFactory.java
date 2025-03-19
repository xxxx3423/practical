import java.io.*;
import java.util.*;

// Інтерфейс для відображення результатів
interface Displayable {
    void display();
}

// Базовий клас, що реалізує Displayable
class Person implements Serializable, Displayable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
    private transient String password;

    public Person(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }

    public void display() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Password: " + (password != null ? password : "[Not saved]"));
    }
}

// Фабричний інтерфейс
interface PersonFactory {
    Person create(String name, int age, String password);
}

// Реалізація фабрики
class ConcretePersonFactory implements PersonFactory {
    public Person create(String name, int age, String password) {
        return new Person(name, age, password);
    }
}

public class SerializationWithFactory {
    private static final String FILE_NAME = "persons.ser";
    private static List<Person> personsList = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PersonFactory factory = new ConcretePersonFactory();

        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Person person = factory.create(name, age, password);
        personsList.add(person);
        saveToFile();
        System.out.println("Object is saved!\n");

        loadFromFile();
        System.out.println("Restored objects:");
        personsList.forEach(Person::display);
    }

    private static void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(personsList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            personsList = (List<Person>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
