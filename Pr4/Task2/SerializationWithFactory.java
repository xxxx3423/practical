import java.io.*;
import java.util.*;


interface Displayable {  //Інтерфейс для відображення результатів
    void display();
}


class Person implements Serializable, Displayable {     //Базовий клас, що реалізує Displayable
    private static final long serialVersionUID = 1L;
    protected String name;
    protected int age;
    protected transient String password;

    public Person(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }

    public void display() {
        System.out.printf("| %-15s | %-3d |\n", name, age);
    }
}

                                    
class Employee extends Person {    //Підклас Person з додатковою інформацією
    private String position;

    public Employee(String name, int age, String password, String position) {
        super(name, age, password);
        this.position = position;
    }

    public void display() {
        System.out.printf("| %-15s | %-3d | %-10s |\n", name, age, position);
    }
}


interface PersonFactory {                                       //Фабричний інтерфейс
    Person create(String name, int age, String password);
}


class ConcretePersonFactory implements PersonFactory {          //Реалізація фабрики
   
    public Person create(String name, int age, String password) {
        return new Person(name, age, password);
    }
    
    public Employee create(String name, int age, String password, String position) {
        return new Employee(name, age, password, position);
    }
}
                                                         
public class SerializationWithFactory {                          //Головний клас
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
        
        System.out.print("Is this person an employee? (yes/no): ");
        String response = scanner.nextLine();
        
        Person person;
        if (response.equalsIgnoreCase("yes")) {
            System.out.print("Enter position: ");
            String position = scanner.nextLine();
            person = ((ConcretePersonFactory) factory).create(name, age, password, position);
        } else {
            person = factory.create(name, age, password);
        }
        
        personsList.add(person);
        saveToFile();
        System.out.println("Object is saved!\n");
        
        loadFromFile();
        System.out.println("Restored objects:");
        System.out.println("--------------------------------------");
        personsList.forEach(Person::display);
        System.out.println("--------------------------------------");
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