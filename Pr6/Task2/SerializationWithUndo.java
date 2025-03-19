import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

// Інтерфейс для об'єктів, які можуть бути відображені
interface Displayable {
    void display();
}

// Інтерфейс для команд, які можна виконувати та відміняти
interface Command {
    void execute();
    void undo();
}

// Команда додавання особи до списку
class AddPersonCommand implements Command {
    private List<Person> personsList;
    private Person person;

    public AddPersonCommand(List<Person> personsList, Person person) {
        this.personsList = personsList;
        this.person = person;
    }

    // Виконання команди - додавання особи до списку
    public void execute() {
        personsList.add(person);
        CommandHistory.getInstance().addCommand(this);
    }

    // Відміна команди - видалення особи зі списку
    public void undo() {
        personsList.remove(person);
    }
}

// Історія виконаних команд для можливості скасування
class CommandHistory {
    private static CommandHistory instance;
    private Stack<Command> history = new Stack<>();

    private CommandHistory() {}

    public static CommandHistory getInstance() {
        if (instance == null) {
            instance = new CommandHistory();
        }
        return instance;
    }

    public void addCommand(Command command) {
        history.push(command);
    }

    // Відміна останньої команди
    public void undoLastCommand() {
        if (!history.isEmpty()) {
            history.pop().undo();
        }
    }
}

// Клас, що представляє особу, реалізує Serializable та Displayable
class Person implements Serializable, Displayable {
    private static final long serialVersionUID = 1L;
    protected String name;
    protected int age;
    protected transient String password; // Поле transient не серіалізується

    public Person(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    // Відображення інформації про особу
    public void display() {
        System.out.printf("| %-15s | %-3d |\n", name, age);
    }
}

// Потік працівник, що обробляє завдання з черги
class WorkerThread extends Thread {
    private BlockingQueue<Runnable> taskQueue;
    private volatile boolean isStopped = false;

    public WorkerThread(BlockingQueue<Runnable> taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void run() {
        while (!isStopped) {
            try {
                Runnable task = taskQueue.take();
                task.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Зупинка потоку
    public void stopWorker() {
        isStopped = true;
        this.interrupt();
    }
}

// Основний клас програми
public class SerializationWithUndo {
    private static final String FILE_NAME = "persons.ser";
    private static List<Person> personsList = new ArrayList<>();
    private static BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private static WorkerThread worker = new WorkerThread(taskQueue);
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        worker.start(); // Запуск потоку працівника
        
        while (true) {
            System.out.println("1. Додати особу\n2. Скасувати\n3. Зберегти\n4. Завантажити\n5. Дисплей\n6. Аналіз віку\n7. Вихід");
            System.out.print("Виберіть варіант: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    addPerson();
                    break;
                case 2:
                    CommandHistory.getInstance().undoLastCommand();
                    break;
                case 3:
                    saveToFile();
                    break;
                case 4:
                    loadFromFile();
                    break;
                case 5:
                    displayPersons();
                    break;
                case 6:
                    analyzeAge();
                    break;
                case 7:
                    shutdownWorker();
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    // Додавання особи
    private static void addPerson() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Person person = new Person(name, age, password);
        Command command = new AddPersonCommand(personsList, person);
        command.execute();
    }

    // Збереження списку осіб у файл
    private static void saveToFile() {
        taskQueue.add(() -> {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                oos.writeObject(personsList);
                System.out.println("Дані збережені.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Завантаження списку осіб із файлу
    private static void loadFromFile() {
        taskQueue.add(() -> {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                personsList = (List<Person>) ois.readObject();
                System.out.println("Дані завантажені.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    // Відображення списку осіб
    private static void displayPersons() {
        taskQueue.add(() -> {
            System.out.println("--------------------------------------");
            personsList.forEach(Person::display);
            System.out.println("--------------------------------------");
        });
    }

    // Аналіз вікових характеристик осіб
    private static void analyzeAge() {
        taskQueue.add(() -> {
            OptionalInt minAge = personsList.stream().mapToInt(Person::getAge).min();
            OptionalInt maxAge = personsList.stream().mapToInt(Person::getAge).max();
            OptionalDouble avgAge = personsList.stream().mapToInt(Person::getAge).average();

            System.out.println("Статистика віку:");
            System.out.println("Мінімальний вік: " + (minAge.isPresent() ? minAge.getAsInt() : "Немає даних"));
            System.out.println("Максимальний вік: " + (maxAge.isPresent() ? maxAge.getAsInt() : "Немає даних"));
            System.out.println("Середній вік: " + (avgAge.isPresent() ? avgAge.getAsDouble() : "Немає даних"));
        });
    }

    // Завершення роботи потоку
    private static void shutdownWorker() {
        worker.stopWorker();
        System.out.println("Worker thread зупинено.");
    }
}