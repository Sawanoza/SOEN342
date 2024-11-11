import java.util.Scanner;

public class Console {
    private Account account;

    public Console() {
        this.account = new Account();
    }

    public void startApplication() {
        // System.out.print("\033[H\033[2J");
        // System.out.flush();
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
                String role = login(scan);
                if (role != null) {
                    // ------------------------------- 2. APPLICATION ---------------------------------
                    boolean userLoggedIn = true;
                    while (userLoggedIn) {
                        if (role.equals("user")) {
                            userMenu(scan);
                        } else if (role.equals("instructor")) {
                            instructorMenu(scan);
                        } else if (role.equals("admin")) {
                            adminMenu(scan);
                        }
                        userLoggedIn = false; // log out
                    }
                } else {
                    // if login fails, go back to the main menu
                    System.out.println("Returning to main menu...");
                }
            }
            // --------------- 1.2 CREATE ACCOUNT ---------------
            else if (menuChoice == 2) {
                boolean accountCreated = createAccount(scan);
                if (accountCreated) {
                    System.out.println("\nAccount created successfully!");
                } else {
                    System.out.println("\nError creating account. Please try again.");
                    System.out.println("Returning to main menu...");
                }
            }
            // --------------- 1.3 QUIT APPLICATION ---------------
            else {
                break;
            }
        }

        System.out.println("Thank you for using LessonHub!");
    }



        // MENU FOR REGULAR USERS
        private void userMenu(Scanner scan) {
            boolean loggedIn = true;
            while (loggedIn) {
                System.out.println("\nUser Menu:");
                System.out.println("1. Log Out");
                System.out.println("2. ");
    
                int choice = getUserChoice(scan, 1, 2);
                switch (choice) {
                    case 1:
                        loggedIn = false;
                        break;
                    case 2:
                        /* */
                        break;
                    default:
                        break;
                }
            }
        }
    
        // MENU FOR INSTRUCTORS
        private void instructorMenu(Scanner scan) {
            boolean loggedIn = true;
            while (loggedIn) {
                System.out.println("\nInstructor Menu:");
                System.out.println("1. Log Out");
                System.out.println("2. ");
    
                int choice = getUserChoice(scan, 1, 3);
                switch (choice) {
                    case 1:
                        loggedIn = false;
                        break;
                    case 2:
                        /* */
                        break;
                    default:
                        break;
                }
            }
        }
    
        // MENU FOR ADMINS
        private void adminMenu(Scanner scan) {
            boolean loggedIn = true;
            while (loggedIn) {
                System.out.println("\nAdmin Menu:");
                System.out.println("1. Log Out");
                System.out.println("2. View all accounts");
                System.out.println("3. Delete an account");
                System.out.println("3. View all offerings");
                System.out.println("4. View all lessons");
                System.out.println("5. View bookings");
    
                int choice = getUserChoice(scan, 1, 5);
                switch (choice) {
                    case 1:
                        loggedIn = false;
                        break;
                    case 2:
                        account.getAccounts();
                        break;
                    case 3:
                        System.out.print("Account ID for deletion: ");
                        account.deleteAccount(Integer.parseInt(scan.nextLine()));
                        break;
                    case 4:
                        //
                        break;
                    case 5:
                        // 
                        break;
                    default:
                        break;
                }
            }
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

        // System.out.print("\033[H\033[2J");
        // System.out.flush();
        return choice;
    }


    // METHOD TO HANDLE USER LOGIN
    private String login(Scanner scan) {
        System.out.print("Enter your email: ");
        String email = scan.nextLine();

        System.out.print("Enter your password: ");
        String password = scan.nextLine();

        //check credentials and get role
        String role = account.authenticateUser(email, password);

        if (role != null) {
            System.out.println("\nLogin successful! Role: " + role);
            return role;
        } else {
            System.out.println("\nInvalid email or password. Please try again.");
            return null;
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

        System.out.print("Enter your role (user, instructor): ");
        String role = scan.nextLine();

        return account.createAccount(name, email, phoneNumber, password, role);
    }




}
