import java.util.Scanner;

public class Console {
    private Account account;
    private Offering offering;
    private Booking booking;
    private Location location;
    private Schedule schedule;
    private TimeSlot timeslot;
    private Instructor instructor;
    private Client client;

    private int accountId;

    public Console() {
        this.account = new Account();
        this.offering = new Offering();
        this.booking = new Booking();
        this.location = new Location();
        this.schedule = new Schedule();
        this.timeslot = new TimeSlot();
        this.instructor = new Instructor();
        this.client = new Client();
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
                        if (role.equals("client")) {
                            clientMenu(scan);
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



    // ====================================================================================================
    // MENU FOR CLIENTS
    // ====================================================================================================
    private void clientMenu(Scanner scan) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\nUser Menu:");
            System.out.println("1. Log Out");
            System.out.println("2. Become a Guardian");
            System.out.println("3. View available lessons");
            System.out.println("4. View lessons registered for");
            System.out.println("5. Register to a lesson");
            System.out.println("6. Cancel a booking");

            int choice = getUserChoice(scan, 1, 6);
            switch (choice) {
                case 1:
                    loggedIn = false;
                    break;
                case 2:
                    createUnderageAccount(scan);
                    break;
                case 3:
                    offering.getAvailableLessonsForClient(accountId);
                    break;
                case 4:
                    client.displayClientLessons(accountId);
                    break;
                case 5:
                    client.registerForLesson(scan, accountId);
                    break;
                case 6:
                    booking.cancelBooking(scan);
                    break;
                default:
                    break;
            }
        }
    }
    // ====================================================================================================



    // ====================================================================================================
    // MENU FOR INSTRUCTORS
    // ====================================================================================================
    private void instructorMenu(Scanner scan) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\nInstructor Menu:");
            System.out.println("1. Log Out");
            System.out.println("2. View available (unassigned) offerings ");
            System.out.println("3. Register to an offering");
            System.out.println("4. View lessons assigned to");

            int choice = getUserChoice(scan, 1, 4);
            switch (choice) {
                case 1:
                    loggedIn = false;
                    break;
                case 2:
                    offering.getInstructorOfferings();
                    break;
                case 3:
                    instructor.assignInstructorToOffering(scan, accountId);
                    break;
                case 4:
                    instructor.displayInstructorLessons(accountId);
                    break;
                default:
                    break;
            }
        }
    }
    // ====================================================================================================


    
    // ====================================================================================================
    // MENU FOR ADMINS
    // ====================================================================================================
    private void adminMenu(Scanner scan) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Log Out");
            System.out.println("2. View all accounts");
            System.out.println("3. View all client accounts");
            System.out.println("4. View all instructor accounts");
            System.out.println("5. Delete an account");
            System.out.println("6. View all offerings");
            System.out.println("7. Add an offering");
            System.out.println("8. View all lessons");
            System.out.println("9. View bookings");
            System.out.println("10. Cancel a booking");
            System.out.println("11. View all locations");
            System.out.println("12. View all schedules");
            System.out.println("13. View all timeslots");
    
            int choice = getUserChoice(scan, 1, 13);
            switch (choice) {
                case 1:
                    loggedIn = false;
                    break;
                case 2:
                    account.getAccounts();
                    break;
                case 3:
                    account.getClients();
                    break;
                case 4:
                    account.getInstructors();
                    break;
                case 5:
                    System.out.print("Account ID for deletion: ");
                    account.deleteAccount(Integer.parseInt(scan.nextLine()));
                    break;
                case 6:
                    offering.getOfferings();
                    break;
                case 7:
                    offering.addOffering(scan);
                    break;
                case 8:
                    offering.getLessons();
                    break;
                case 9:
                    booking.getBookings();
                    break;
                case 10:
                booking.cancelBooking(scan);
                    break;
                case 11:
                    location.getAllLocations();
                    break;
                case 12:
                    schedule.getAllSchedules();
                    break;
                case 13:
                    timeslot.getAllTimeslots();
                    break;
                default:
                    break;
                }
            }
        }
    // ====================================================================================================



    // ====================================================================================================
    // METHOD FOR USER CHOSING A NUMBER IN MENU
    // ====================================================================================================
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
    // ====================================================================================================


    // METHOD TO HANDLE USER LOGIN
    private String login(Scanner scan) {
        System.out.print("Enter your email: ");
        String email = scan.nextLine();

        System.out.print("Enter your password: ");
        String password = scan.nextLine();

        //check credentials and get role
        String role = account.authenticateUser(email, password);
        accountId = account.getAccountIdByEmail(email);

        if (role != null) {
            System.out.println("\nLogin successful!");
            return role;
        } else {
            System.out.println("\nInvalid email or password. Please try again.");
            return null;
        }
    }


    // METHOD TO HANDLE ACCOUNT CREATION
    public boolean createAccount(Scanner scan) {
        // Take inputs from the user
        System.out.print("Enter your name: ");
        String name = scan.nextLine();
    
        System.out.print("Enter your age: ");
        int age = scan.nextInt();
        scan.nextLine();
    
        System.out.print("Enter your email: ");
        String email = scan.nextLine();
    
        System.out.print("Enter your phone number: ");
        String phoneNumber = scan.nextLine();
    
        System.out.print("Enter your password: ");
        String password = scan.nextLine();
    
        System.out.print("Enter your role (client, instructor): ");
        String role = scan.nextLine();
    
        // Check if the age is valid
        if (age < 18) {
            System.out.println("\nError: Age must be 18 or older to create an account.");
            return false;
        }
    
        // Insert into the accounts table
        int accountId = account.insertIntoAccounts(name, age, email, phoneNumber, password);
        if (accountId == -1) {
            System.out.println("Error: Failed to create account in the database.");
            return false;
        }
    
        // Insert into the respective table (client or instructor)
        if (role.equalsIgnoreCase("user") || role.equalsIgnoreCase("client")) {
            return account.insertClient(accountId, 0);
        } else if (role.equalsIgnoreCase("instructor")) {
            System.out.print("Enter your availability (ex: Montreal, Toronto, Vancouver): ");
            String availability = scan.nextLine();

            System.out.print("Enter your speciality (ex: Yoga, Swimming, Tennis): ");
            String speciality = scan.nextLine();
            return account.insertInstructor(accountId, availability, speciality);
        } else {
            System.out.println("Error: Invalid role entered.");
            return false;
        }
    }



        // METHOD TO HANDLE GUARDIANED ACCOUNT CREATION
        public boolean createUnderageAccount(Scanner scan) {
            // Take inputs from the user
            System.out.print("Enter your email: ");
            int guardianID = account.getAccountIdByEmail(scan.nextLine());
            
            System.out.print("Enter ward's name: ");
            String name = scan.nextLine();
        
            System.out.print("Enter ward's age: ");
            int age = scan.nextInt();
            scan.nextLine();
        
            System.out.print("Enter ward's email: ");
            String email = scan.nextLine();
        
            System.out.print("Enter ward's phone number: ");
            String phoneNumber = scan.nextLine();
        
            System.out.print("Enter ward's password: ");
            String password = scan.nextLine();
        
            // Insert into the accounts table
            int accountId = account.insertIntoAccounts(name, age, email, phoneNumber, password);
            if (accountId == -1) {
                System.out.println("Error: Failed to create account in the database.");
                return false;
            }
        
            return account.insertClient(accountId, guardianID);
        }






}
