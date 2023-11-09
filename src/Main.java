import service.*;
import service.InformationProcessing;

import java.sql.Connection;
import java.util.Scanner;


public class Main {


    static ConnectionHelper connectionHelper = new ConnectionHelper();
    static Connection connection = null;


    public static void main(String[] args) {

        try{

            try{
                connection = connectionHelper.getConnection();
                Scanner scanner = new Scanner(System.in);

                System.out.println("********************************************");
                System.out.println("*   WELCOME TO THE WOLF PARKING MANAGEMENT   *");
                System.out.println("*                   SYSTEM                   *");
                System.out.println("********************************************");

                while(true){
                    System.out.println("\n🌟 Choose one of the following options:");
                    System.out.println("1. Information Processing");
                    System.out.println("2. Generate and Maintain Citations");
                    System.out.println("3. Maintain Permit and Vehicle Information");
                    System.out.println("4. Generate Reports");
                    System.out.println("5. Reset Tables");
                    System.out.println("6. Exit");


                    System.out.println("Enter your choice: \t");
                    int choice = scanner.nextInt();

                    switch (choice){
                        case 1:
                            InformationProcessing ip = new InformationProcessing();
                            ip.run(connection);
                            break;

                        case 2:
                            Citations citation = new Citations();
                            citation.run(connection);
                            break;

                        case 3:
                            VehiclePermit vp = new VehiclePermit();
                            vp.run(connection);
                            break;

                        case 4:
                            GenerateReports gr = new GenerateReports();
                            gr.run(connection);
                            break;

                        case 5:
                            PrepareTable.createTable(connection);
                            PrepareTable.insertData(connection);
                            break;

                        case 6:
                            break;

                        default:
                            System.out.println("Invalid Input, Please try again.");
                    }

                    if(choice == 6) {
                        break;
                    }
                }

            } finally{
                System.out.println("Exiting program and closing all connections....");
                if(connection!=null){
                    connection.close();
                    System.out.println("Connection closed!");
                }
            }


        } catch(Exception ex){
            System.out.println("Exception details: " + ex.getMessage() );
        }

    }
}