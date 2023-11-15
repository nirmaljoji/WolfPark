package service;

import service.helpers.ResultSetService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class GenerateReports {
    Scanner scanner = new Scanner(System.in);
    ResultSetService resultSetService = new ResultSetService();


    public void run(Connection conn){

        try{

            while (true){
                System.out.println("\nGENERATE REPORTS:");
                System.out.println("1. Get total number of citations");
                System.out.println("2. Get total number of citations per lot");
                System.out.println("3. Get total number of citations in each parking lot between two dates");
                System.out.println("4. Get the Zones for each Parking Lot");
                System.out.println("5. Get the number of cars currently in violation");
                System.out.println("6. Get the number of employees with Permits in a Parking Zone");
                System.out.println("7. Get the Permit information of a driver");
                System.out.println("8. Get Available Spaces in a given Parking Lot");
                System.out.println("9. Return to Main Menu\n");
                System.out.println("Enter you choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        /* Call Method to get Total Citations */
                        getTotalCitations(conn);
                        break;
                    case 2:
                        /* Call Method to get Total Citations per lot*/
                        getTotalCitationsPerLot(conn);
                        break;
                    case 3:
                        /* Call Method to get Total Citations per lot within two dates */
                        getTotalCitationsForDatesPerLot(conn);
                        break;
                    case 4:
                        /* Call Method to get list of zones for each lot  */
                        getZones(conn);
                        break;
                    case 5:
                        /* Call Method to get number of cars that are currently in violation  */
                        getCarViolations(conn);
                        break;
                    case 6:
                        /* Call Method to get the number of employees having permits for a given parking zone */
                        getEmployeesWithPermitsInZone(conn);
                        break;
                    case 7:
                        /* Call Method to get permit information given the univID or phone number  */
                        getPermitInformation(conn);
                        break;
                    case 8:
                        /* Call Method to get an available space number given a space type in a given parking lot */
                        getAvailableSpace(conn);
                        break;
                    case 9:
                        return;
                    default:
                        System.out.println("Invalid Input");
                }
                if(choice == 9) {
                    break;
                }
            }
        }catch( Exception ex){
            System.out.println("Exception: "+ ex.getMessage());

        }

    }

    /* Method to get Total Citations */
    private boolean getTotalCitations(Connection conn) {
        try {
            final String sqlQuery = "SELECT COUNT(CitationNo) AS TotalCitations FROM Citation;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);
        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }

    /* Method to get Total Citations per lot*/
    private boolean getTotalCitationsPerLot(Connection conn) {
        try {
            final String sqlQuery = "SELECT PLName, COUNT(CitationNo) AS Citations FROM Citation GROUP BY PLName;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);
        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }

    /* Method to get Total Citations per lot within two dates */
    private boolean getTotalCitationsForDatesPerLot(Connection conn) {
        try {
            System.out.println("Enter Start Date (yyyy-mm-dd):");
            final String startDate = scanner.nextLine();

            System.out.println("Enter End Date (yyyy-mm-dd):");
            final String endDate = scanner.nextLine();

            final String sqlQuery = "SELECT PLName, COUNT(CitationNo) AS Citations FROM Citation WHERE CitationDate BETWEEN ? AND ? GROUP BY PLName;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, startDate);
            statement.setString(2, endDate);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);

        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }

    /* Method to get list of zones for each lot  */
    private boolean getZones(Connection conn) {
        try {
            final String sqlQuery = "SELECT PLName, ZoneID FROM Zone;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);
        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }

    /* Method to get number of cars that are currently in violation */
    private boolean getCarViolations(Connection conn) {
        try {
            final String sqlQuery = "SELECT COUNT(DISTINCT LicenseNo) AS CarsInViolation FROM Citation WHERE PaymentStatus = \"Pending\";";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);
        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }

    /* Method to get the number of employees having permits for a given parking zone */
    private boolean getEmployeesWithPermitsInZone(Connection conn) {
        try {
            System.out.println("Choose from the following:\n 1. Get permits for a Zone w.r.t Parking Lot.\n 2. Get all permits for a given Zone.");
            final int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                System.out.println("Parking Lots in the database:");
                resultSetService.runQueryAndPrintOutput2(conn, "SELECT * from ParkingLot;");

                System.out.println("Employee Zones in the database:");
                resultSetService.runQueryAndPrintOutput2(conn, "SELECT * from Zone WHERE ZoneID IN ('A','B','C','D');");

                System.out.println("Enter Parking Lot:");
                final String parkingLot = scanner.nextLine();

                System.out.println("Enter Zone ID (A,B,C,D):");
                final String zoneID = scanner.nextLine();

                final String sqlQuery = "SELECT COUNT(D.DriverID) AS EmployessWithPermits FROM PermitLocation PL INNER JOIN Permit P ON PL.PermitID = P.PermitID INNER JOIN Vehicle V ON V.LicenseNo = P.LicenseNo  INNER JOIN Driver D ON V.DriverID = D.DriverID WHERE PL.PLName = ? AND PL.ZoneID = ? AND D.Status = 'E';";
                PreparedStatement statement = conn.prepareStatement(sqlQuery);
                statement.setString(1, parkingLot);
                statement.setString(2, zoneID);

                ResultSet resultSet = statement.executeQuery();
                resultSetService.viewFromResultSet(resultSet);
            }
            else if (option == 2) {
                System.out.println("Employee Zones in the database:");
                resultSetService.runQueryAndPrintOutput2(conn, "SELECT * from Zone WHERE ZoneID IN ('A','B','C','D');");

                System.out.println("Enter Zone ID (A,B,C,D):");
                final String zoneID = scanner.nextLine();

                final String sqlQuery = "SELECT COUNT(D.DriverID) AS EmployessWithPermits FROM PermitLocation PL INNER JOIN Permit P ON PL.PermitID = P.PermitID INNER JOIN Vehicle V ON V.LicenseNo = P.LicenseNo  INNER JOIN Driver D ON V.DriverID = D.DriverID WHERE PL.ZoneID = ? AND D.Status = 'E';";
                PreparedStatement statement = conn.prepareStatement(sqlQuery);
                statement.setString(1, zoneID);

                ResultSet resultSet = statement.executeQuery();
                resultSetService.viewFromResultSet(resultSet);
            }
            else {
                System.out.println("Invalid Input. Try Again");
            }

        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }

    /* Method to get permit information given the univID or phone number */
    private boolean getPermitInformation(Connection conn) {
        try {
            System.out.println("Drivers in the database:");
            resultSetService.runQueryAndPrintOutput2(conn, "SELECT * from Driver;");

            System.out.println("Enter Driver ID:");
            final String driverID = scanner.nextLine();

            final String sqlQuery = "SELECT D.DriverID, P.PermitID, PL.PLName, PL.ZoneID, S.SpaceType, P.LicenseNo, P.StartDate, P.ExpirationDate, P.ExpirationTime, P.PermitType FROM Permit P INNER JOIN Vehicle V ON P.LicenseNo = V.LicenseNo INNER JOIN Driver D ON V.DriverID = D.DriverID INNER JOIN PermitLocation PL on PL.PermitID = P.PermitID INNER JOIN Space S ON S.SpaceNo = PL.SpaceNo and S.PLName = PL.PLName and S.ZoneID = PL.ZoneID WHERE D.DriverID = ?;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, driverID);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);

        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }

    /* Method to get an available space number given a space type in a given parking lot */
    private boolean getAvailableSpace(Connection conn) {
        try {
            System.out.println("Parking Lots in the database:");
            resultSetService.runQueryAndPrintOutput2(conn, "SELECT * from ParkingLot;");

            System.out.println("Zones in the database:");
            resultSetService.runQueryAndPrintOutput2(conn, "SELECT * from Zone;");

            System.out.println("Enter Parking Lot:");
            final String parkingLot = scanner.nextLine();

            System.out.println("Enter Zone ID (A,B,C,D,AS,BS,CS,DS,V):");
            final String zoneID = scanner.nextLine();

            System.out.println("Enter Space Type (Regular, Handicap, Electric, Compact Car):");
            final String spaceType = scanner.nextLine();

            final String sqlQuery = "SELECT PL.PLName, PL.ZoneID, S.SpaceNo FROM ParkingLocation PL INNER JOIN Space S ON S.SpaceNo = PL.SpaceNo and S.PLName = PL.PLName and S.ZoneID = PL.ZoneID WHERE PL.PLName = ? and PL.ZoneID= ? AND SpaceType = ? and PL.AvailabilityStatus = TRUE limit 1;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, parkingLot);
            statement.setString(2, zoneID);
            statement.setString(3, spaceType);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);

        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }

}
