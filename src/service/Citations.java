package service;

import service.helpers.ResultSetService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Citations {
    Scanner scanner = new Scanner(System.in);
    ResultSetService resultSetService = new ResultSetService();


    public void run(Connection conn){

        try{
            while (true){
                System.out.println("\nGENERATE AND MAINTAIN CITATIONS:");
                System.out.println("1. Check the Validity of the Permit - No Permit");
                System.out.println("2. Check the Validity of the Permit - Expired Permit");
                System.out.println("3. Check the Validity of the Permit - Invalid Permit");
                System.out.println("4. Generate a Citation");
                System.out.println("5. Appeal  a Citation");
                System.out.println("6. Pay Citation Fees");
                System.out.println("7. Update a Citation");
                System.out.println("8. Return to Main Menu\n");
                System.out.println("Enter you choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                case 1:
                    /* Call Method to get Total Citations */
                    getValidityforNoPermit(conn);
                    break;
                case 2:
                    /* Call Method to get Total Citations per lot*/
                    getValidityforExpiredPermit(conn);
                    break;
                case 3:
                    /* Call Method to get Total Citations per lot within two dates */
                	getValidityforInvalidPermit(conn);
                    break;
                case 4:
                    /* Call Method to get list of zones for each lot  */
                    generateCitations(conn);
                    break;
                case 5:
                    /* Call Method to get number of cars that are currently in violation  */
                    appealCitation(conn);
                    break;
                case 6:
                    /* Call Method to get the number of employees having permits for a given parking zone */
                    payCitationFee(conn);
                    break;
                case 7:
                    /* Call Method to get permit information given the univID or phone number  */
                    updateCitation(conn);
                    break;
                case 8:
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
    
   
    private boolean getValidityforNoPermit(Connection conn) {
        try {
            System.out.println("Vehicles in the database:");
            resultSetService.runQueryAndPrintOutput(conn, "SELECT * from Vehicle;");

            System.out.println("Enter the License No:");
            final String LNo = scanner.nextLine();

            final String sqlQuery = "SELECT CASE WHEN P.LicenseNo IS NOT NULL THEN CAST(P.PermitID AS VARCHAR) ELSE 'No Permit' END AS PermitExists FROM Permit P WHERE P.LicenseNo = ? ;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, LNo);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);

        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }
    
    private boolean getValidityforExpiredPermit(Connection conn) {
        try {
            System.out.println("Vehicles in the database:");
            resultSetService.runQueryAndPrintOutput(conn, "SELECT * from Vehicle;");

            System.out.println("Enter the License No:");
            final String LNo = scanner.nextLine();

            final String sqlQuery = "SELECT P.PermitID, P.StartDate, P.ExpirationDate, P.ExpirationTime, NOW() AS CurrentDateTime FROM Permit P, PermitLocation Pl, Space S WHERE P.PermitID = Pl.PermitID AND Pl.PLName = S.PLName AND Pl.ZoneID = S.ZoneID AND Pl.SpaceNo = S.SpaceNo AND P.StartDate <= CURDATE() AND CURDATE() <= P.ExpirationDate AND P.ExpirationTime >= CURTIME() AND P.LicenseNo = ? ;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, LNo);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);

        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }
    
    private boolean getValidityforInvalidPermit(Connection conn) {
        try {
        	System.out.println("Parking Lots in the database:");
            resultSetService.runQueryAndPrintOutput(conn, "SELECT * from ParkingLot;");

            System.out.println("Enter the PLName");
            final String PLName = scanner.nextLine();
            
            System.out.println("Enter the ZoneID:");
            final String ZoneID = scanner.nextLine();
            
            System.out.println("Enter the SpaceNo:");
            final String SpaceNo = scanner.nextLine();
            
            System.out.println("Vehicles in the database:");
            resultSetService.runQueryAndPrintOutput(conn, "SELECT * from Vehicle;");

            System.out.println("Enter the License No:");
            final String LNo = scanner.nextLine();

            final String sqlQuery = "SELECT P.PermitID, CASE WHEN PL.ZoneID = ? AND PL.PLName = ? AND PL.SpaceNo = ? THEN 'yes' ELSE 'no' END AS IsValid FROM PermitLocation PL INNER JOIN Permit P ON PL.PermitID = P.PermitID WHERE P.LicenseNo = ?;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, ZoneID);
            statement.setString(2, PLName);
            statement.setString(3, SpaceNo);
            statement.setString(4, LNo);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);

        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }
    
    private boolean generateCitations(Connection conn) {
        try {

            System.out.println("Enter the License No:");
            final String LNo = scanner.nextLine();
            
            System.out.println("Enter the Citation No:");
            final String CNo = scanner.nextLine();
            
            System.out.println("Enter the PLName");
            final String PLName = scanner.nextLine();
            
            System.out.println("Enter the Staff ID");
            final String StaffID = scanner.nextLine();
            
            System.out.println("Enter the Citation Type");
            final String CName = scanner.nextLine();

            final String sqlQuery = "INSERT INTO Citation (CitationNo, CitationDate, CitationTime, PaymentStatus, StaffID, LicenseNo, PLName, CitationName) VALUES (?, '2023-09-10', '11:00:00', 'Pending', ?, ?, ?, ?);";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, CNo);
            statement.setString(2, LNo);
            statement.setString(3, PLName);
            statement.setString(4, StaffID);
            statement.setString(5, CName);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);

        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }
    
    private boolean appealCitation(Connection conn) {
        try {
            System.out.println("Citations in the database:");
            resultSetService.runQueryAndPrintOutput(conn, "SELECT * from Citation;");

            System.out.println("Enter the Citation No:");
            final String CNo = scanner.nextLine();
            
            System.out.println("Enter the Driver ID:");
            final String DriverID = scanner.nextLine();

            final String sqlQuery = "INSERT INTO Appeals (DriverID, CitationNo, AppealStatus)  VALUES (? , ? , 'Requested');";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, CNo);
            statement.setString(2, DriverID);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);

        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }
    
    private boolean payCitationFee(Connection conn) {
        try {
            System.out.println("Ciatations in the database:");
            resultSetService.runQueryAndPrintOutput(conn, "SELECT * from Citation;");

            System.out.println("Enter the Citation No:");
            final String CNo = scanner.nextLine();

            final String sqlQuery = "UPDATE Citation SET PaymentStatus = 'Complete' WHERE CitationNo= ? ;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, CNo);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);

        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }
    
    private boolean updateCitation(Connection conn) {
        try {
            final String sqlQuery = "UPDATE Citation SET CitationName = ‘Expired Permit’ WHERE CitationDate > 2022-06-01;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);

        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }    
    
}
