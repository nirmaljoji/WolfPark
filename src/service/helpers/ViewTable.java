package service.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class ViewTable {

    Scanner scanner = new Scanner(System.in);
    ResultSetService resultSetService = new ResultSetService();
    public void run(Connection conn) {
        try {
            while (true) {
                System.out.println("\nTABLES IN THE DATABASE:");
                System.out.println("1. Appeals");
                System.out.println("2. Citation");
                System.out.println("3. CitationCategory");
                System.out.println("4. Driver");
                System.out.println("5. ParkingLocation");
                System.out.println("6. ParkingLot");
                System.out.println("7. Permit");
                System.out.println("8. PermitLocation");
                System.out.println("9. Space");
                System.out.println("10. Staff");
                System.out.println("11. Vehicle");
                System.out.println("12. VehicleModelManufacturer");
                System.out.println("13. Zone");
                System.out.println("14. Return to Main Menu\n");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        runSelectAllQuery(conn, "Appeals");
                        break;
                    case 2:
                        runSelectAllQuery(conn, "Citation");
                        break;
                    case 3:
                        runSelectAllQuery(conn, "CitationCategory");
                        break;
                    case 4:
                        runSelectAllQuery(conn, "Driver");
                        break;
                    case 5:
                        runSelectAllQuery(conn, "ParkingLocation");
                        break;
                    case 6:
                        runSelectAllQuery(conn, "ParkingLot");
                        break;
                    case 7:
                        runSelectAllQuery(conn, "Permit");
                        break;
                    case 8:
                        runSelectAllQuery(conn, "PermitLocation");
                        break;
                    case 9:
                        runSelectAllQuery(conn, "Space");
                        break;
                    case 10:
                        runSelectAllQuery(conn, "Staff");
                        break;
                    case 11:
                        runSelectAllQuery(conn, "Vehicle");
                        break;
                    case 12:
                        runSelectAllQuery(conn, "VehicleModelManufacturer");
                        break;
                    case 13:
                        runSelectAllQuery(conn, "Zone");
                        break;
                    case 14:
                        return;
                    default:
                        System.out.println("Invalid Input");
                }
                if (choice == 14) {
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    private void runSelectAllQuery(Connection conn, String tableName) {
        try {
            String selectAllQuery = "SELECT * FROM " + tableName;
            try (PreparedStatement selectAllStatement = conn.prepareStatement(selectAllQuery);
                 ResultSet resultSet = selectAllStatement.executeQuery()) {

                System.out.println("All records from the " + tableName + " table:");
                resultSetService.runQueryAndPrintOutput2(conn, selectAllQuery);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

}
