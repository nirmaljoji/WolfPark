import service.*;
import service.InformationProcessing;
import service.helpers.ConnectionHelper;
import service.helpers.Login;
import service.helpers.PrepareTable;

import java.sql.Connection;
import java.util.Scanner;

import static java.lang.System.exit;


public class Main {


    static ConnectionHelper connectionHelper = new ConnectionHelper();
    static Connection connection = null;

    static int staff = 0; // 1 - Admin , 2 - Security , 3 - Driver

    public static void main(String[] args){
        try{
            try{
                connection = connectionHelper.getConnection();
                Scanner scanner = new Scanner(System.in);

                System.out.println("********************************************");
                System.out.println("*   WELCOME TO THE WOLF PARKING MANAGEMENT   *");
                System.out.println("*                   SYSTEM                   *");
                System.out.println("********************************************");

                while(true){
                    System.out.println("\n🌟 Please choose the user you want to login as:");
                    System.out.println("1. Admin");
                    System.out.println("2. Security");
                    System.out.println("3. Driver");
                    System.out.println("4. Exit");

                    System.out.println("Enter your choice: \t");
                    int choice = scanner.nextInt();
                    Login l = new Login();
                    switch (choice){
                        case 1:
                            staff = 1;
                            l.AdminView(connection);
                            break;

                        case 2:
                            staff = 2;
                            l.SecurityView(connection);
                            break;

                        case 3:
                            staff = 3;
                            l.DriverView(connection);
                            break;

                        case 4:
                            break;

                        default:
                            System.out.println("Invalid Input, Please try again.");
                    }

                    if(choice == 4) {
                        break;
                    }
                }
            }finally{
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