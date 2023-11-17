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

    // Admin interface view with various administrative options.
    public void AdminView(Connection connection) {

        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                // Displaying admin options.
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
                // Switch case to handle various admin functionalities.
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
                        return; // Return to the previous menu.

                    case 7:
                        // Exiting the program and closing the connection.
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

    // Security interface view with options related to citations and reports.
    public void SecurityView(Connection connection) {

        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                // Displaying security options
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

                // Switch case for handling security functionalities
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
                        return; // Return to previous menu.

                    case 6:
                        // Exiting the program and closing the connection.
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

    // Driver interface view with options for viewing and managing driver-specific information.
    public void DriverView(Connection connection) {

        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                // Displaying driver options.
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

                // Switch case for handling driver functionalities.
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
                        return; // Return to the previous menu

                    case 10:
                        // Exiting the program and closing the connection.
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
