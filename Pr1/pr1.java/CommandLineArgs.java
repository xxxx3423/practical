// Оголошення класу для роботи з аргументами командного рядка
public class CommandLineArgs {
    
    // Головний метод програми, який приймає аргументи командного рядка
    public static void main(String[] args) {
        
        // Вивід повідомлення про початок обробки аргументів
        System.out.println("Argumentu komandnogo raydka:");
        
        // Цикл для перебору всіх аргументів командного рядка
        for (int i = 0; i < args.length; i++) {
            
            // Вивід кожного аргументу з його номером
            System.out.println("Argument " + (i + 1) + ": " + args[i]);
        }
    }
}
