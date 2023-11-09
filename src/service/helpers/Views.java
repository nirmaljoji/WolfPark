package service.helpers;

import service.Citations;
import service.GenerateReports;
import service.InformationProcessing;
import service.VehiclePermit;

import java.sql.Connection;
import java.util.Scanner;

import static java.lang.System.exit;

public class Views {

    public void AdminView(Connection connection) {

        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n🌟 Choose one of the following options:");
                System.out.println("1. Information Processing");
                System.out.println("2. Maintain Permit and Vehicle Information");
                System.out.println("3. Generate Reports");
                System.out.println("4. Reset Tables");
                System.out.println("5. Logout");
                System.out.println("6. Exit");


                System.out.println("Enter your choice: \t");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        InformationProcessing ip = new InformationProcessing();
                        ip.run(connection);
                        break;

                    case 2:
                        VehiclePermit vp = new VehiclePermit();
                        vp.run(connection);
                        break;

                    case 3:
                        GenerateReports gr = new GenerateReports();
                        gr.run(connection);
                        break;

                    case 4:
                        PrepareTable.createTable(connection);
                        PrepareTable.insertData(connection);
                        break;

                    case 5:
                        return;

                    case 6:
                        System.out.println("Exiting program and closing all connections....");
                        if(connection!=null){
                            connection.close();
                            System.out.println("Connection closed!");
                        }
                        exit(0);
                        break;
                    default:
                        System.out.println("Invalid Input, Please try again.");
                }
            }


        } catch (Exception ex) {
            System.out.println("Exception details: " + ex.getMessage());
        }

    }

    public void SecurityView(Connection connection) {

        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n🌟 Choose one of the following options:");
                System.out.println("1. Generate and Maintain Citations");
                System.out.println("2. Reset Tables");
                System.out.println("3. Logout");
                System.out.println("4. Exit");


                System.out.println("Enter your choice: \t");
                int choice = scanner.nextInt();

                switch (choice) {

                    case 1:
                        Citations citation = new Citations();
                        citation.run(connection);
                        break;

                    case 2:
                        PrepareTable.createTable(connection);
                        PrepareTable.insertData(connection);
                        break;

                    case 3:
                        return;

                    case 4:
                        System.out.println("Exiting program and closing all connections....");
                        if(connection!=null){
                            connection.close();
                            System.out.println("Connection closed!");
                        }
                        exit(0);
                        break;
                    default:
                        System.out.println("Invalid Input, Please try again.");
                }
            }


        } catch (Exception ex) {
            System.out.println("Exception details: " + ex.getMessage());
        }

    }

    public void DriverView(Connection connection) {

        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n🌟 Choose one of the following options:");
                System.out.println("1. View Permits");
                System.out.println("2. View Vehicles");
                System.out.println("3. View Citations");
                System.out.println("4. Appeal Citation");
                System.out.println("5. Reset Tables");
                System.out.println("6. Logout");
                System.out.println("7. Exit");


                System.out.println("Enter your choice: \t");
                int choice = scanner.nextInt();

                switch (choice) {
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
                        return;

                    case 7:
                        System.out.println("Exiting program and closing all connections....");
                        if(connection!=null){
                            connection.close();
                            System.out.println("Connection closed!");
                        }
                        exit(0);
                        break;
                    default:
                        System.out.println("Invalid Input, Please try again.");
                }
            }


        } catch (Exception ex) {
            System.out.println("Exception details: " + ex.getMessage());
        }

    }

}
