package service;

import service.helpers.ResultSetService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class Citations {
    Scanner scanner = new Scanner(System.in);
    ResultSetService resultSetService = new ResultSetService();


    public void run(Connection conn){

        try{
            while (true){
                System.out.println("\nGENERATE AND MAINTAIN CITATIONS:");
                System.out.println("1. Check the permit validity for a vehicle");
                System.out.println("2. Generate a Citation");
                System.out.println("3. Appeal  a Citation");
                System.out.println("4. Pay Citation Fees");
                System.out.println("5. Update a Citation");
                System.out.println("6. Accept or reject an Appeal");
                System.out.println("7. Return to Main Menu\n");
                System.out.println("Enter you choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                case 1:
                    checkVehiclePermitValidity(conn);
                    break;
                case 2:
                	generateCitationfromScratch(conn);
                    break;
                case 3:
                    appealCitation(conn);
                    break;
                case 4:
                    payCitationFee(conn);
                    break;
                case 5:
                    updateCitation(conn);
                    break;
                case 6:
                    handleAppeal(conn);
                    break;
                case 7:
                    return;
                default:
                        System.out.println("Invalid Input");
                }
                if(choice == 7) {
                    break;
                }
            }
        }catch( Exception ex){
            System.out.println("Exception: "+ ex.getMessage());

        }

    }    

    private boolean checkVehiclePermitValidity(Connection conn) {
        try {
            Scanner scanner = new Scanner(System.in);

            // Input values
            System.out.println("Enter LicenseNo:");
            String licenseNo = scanner.nextLine();

            System.out.println("Enter PLName:");
            String plName = scanner.nextLine();

            System.out.println("Enter ZoneID:");
            String zoneID = scanner.nextLine();

            System.out.println("Enter SpaceNo:");
            String spaceNo = scanner.nextLine();
            
         // Check if the vehicle has a permit
            String permitQuery = "SELECT * FROM Permit WHERE LicenseNo = ?";
            try (PreparedStatement permitStatement = conn.prepareStatement(permitQuery)) {
                permitStatement.setString(1, licenseNo);
                ResultSet permitResult = permitStatement.executeQuery();

                if (!permitResult.next()) {
                    System.out.println("No permit found for the given LicenseNo.");

                    // Prompt user to generate citation
                    System.out.println("Do you want to generate a citation? (yes/no)");
                    String generateCitationInput = scanner.nextLine().toLowerCase();

                    if (generateCitationInput.equals("yes")) {
                        generateCitation(conn, licenseNo, plName, "No Permit");
                    }

                    return false;
                }
             // Check if the permit is expired
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String expirationDateStr = permitResult.getString("ExpirationDate");

                if (expirationDateStr != null) {
                    Date expirationDate = dateFormat.parse(expirationDateStr);

                    if (currentDate.after(expirationDate)) {
                        System.out.println("Expired permit. Vehicle is not parked correctly.");

                        // Prompt user to generate citation
                        System.out.println("Do you want to generate a citation? (yes/no)");
                        String generateCitationInput = scanner.nextLine().toLowerCase();

                        if (generateCitationInput.equals("yes")) {
                            generateCitation(conn, licenseNo, plName, "Expired Permit");
                        }

                        return false;
                    }
                }
            }

            // Check if the permit matches input values
            String plZoneSpaceQuery = "SELECT * FROM PermitLocation " +
                    "INNER JOIN Permit ON PermitLocation.PermitID = Permit.PermitID " +
                    "WHERE Permit.LicenseNo = ?";
            try (PreparedStatement plZoneSpaceStatement = conn.prepareStatement(plZoneSpaceQuery)) {
                plZoneSpaceStatement.setString(1, licenseNo);
                ResultSet plZoneSpaceResult = plZoneSpaceStatement.executeQuery();

                if (plZoneSpaceResult.next()) {
                    String permitPLName = plZoneSpaceResult.getString("PLName");
                    String permitZoneID = plZoneSpaceResult.getString("ZoneID");
                    String permitSpaceNo = plZoneSpaceResult.getString("SpaceNo");

                    if (!plName.equals(permitPLName) || !zoneID.equals(permitZoneID) || !spaceNo.equals(permitSpaceNo)) {
                        System.out.println("Invalid permit. Vehicle is not parked correctly.");

                        // Prompt user to generate citation
                        System.out.println("Do you want to generate a citation? (yes/no)");
                        String generateCitationInput = scanner.nextLine().toLowerCase();

                        if (generateCitationInput.equals("yes")) {
                            generateCitation(conn, licenseNo, plName, "Invalid Permit");
                        }

                        return false;
                    } else {
                        System.out.println("Permit is valid. Vehicle is parked correctly.");
                        return true;
                    }
                } else {
                    System.out.println("Invalid permit. Vehicle is not parked correctly.");

                    // Prompt user to generate citation
                    System.out.println("Do you want to generate a citation? (yes/no)");
                    String generateCitationInput = scanner.nextLine().toLowerCase();

                    if (generateCitationInput.equals("yes")) {
                        generateCitation(conn, licenseNo, plName, "Invalid Permit");
                    }

                    return false;
                }
            }

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return false;
        }
    }
    
    private boolean generateCitation(Connection conn, String licenseNo, String plName, String citationName) {
        try {
            System.out.println("Enter CitationNo:");
            String citationNo = scanner.nextLine();

            final int StaffID = 2;
            
            // Check vehicle category
            String vehicleCategoryQuery = "SELECT VehicleCategory FROM Vehicle WHERE LicenseNo = ?";
            try (PreparedStatement vehicleCategoryStatement = conn.prepareStatement(vehicleCategoryQuery)) {
                vehicleCategoryStatement.setString(1, licenseNo);
                ResultSet vehicleCategoryResult = vehicleCategoryStatement.executeQuery();

                if (vehicleCategoryResult.next()) {
                    String vehicleCategory = vehicleCategoryResult.getString("VehicleCategory");

                    // Add "Handicap" at the beginning of citationName if the vehicle category is "Handicap"
                    if ("Handicap".equalsIgnoreCase(vehicleCategory)) {
                        citationName = "Handicap " + citationName;
                    }
                }
            }
            System.out.println(citationName);

                // Insert a new entry into the Citation table
            	String date = LocalDate.now().toString();
            	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            	LocalTime localTime = LocalTime.now();
            	String time = dtf.format(localTime);
                String insertCitationQuery = "INSERT INTO Citation (CitationNo, CitationDate, CitationTime, PaymentStatus, StaffID, LicenseNo, PLName, CitationName) VALUES (?, ?, ?, 'Pending', ?, ?, ?, ?)";
                try (PreparedStatement insertCitationStatement = conn.prepareStatement(insertCitationQuery)) {
                    insertCitationStatement.setString(1, citationNo);
                    insertCitationStatement.setString(2, date);
                    insertCitationStatement.setString(3, time);
                    insertCitationStatement.setInt(4, StaffID);
                    insertCitationStatement.setString(5, licenseNo);
                    insertCitationStatement.setString(6, plName);
                    insertCitationStatement.setString(7, citationName);
                    System.out.println(insertCitationStatement.toString());

                    int rowsAffected = insertCitationStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Citation generated successfully.");
                        return true;
                    } else {
                        System.out.println("Failed to generate citation.");
                        return false;
                    }
                }
        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
    }
    
    private boolean generateCitationfromScratch(Connection conn) {
        try {
            final int StaffID = 2;

            System.out.println("Enter the Vehicle License No:");
            final String LNo = scanner.nextLine();
            
            System.out.println("Enter the Citation No:");
            final String CNo = scanner.nextLine();
            
            System.out.println("Enter the PLName:");
            final String PLName = scanner.nextLine();
            
            System.out.println("Enter the Citation Type (No Permit/Expired Permit/Invalid Permit):");
            String CName = scanner.nextLine();
            
            // Check vehicle category
            String vehicleCategoryQuery = "SELECT VehicleCategory FROM Vehicle WHERE LicenseNo = ?";
            try (PreparedStatement vehicleCategoryStatement = conn.prepareStatement(vehicleCategoryQuery)) {
                vehicleCategoryStatement.setString(1, LNo);
                ResultSet vehicleCategoryResult = vehicleCategoryStatement.executeQuery();

                if (vehicleCategoryResult.next()) {
                    String vehicleCategory = vehicleCategoryResult.getString("VehicleCategory");

                    // Add "Handicap" at the beginning of citationName if the vehicle category is "Handicap"
                    if ("Handicap".equalsIgnoreCase(vehicleCategory)) {
                        CName = "Handicap " + CName;
                    }
                }
            }

            final String sqlQuery = "INSERT INTO Citation (CitationNo, CitationDate, CitationTime, PaymentStatus, StaffID, LicenseNo, PLName, CitationName) VALUES (?, '2023-09-10', '11:00:00', 'Pending', ?, ?, ?, ?);";
            
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, CNo);
            statement.setString(3, LNo);
            statement.setString(4, PLName);
            statement.setInt(2, StaffID);
            statement.setString(5, CName);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);

        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }
    
    public boolean appealCitation(Connection conn) {
        try {
        	System.out.println("Enter the Driver ID:");
        	final String dID = scanner.nextLine();
        	
            System.out.println("Citations in the database for the given driver:");
            String citationQuery = "SELECT * FROM Citation INNER JOIN Vehicle ON Citation.LicenseNo = Vehicle.LicenseNo WHERE Vehicle.DriverID = ?";
            PreparedStatement citationStatement = conn.prepareStatement(citationQuery);
            citationStatement.setString(1, dID);
            
            ResultSet citationResult = citationStatement.executeQuery();
            resultSetService.viewFromResultSet(citationResult);

            System.out.println("Enter the Citation No to appeal:");
            final String CNo = scanner.nextLine();
            
         // Check if the payment status is pending
            String paymentStatusQuery = "SELECT PaymentStatus FROM Citation WHERE CitationNo = ?";
            PreparedStatement paymentStatusStatement = conn.prepareStatement(paymentStatusQuery);
            paymentStatusStatement.setString(1, CNo);
            ResultSet paymentStatusResult = paymentStatusStatement.executeQuery();

            if (paymentStatusResult.next()) {
                String paymentStatus = paymentStatusResult.getString("PaymentStatus");

                if (!"Pending".equalsIgnoreCase(paymentStatus)) {
                    System.out.println("This citation does not need to be appealed. Payment status is not pending.");
                    return false;
                }
            }

            final String sqlQuery = "INSERT INTO Appeals (DriverID, CitationNo, AppealStatus)  VALUES (? , ? , 'Requested');";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, dID);
            statement.setString(2, CNo);
            

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);
            System.out.println("This citation has been appealed.");

        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }
    
    
    
    private boolean payCitationFee(Connection conn) {
        try {
        	System.out.println("Enter the Driver ID:");
        	final String dID = scanner.nextLine();
        	
            System.out.println("Unpaid Citations in the database for the given driver:");
            String citationQuery = "SELECT * FROM Citation INNER JOIN Vehicle ON Citation.LicenseNo = Vehicle.LicenseNo WHERE Vehicle.DriverID = ? AND Citation.PaymentStatus = 'Pending'";
            PreparedStatement citationStatement = conn.prepareStatement(citationQuery);
            citationStatement.setString(1, dID);
            
            ResultSet citationResult = citationStatement.executeQuery();
            
            // Check if there are no unpaid citations
            if (!citationResult.next()) {
                System.out.println("No unpaid citations found.");
                return false;
            }

            // Display the unpaid citations
            citationResult.beforeFirst(); // Move the cursor back to the beginning
            resultSetService.viewFromResultSet(citationResult);
            
            System.out.println("Do you want to pay a citation? (yes/no)");
            String payCitationInput = scanner.nextLine().toLowerCase();
            
            if (payCitationInput.equals("yes")) {

            System.out.println("Enter the required Citation No:");
            final String CNo = scanner.nextLine();

            String CQuery = "SELECT * FROM Citation WHERE CitationNo = ?";
            PreparedStatement CStatement = conn.prepareStatement(CQuery);
            CStatement.setString(1, CNo);
            ResultSet CResult = CStatement.executeQuery();

            if (CResult.next()) {
                final String sqlQuery = "UPDATE Citation SET PaymentStatus = 'Complete' WHERE CitationNo= ? ;";
                PreparedStatement statement = conn.prepareStatement(sqlQuery);
                statement.setString(1, CNo);

                ResultSet resultSet = statement.executeQuery();
                resultSetService.viewFromResultSet(resultSet);
                
                // Delete the corresponding appeal
                final String deleteAppealQuery = "DELETE FROM Appeals WHERE CitationNo = ?";
                PreparedStatement deleteAppealStatement = conn.prepareStatement(deleteAppealQuery);
                deleteAppealStatement.setString(1, CNo);
                deleteAppealStatement.executeUpdate();

                System.out.println("Citation paid successfully, and appeal removed.");
                
            } else {
                System.out.println("Citation not found or already paid.");
                return false;
            }
          }
            else {
            	return false;
            }
            } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }
    
    private boolean updateCitation(Connection conn) { //while loop for updations and checking the payment status if pending
        try {
        	System.out.println("Citations in the database:");
            resultSetService.runQueryAndPrintOutput2(conn, "SELECT * from Citation;");
            
            System.out.println("Enter the CitationNo you want to update:");
            final String CNo = scanner.nextLine();
            
         // Check payment status before allowing update
            String paymentStatusQuery = "SELECT PaymentStatus FROM Citation WHERE CitationNo = ?";
            try (PreparedStatement paymentStatusStatement = conn.prepareStatement(paymentStatusQuery)) {
                paymentStatusStatement.setString(1, CNo);
                ResultSet paymentStatusResult = paymentStatusStatement.executeQuery();
                if (paymentStatusResult.next()) {
                    String paymentStatus = paymentStatusResult.getString("PaymentStatus");
                    if ("Complete".equalsIgnoreCase(paymentStatus)) {
                        System.out.println("Cannot update the field. Payment status is Complete.");
                        return false;
                    }
                }
            }
            
            System.out.println("Enter which field you want to update \n1.CitationDate \n2.CitationTime \n3.StaffID \n4.LicenseNo \n5.PLName \n6.CitationName");
            final String fieldName = scanner.nextLine();
            
            System.out.println("Enter the new value: ");
            final String newValue = scanner.nextLine();
            
            String sqlQuery = "";
            switch (fieldName) {
                case "1":
                    sqlQuery = "UPDATE Citation SET CitationDate = ? WHERE CitationNo = ?";
                    break;
                case "2":
                    sqlQuery = "UPDATE Citation SET CitationTime = ? WHERE CitationNo = ?";
                    break;
                case "3":
                    sqlQuery = "UPDATE Citation SET StaffID = ? WHERE CitationNo = ?";
                    break;
                case "4":
                    sqlQuery = "UPDATE Citation SET LicenseNo = ? WHERE CitationNo = ?";
                    break;
                case "5":
                    sqlQuery = "UPDATE Citation SET PLName = ? WHERE CitationNo = ?";
                    break;
                case "6":
                    sqlQuery = "UPDATE Citation SET CitationName = ? WHERE CitationNo = ?";
                    break;
                default:
                	System.out.println("Invalid Field. Please try again.");
                    break;
                	
            }
            
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, newValue);
            statement.setString(2, CNo);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);
            System.out.println("The value has been updated.");

        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    } 
    
    private boolean handleAppeal(Connection conn) {
        try {
            System.out.println("Appeals in the database:");
            resultSetService.runQueryAndPrintOutput2(conn, "SELECT * from Appeals;");

            System.out.println("Enter CitationNo for the appeal:");
            final String CNo = scanner.nextLine();
            
            System.out.println("Enter action (accept/reject):");
            String choice = scanner.next().toLowerCase();
       
            
            String sqlQuery = "";
            String sqlQuery1 = "";
            switch (choice) {
                case "accept":
                	sqlQuery = "UPDATE Appeals SET AppealStatus = 'Accepted' WHERE CitationNo = ?";
                    PreparedStatement statement = conn.prepareStatement(sqlQuery);
                    statement.setString(1, CNo);
                    ResultSet resultSet = statement.executeQuery();
                    resultSetService.viewFromResultSet(resultSet);
                    
                    sqlQuery = "UPDATE Citation SET PaymentStatus = 'Not Required' WHERE CitationNo = ?";
                    PreparedStatement statement1 = conn.prepareStatement(sqlQuery);
                    statement1.setString(1, CNo);
                    ResultSet resultSet1 = statement1.executeQuery();
                    resultSetService.viewFromResultSet(resultSet1);
                    break;
                case "reject":
                	sqlQuery = "UPDATE Appeals SET AppealStatus = 'Rejected' WHERE CitationNo = ?";
                	 PreparedStatement statement2 = conn.prepareStatement(sqlQuery);
                     statement2.setString(1, CNo);
                     ResultSet resultSet2 = statement2.executeQuery();
                     resultSetService.viewFromResultSet(resultSet2);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }


        } catch (Exception ex) {
            System.out.println("Exception: "+ ex.getMessage());
            return false;
        }
        return true;
    }
    
}
