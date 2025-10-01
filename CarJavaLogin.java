import java.io.Console;

public class CarJavaLogin {

    public static void main(String[] args) {
        // Correct username and password for validation
        String correctUsername = "Arnold";
        String correctPassword = "GracieAbrams";

        // Get system console
        Console console = System.console();
        if (console == null) {
            System.out.println("Console is not available. Please run this program in a terminal.");
            return; // Exit if console is missing
        }

        int attempts = 0;          // Counter for login attempts
        boolean loggedIn = false;  // If login is successful

        // Allow maximum 3 login attempts
        while (attempts < 3) {
            // Asks for username
            System.out.print("Enter your Username: ");
            String username = console.readLine();

            // Asks for password (input is hidden)
            char[] passwordArray = console.readPassword("Now Enter your Password Mate: ");
            String password = new String(passwordArray);

            // Check credentials
            if (username.equals(correctUsername) && password.equals(correctPassword)) {
                System.out.println("Login Successful Mate. Welcome " + username + ", oh you so fyyn!!");
                loggedIn = true;
                break; // Exit loop after success
            } else {
                System.out.println("Invalid Username or Password. Lets do this again!!");
                attempts++; // Count failed attempt
            }
        }

        // If user fails 3 times
        if (!loggedIn) {
            System.out.println("Sorry Mate, times up. Leave and don't come back!!");
        }
    }
}