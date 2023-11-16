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


    public void run(Connection conn) {

        try {
            while (true) {
                System.out.println("\nGENERATE AND MAINTAIN CITATIONS:");
                System.out.println("1. Check the permit validity for a vehicle");
                System.out.println("2. Generate a Citation");
                System.out.println("3. Update a Citation");
                System.out.println("4. Accept or reject an Appeal");
                System.out.println("5. Return to Main Menu\n");
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
                        updateCitation(conn);
                        break;
                    case 4:
                        handleAppeal(conn);
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid Input");
                }
                if (choice == 5) {
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());

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

            // Check if there is no permit
            String noPermitQuery = "SELECT * FROM Permit WHERE LicenseNo = ?";
            try (PreparedStatement noPermitStatement = conn.prepareStatement(noPermitQuery)) {
                noPermitStatement.setString(1, licenseNo);
                ResultSet noPermitResult = noPermitStatement.executeQuery();

                if (!noPermitResult.next()) {
                    System.out.println("No permit found for the given LicenseNo.");

                    // Prompt user to generate citation
                    System.out.println("Do you want to generate a citation? (yes/no)");
                    String generateCitationInput = scanner.nextLine().toLowerCase();

                    if (generateCitationInput.equals("yes")) {
                        generateCitation(conn, licenseNo, plName, "No Permit");
                    }

                    return false;
                }
            }

            // Check if the permit exists and matches input values
            String permitValidationQuery = "SELECT * FROM Permit " +
                    "INNER JOIN PermitLocation ON Permit.PermitID = PermitLocation.PermitID " +
                    "WHERE Permit.LicenseNo = ? " +
                    "AND PermitLocation.PLName = ? " +
                    "AND PermitLocation.ZoneID = ? " +
                    "AND PermitLocation.SpaceNo = ?";

            try (PreparedStatement permitValidationStatement = conn.prepareStatement(permitValidationQuery)) {
                permitValidationStatement.setString(1, licenseNo);
                permitValidationStatement.setString(2, plName);
                permitValidationStatement.setString(3, zoneID);
                permitValidationStatement.setString(4, spaceNo);

                ResultSet permitValidationResult = permitValidationStatement.executeQuery();

                if (!permitValidationResult.next()) {
                    System.out.println("Invalid permit. Vehicle is not parked correctly.");

                    // Prompt user to generate citation
                    System.out.println("Do you want to generate a citation? (yes/no)");
                    String generateCitationInput = scanner.nextLine().toLowerCase();

                    if (generateCitationInput.equals("yes")) {
                        generateCitation(conn, licenseNo, plName, "Invalid Permit");
                    }

                    return false;
                } else {
                    // Check if the permit is expired
                    Date currentDate = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String expirationDateStr = permitValidationResult.getString("ExpirationDate");

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

                    System.out.println("Permit is valid. Vehicle is parked correctly.");
                    return true;
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
            System.out.println("Exception: " + ex.getMessage());
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

            String date = LocalDate.now().toString();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime localTime = LocalTime.now();
            String time = dtf.format(localTime);
            final String sqlQuery = "INSERT INTO Citation (CitationNo, CitationDate, CitationTime, PaymentStatus, StaffID, LicenseNo, PLName, CitationName) VALUES (?, ?, ?, 'Pending', ?, ?, ?, ?);";

            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, CNo);
            statement.setString(2, date);
            statement.setString(3, time);
            statement.setString(5, LNo);
            statement.setString(6, PLName);
            statement.setInt(4, StaffID);
            statement.setString(7, CName);

            ResultSet resultSet = statement.executeQuery();
            resultSetService.viewFromResultSet(resultSet);
            System.out.println("Citation generated successfully.");

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean appealCitation(Connection conn) {
        try {
            System.out.println("Enter the Driver ID:");
            final String dID = scanner.nextLine();

            // Check if there are any citations for the given driver
            String countCitationsQuery = "SELECT COUNT(*) FROM Citation INNER JOIN Vehicle ON Citation.LicenseNo = Vehicle.LicenseNo WHERE Vehicle.DriverID = ?";
            try (PreparedStatement countCitationsStatement = conn.prepareStatement(countCitationsQuery)) {
                countCitationsStatement.setString(1, dID);
                try (ResultSet countCitationsResult = countCitationsStatement.executeQuery()) {
                    if (!countCitationsResult.next() || countCitationsResult.getInt(1) == 0) {
                        System.out.println("No citations found for the given driver.");
                        return false;
                    }
                }
            }

            System.out.println("Citations in the database for the given driver:");
            String citationQuery = "SELECT * FROM Citation INNER JOIN Vehicle ON Citation.LicenseNo = Vehicle.LicenseNo WHERE Vehicle.DriverID = ?";
            try (PreparedStatement citationStatement = conn.prepareStatement(citationQuery)) {
                citationStatement.setString(1, dID);

                ResultSet citationResult = citationStatement.executeQuery();
                resultSetService.viewFromResultSet(citationResult);

                System.out.println("Enter the Citation No to appeal:");
                final String CNo = scanner.nextLine();

                // Check if the payment status is pending
                String paymentStatusQuery = "SELECT PaymentStatus FROM Citation WHERE CitationNo = ?";
                try (PreparedStatement paymentStatusStatement = conn.prepareStatement(paymentStatusQuery)) {
                    paymentStatusStatement.setString(1, CNo);
                    try (ResultSet paymentStatusResult = paymentStatusStatement.executeQuery()) {
                        if (paymentStatusResult.next()) {
                            String paymentStatus = paymentStatusResult.getString("PaymentStatus");

                            if (!"Pending".equalsIgnoreCase(paymentStatus)) {
                                System.out.println("This citation does not need to be appealed. Payment status is not pending.");
                                return false;
                            }
                        }
                    }
                }

                final String sqlQuery = "INSERT INTO Appeals (DriverID, CitationNo, AppealStatus)  VALUES (? , ? , 'Requested');";
                try (PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
                    statement.setString(1, dID);
                    statement.setString(2, CNo);
                    statement.executeUpdate();
                }

                System.out.println("This citation has been appealed.");

            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean payCitationFee(Connection conn) {
        try {
            System.out.println("Enter the Driver ID:");
            final String dID = scanner.nextLine();

            System.out.println("Citations in the database for the given driver:");
            String citationQuery = "SELECT Citation.*, CitationCategory.Fees " +
                    "FROM Citation " +
                    "INNER JOIN CitationCategory ON Citation.CitationName = CitationCategory.CitationName " +
                    "WHERE Citation.LicenseNo IN (SELECT LicenseNo FROM Vehicle WHERE DriverID = ?)";
            PreparedStatement citationStatement = conn.prepareStatement(citationQuery);
            citationStatement.setString(1, dID);

            ResultSet citationResult = citationStatement.executeQuery();

            // Check if there are no unpaid citations
            if (!citationResult.next()) {
                System.out.println("No citations found.");
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

                // Check if the payment status is 'Pending'
                String paymentStatusQuery = "SELECT PaymentStatus FROM Citation WHERE CitationNo = ?";
                try (PreparedStatement paymentStatusStatement = conn.prepareStatement(paymentStatusQuery)) {
                    paymentStatusStatement.setString(1, CNo);
                    ResultSet paymentStatusResult = paymentStatusStatement.executeQuery();

                    if (paymentStatusResult.next()) {
                        String paymentStatus = paymentStatusResult.getString("PaymentStatus");

                        if ("Pending".equalsIgnoreCase(paymentStatus)) {
                            // Update the payment status to 'Complete'
                            final String sqlQuery = "UPDATE Citation SET PaymentStatus = 'Complete' WHERE CitationNo= ? ;";
                            try (PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
                                statement.setString(1, CNo);
                                statement.executeUpdate();

                                // Delete the corresponding appeal
                                final String deleteAppealQuery = "DELETE FROM Appeals WHERE CitationNo = ?";
                                try (PreparedStatement deleteAppealStatement = conn.prepareStatement(deleteAppealQuery)) {
                                    deleteAppealStatement.setString(1, CNo);
                                    deleteAppealStatement.executeUpdate();

                                    System.out.println("Citation paid successfully, and appeals, if any, have been removed.");
                                }
                            }
                        } else {
                            System.out.println("Citation has already been paid or doesn't require payment.");
                            return false;
                        }
                    } else {
                        System.out.println("Invalid CitationNo.");
                        return false;
                    }
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return false;
        }
        return true;
    }

    private boolean updateCitation(Connection conn) {
        try {
            System.out.println("Citations in the database:");
            resultSetService.runQueryAndPrintOutput2(conn, "SELECT * from Citation;");

            System.out.println("Enter the CitationNo you want to update:");
            final String CNo = scanner.nextLine();

            // Check if the CitationNo exists
            String citationExistenceQuery = "SELECT * FROM Citation WHERE CitationNo = ?";
            try (PreparedStatement citationExistenceStatement = conn.prepareStatement(citationExistenceQuery)) {
                citationExistenceStatement.setString(1, CNo);
                ResultSet citationExistenceResult = citationExistenceStatement.executeQuery();
                if (!citationExistenceResult.next()) {
                    System.out.println("Invalid CitationNo. Citation does not exist.");
                    return false;
                }
            }

            // Check payment status before allowing update
            String paymentStatusQuery = "SELECT PaymentStatus FROM Citation WHERE CitationNo = ?";
            try (PreparedStatement paymentStatusStatement = conn.prepareStatement(paymentStatusQuery)) {
                paymentStatusStatement.setString(1, CNo);
                ResultSet paymentStatusResult = paymentStatusStatement.executeQuery();
                if (paymentStatusResult.next()) {
                    String paymentStatus = paymentStatusResult.getString("PaymentStatus");
                    if (!("Pending".equalsIgnoreCase(paymentStatus))) {
                        System.out.println("Cannot update the field. Payment status is Complete/Not Required.");
                        return false;
                    }
                }
            }

            System.out.println("Enter which field you want to update \n1.CitationDate \n2.CitationTime \n3.StaffID \n4.LicenseNo \n5.PLName");
            final String fieldName = scanner.nextLine();

            System.out.println("Enter the new value: ");
            final String newValue = scanner.nextLine();

            String sqlQuery;
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
                default:
                    System.out.println("Invalid Field. Please try again.");
                    return false;
            }

            try (PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
                statement.setString(1, newValue);
                statement.setString(2, CNo);

                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("The value has been updated.");
                } else {
                    System.out.println("No rows were updated. Please check your input values.");
                }
            }

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return false;
        }
        return true;
    }

    private boolean handleAppeal(Connection conn) {
        try {
            // Check if there is data in the Appeals table
            String countAppealsQuery = "SELECT COUNT(*) FROM Appeals WHERE AppealStatus = 'Requested'";
            try (PreparedStatement countAppealsStatement = conn.prepareStatement(countAppealsQuery);
                 ResultSet countAppealsResult = countAppealsStatement.executeQuery()) {
                if (!countAppealsResult.next() || countAppealsResult.getInt(1) == 0) {
                    System.out.println("No appeals with status 'Requested' in the Appeals table.");
                    return false;
                }
            }

            System.out.println("Appeals with status 'Requested' in the database:");
            resultSetService.runQueryAndPrintOutput2(conn, "SELECT * FROM Appeals WHERE AppealStatus = 'Requested';");

            System.out.println("Enter CitationNo for the appeal:");
            final String CNo = scanner.nextLine();

            // Check if there is an appeal for the specified CitationNo
            String countAppealForCNoQuery = "SELECT COUNT(*) FROM Appeals WHERE CitationNo = ? AND AppealStatus = 'Requested'";
            try (PreparedStatement countAppealForCNoStatement = conn.prepareStatement(countAppealForCNoQuery)) {
                countAppealForCNoStatement.setString(1, CNo);
                try (ResultSet countAppealForCNoResult = countAppealForCNoStatement.executeQuery()) {
                    if (!countAppealForCNoResult.next() || countAppealForCNoResult.getInt(1) == 0) {
                        System.out.println("No appeal with status 'Requested' exists for the given CitationNo.");
                        return false;
                    }
                }
            }

            System.out.println("Enter action (accept/reject):");
            String choice = scanner.next().toLowerCase();

            String sqlQuery;

            switch (choice) {
                case "accept":
                    sqlQuery = "UPDATE Appeals SET AppealStatus = 'Accepted' WHERE CitationNo = ?";
                    try (PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
                        statement.setString(1, CNo);
                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("Appeal accepted successfully. CitationNo: " + CNo);
                        } else {
                            System.out.println("Failed to accept the appeal. Please try again.");
                        }
                    }

                    sqlQuery = "UPDATE Citation SET PaymentStatus = 'Not Required' WHERE CitationNo = ?";
                    try (PreparedStatement statement1 = conn.prepareStatement(sqlQuery)) {
                        statement1.setString(1, CNo);
                        statement1.executeUpdate();
                    }
                    break;
                case "reject":
                    sqlQuery = "UPDATE Appeals SET AppealStatus = 'Rejected' WHERE CitationNo = ?";
                    try (PreparedStatement statement2 = conn.prepareStatement(sqlQuery)) {
                        statement2.setString(1, CNo);
                        int rowsAffected = statement2.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("Appeal rejected successfully. CitationNo: " + CNo);
                        } else {
                            System.out.println("Failed to reject the appeal. Please try again.");
                        }
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return false;
            }

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return false;
        }
        return true;
    }
}


