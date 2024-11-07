import java.util.Scanner;

public class Console {
    private Account account;

    public Console() {
        this.account = new Account();
    }

    public void startApplication() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("Welcome to LessonHub!");
        Scanner scan = new Scanner(System.in);
        boolean applicationLoop = true;

        // ------------------------------ 1. MENU ------------------------------
        while (applicationLoop) {
            System.out.println("\n1. Log In");
            System.out.println("2. Create an account");
            System.out.println("3. Quit the application");
            int menuChoice = getUserChoice(scan, 1, 3);

            // --------------- 1.1 LOGIN ---------------
            if (menuChoice == 1) {
                boolean loginSuccessful = login(scan);
                if (loginSuccessful) {
                    // ------------------------------- 2. APPLICATION ---------------------------------
                    boolean userLoggedIn = true;
                    while (userLoggedIn) {
                        System.out.println("\nChoose what operation you would like to do:");
                        System.out.println("1. Log Out");
                        System.out.println("2. See all accounts");

                        int choice = getUserChoice(scan, 1, 2);

                        switch (choice) {
                            case 1:
                                userLoggedIn = false; // exit application layer
                                break;

                            case 2:
                                account.getAccounts();
                                break;

                            default:
                                break;
                        }
                    }
                } else {
                    // if login fails, go back to the main menu
                    System.out.println("Returning to main menu.");
                }
            } 
            // --------------- 1.2 CREATE ACCOUNT ---------------
            else if (menuChoice == 2) {
                boolean accountCreated = createAccount(scan);
                if (accountCreated) {
                    System.out.println("Account created successfully!");
                } else {
                    System.out.println("Error creating account. Please try again.");
                }
            }
            // --------------- 1.3 QUIT APPLICATION ---------------
            else {
                break;
            }
        }

        System.out.println("Thank you for using LessonHub!");
    }



    // METHOD FOR USER CHOSING A NUMBER IN MENU
    private int getUserChoice(Scanner scan, int min, int max) {
        int choice = 0;
        while (choice < min || choice > max) {
            try {
                System.out.print("\nEnter your choice: ");
                choice = Integer.parseInt(scan.nextLine());
                if (choice < min || choice > max) {
                    System.out.println("Please enter a valid choice between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        System.out.print("\033[H\033[2J");
        System.out.flush();
        return choice;
    }


    // METHOD TO HANDLE USER LOGIN
    private boolean login(Scanner scan) {
        System.out.print("Enter your email: ");
        String email = scan.nextLine();
        
        System.out.print("Enter your password: ");
        String password = scan.nextLine();

        // check if the credentials are valid by querying the database
        boolean isAuthenticated = account.authenticateUser(email, password);

        if (isAuthenticated) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("\nInvalid email or password. Please try again.");
            return false;
        }
    }


    // METHOD TO HANDLE ACCOUNT CREATION
    private boolean createAccount(Scanner scan) {
        System.out.print("Enter your name: ");
        String name = scan.nextLine();
        
        System.out.print("Enter your email: ");
        String email = scan.nextLine();
        
        System.out.print("Enter your phone number: ");
        String phoneNumber = scan.nextLine();
        
        System.out.print("Enter your password: ");
        String password = scan.nextLine();

        boolean isCreated = account.createAccount(name, email, phoneNumber, password);

        return isCreated;
    }
}
