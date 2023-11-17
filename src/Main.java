import service.helpers.ConnectionHelper;
import service.helpers.PrepareTable;
import service.helpers.ViewTable;
import service.helpers.Views;

import java.sql.Connection;
import java.util.Scanner;


public class Main {

    // Connection helper to manage database connections
    static ConnectionHelper connectionHelper = new ConnectionHelper();
    static Connection connection = null;

    // Variable to track the type of staff (Admin or Security)
    static int staff = 0; // 1 - Admin , 2 - Security

    public static void main(String[] args){
        try{
            try{
                // Establishing a connection to the database
                connection = connectionHelper.getConnection();
                Scanner scanner = new Scanner(System.in);

                // Displaying the welcome message and user options
                System.out.println("********************************************");
                System.out.println("*   WELCOME TO THE WOLF PARKING MANAGEMENT   *");
                System.out.println("*                   SYSTEM                   *");
                System.out.println("********************************************");

                while(true){
                    // Displaying options for different user roles and functionalities
                    System.out.println("\n🌟🌟 Landing Page 🌟🌟");
                    System.out.println("\nPlease choose the user you want to login as:");
                    System.out.println("1. Admin");
                    System.out.println("2. Security");
                    System.out.println("3. Driver");
                    System.out.println("4. View Tables");
                    System.out.println("5. Reset Tables");
                    System.out.println("6. Exit");

                    System.out.println("Enter your choice: \t");
                    int choice = scanner.nextInt();
                    Views l = new Views();
                    switch (choice){
                        case 1:
                            staff = 1;  // Set user as Admin
                            l.AdminView(connection);
                            break;

                        case 2:
                            staff = 2;  // Set user as Security
                            l.SecurityView(connection);
                            break;

                        case 3:
                            l.DriverView(connection);
                            break;

                        case 4:
                            // View current state of tables
                            ViewTable vt = new ViewTable();
                            vt.run(connection);
                            break;

                        case 5:
                            // Reset tables to their initial state
                            PrepareTable.createTable(connection);
                            PrepareTable.insertData(connection);
                            break;

                        case 6:
                            // Exit the program
                            break;

                        default:
                            System.out.println("Invalid Input, Please try again.");
                    }

                    if(choice == 6) {
                        break; // Exit the while loop if user chooses to exit
                    }
                }
            }finally{
                // Closing database connection before exiting the program
                System.out.println("Exiting program and closing all connections....");
                if(connection!=null){
                    connection.close();
                    System.out.println("Connection closed!");
                }
            }
        }catch(Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }
    }

}