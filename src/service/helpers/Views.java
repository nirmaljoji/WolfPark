package service.helpers;

import service.Citations;
import service.GenerateReports;
import service.InformationProcessing;
import service.VehiclePermit;

import java.sql.Connection;
import java.util.Scanner;

import static java.lang.System.exit;

public class Views {
    ResultSetService resultSetService = new ResultSetService();

    public void AdminView(Connection connection) {

        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n🌟🌟 ADMIN VIEW 🌟🌟");
                System.out.println("\nChoose one of the following options:");
                System.out.println("1. Information Processing");
                System.out.println("2. Maintain Permit and Vehicle Information");
                System.out.println("3. Generate Reports");
                System.out.println("4. View Tables");
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
                        VehiclePermit vp = new VehiclePermit();
                        vp.run(connection);
                        break;

                    case 3:
                        GenerateReports gr = new GenerateReports();
                        gr.run(connection);
                        break;

                    case 4:
                        ViewTable vt = new ViewTable();
                        vt.run(connection);
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

    public void SecurityView(Connection connection) {

        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n🌟🌟 SECURITY VIEW 🌟🌟");
                System.out.println("\nChoose one of the following options:");
                System.out.println("1. Generate and Maintain Citations");
                System.out.println("2. Generate Reports");
                System.out.println("3. View Tables");
                System.out.println("4. Reset Tables");
                System.out.println("5. Logout");
                System.out.println("6. Exit");


                System.out.println("Enter your choice: \t");
                int choice = scanner.nextInt();

                switch (choice) {

                    case 1:
                        Citations citation = new Citations();
                        citation.run(connection);
                        break;

                    case 2:
                        GenerateReports gr = new GenerateReports();
                        gr.run(connection);
                        break;

                    case 3:
                        ViewTable vt = new ViewTable();
                        vt.run(connection);
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

    public void DriverView(Connection connection) {

        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n🌟🌟 DRIVER VIEW 🌟🌟");
                System.out.println("\nChoose one of the following options:");
                System.out.println("1. View Driver Information");
                System.out.println("2. View Vehicles");
                System.out.println("3. View Permits");
                System.out.println("4. View Citations");
                System.out.println("5. Appeal Citation");
                System.out.println("6. Pay Citation Fee");
                System.out.println("7. View Tables");
                System.out.println("8. Reset Tables");
                System.out.println("9. Logout");
                System.out.println("10. Exit");


                System.out.println("Enter your choice: \t");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.println("Enter Driver ID:");
                        String driverId = scanner.next();

                        // Execute SQL query to view driver information
                        resultSetService.runQueryAndPrintOutput2(connection, "SELECT * FROM Driver WHERE DriverID = '" + driverId + "'");
                        break;

                    case 2:
                        System.out.println("Enter Driver ID:");
                        String driverId2 = scanner.next(); // Assuming Driver ID is a string, modify as needed

                        // Execute SQL query to view vehicles for the given driver
                        resultSetService.runQueryAndPrintOutput2(connection, "SELECT * FROM Vehicle " +
                                "WHERE DriverID = '" + driverId2 + "'");
                        break;


                    case 3:
                        System.out.println("Enter Driver ID:");
                        String driverId3 = scanner.next(); // Assuming Driver ID is a string, modify as needed

                        // Execute SQL query to view permits for the given driver
                        resultSetService.runQueryAndPrintOutput2(connection, "SELECT * FROM Permit " +
                                "WHERE Permit.LicenseNo IN (SELECT LicenseNo FROM Vehicle WHERE DriverID = '" + driverId3 + "')");
                        break;

                    case 4:
                        System.out.println("Enter Driver ID:");
                        String driverId4 = scanner.next(); // Assuming Driver ID is a string, modify as needed

                        // Execute SQL query to view citations for the given driver
                        resultSetService.runQueryAndPrintOutput2(connection, "SELECT * FROM Citation " +
                                "INNER JOIN Vehicle ON Citation.LicenseNo = Vehicle.LicenseNo " +
                                "WHERE Vehicle.DriverID = '" + driverId4 + "'");
                        break;

                    case 5:
                        Citations c = new Citations();
                        c.appealCitation(connection);
                        break;

                    case 6:
                        Citations c1 = new Citations();
                        c1.payCitationFee(connection);
                        break;

                    case 7:
                        ViewTable vt = new ViewTable();
                        vt.run(connection);
                        break;

                    case 8:
                        PrepareTable.createTable(connection);
                        PrepareTable.insertData(connection);
                        break;

                    case 9:
                        return;

                    case 10:
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
