//        createPermit (startDate, expirationDate, expirationTime, permitType, driverID, licenseNo, PLName, zoneID, SpaceNo, StaffID)
//        returns true if updated and false otherwise.
//        If NULL value for any of the fields, then they will not be updated
//        permitID is auto generated and hence not sent as parameter
//        AdminID is passed to keep track of who created the permit
//
//        updatePermit (permitID, startDate, expirationDate, expirationTime, permitType, driverID, licenseNo, PLName, zoneID, SpaceNo, StaffID)
//        If NULL value for any of the fields then those values will retain their current value.
//        True signifies a permit record was found and updated , false if no record was found to update.
//
//        deletePermit (startDate, expirationDate, expirationTime, permitType, driverID, licenseNo, PLName, zoneID, SpaceNo, StaffID)
//        returns true if permit is removed, and false otherwise
//
//        updateVehicleOwnership( licenseNo, DriverID )
//        returns true if updated, and false otherwise
//        If NULL value for any of the fields then those values will retain their current value.
//        Parameters: licenseNo denotes the new license to be updated for a driver
//        If a permit exists for an older license then it is deleted and the driver will need to apply for a new permit.
//
//        addVehicle(LicenseNo, Model, Color, Manufacturer, Year, DriverID)
//        returns true if added, and false otherwise
//        If NULL value for any of the fields, then it is not created.
//
//        removeVehicle(LicenseNo)
//        returns true if vehicle removed, and false otherwise
//        If NULL value for any of the fields then those values will retain their current value.
//        True signifies that vehicle is found and deleted, False if no record was found.
//        If a permit exists then the permit is deleted as well.

package service;

import service.helpers.ResultSetService;

import javax.sound.midi.SysexMessage;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Year;
import java.time.ZoneId;
import java.util.*;
import java.time.LocalDate;

public class VehiclePermit {

    Scanner scanner = new Scanner(System.in);
    ResultSetService resultSetService = new ResultSetService();


    public void run(Connection conn) {

        try {

            while (true) {
                System.out.println("\nMAINTAIN PERMIT AND VEHICLE INFORMATION:");
                System.out.println("1. Create new Permit");
                System.out.println("2. Update Permit");
                System.out.println("3. Delete Permit");
                System.out.println("4. Update Vehicle Ownership");
                System.out.println("5. Add Vehicle");
                System.out.println("6. Remove Vehicle");
                System.out.println("7. Return to Main Menu\n");
                System.out.println("Enter you choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        createPermit(conn);
                        break;
                    case 2:
                        updatePermit(conn);
                        break;
                    case 3:
                        deletePermit(conn);
                        break;
                    case 4:
                        updateVehicleOwnership(conn);
                        break;
                    case 5:
                        addVehicle(conn);
                        break;
                    case 6:
                        deleteVehicle(conn);
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("Invalid Input");
                        break;
                }
                if (choice == 7) {
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());

        }

    }

    private void deleteVehicle(Connection conn) {
        try {
            System.out.print("(DELETE) Enter License No of vehicle : ");
            final String licenseNo = scanner.nextLine();

            String deletePermit = "DELETE from Vehicle where LicenseNo = ?;";
            PreparedStatement stmt1 = conn.prepareStatement(deletePermit);
            stmt1.setString(1, licenseNo);
            int check = stmt1.executeUpdate();

            if(check == 1){
                System.out.println("Vehicle Deleted Successfully");
            }else{
                System.out.println("Please enter Valid Information");
            }
            stmt1.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void addVehicle(Connection conn) {
        try {
            System.out.println("Enter new vehicle information: ");
            //(LicenseNo, Model, Color, Manufacturer, Year, DriverID)
            System.out.println("License No: ");
            String licenseNo = scanner.nextLine();
            System.out.println("DriverID: ");
            String driverID = scanner.nextLine();
            System.out.println("Model : ");
            String model = scanner.nextLine();
            System.out.println("Color: ");
            String color = scanner.nextLine();
            System.out.println("Manufacturer: ");
            String manufacturer = scanner.nextLine();
            System.out.println("Year (YYYY-MM-DD): ");
            String year = scanner.nextLine();

            final String insertToVehicle = "INSERT INTO Vehicle (LicenseNo, DriverID, Model, Color, Year, VehicleCategory) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(insertToVehicle);
            statement.setString(1, licenseNo);
            statement.setString(2, driverID);
            statement.setString(3, model);
            statement.setString(4, color);
            statement.setString(5, manufacturer);
            statement.setString(6, year);
            statement.executeUpdate();
            statement.close();

            if (manufacturer.compareTo("") == 0) {
                final String insertToVehicleModel = "INSERT INTO VehicleModelManufacturer (Model, Manufacturer) VALUES (?, ?)";
                PreparedStatement statement1 = conn.prepareStatement(insertToVehicleModel);
                statement1.setString(1, model);
                statement1.setString(2, manufacturer);
                statement1.executeUpdate();
                statement1.close();
            }

            System.out.println("Added Vehicle Successfully");

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void updateVehicleOwnership(Connection conn) {


        try {
            System.out.println("\n Enter License No of Vehicle you wish to update:");
            String licenceNo_old = scanner.nextLine();

            final String sqlQuery = "Select * from Vehicle  V natural join VehicleModelManufacturer vm  where V.LicenseNo = ?;";
            System.out.println("\nVEHICLE  DETAILS:");
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, licenceNo_old);
            ResultSet resultSet1 = statement.executeQuery();
            if(resultSet1.isBeforeFirst() ){

                resultSetService.viewFromResultSet(resultSet1);
            }else{
                System.out.println("[ERROR] No Vehicle found for License");
                return;
            }


            System.out.println("\n Choose the information  you would like to update:");
            System.out.println("1. LicenseNo");
            System.out.println("2. DriverID");
            System.out.println("3. Model and Manufacturer");
            System.out.println("4. Color");
            System.out.println("5. Year");
            System.out.println("6. Vehicle Category");
            System.out.println("Enter you choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            String sql = null;
            PreparedStatement stmt = null;
                switch (choice) {
                    case 1:
                        System.out.print("Enter new License No: ");
                        String licenseNo = scanner.nextLine();
                        sql = "UPDATE Vehicle SET LicenseNo = ? WHERE LicenseNo = ?;";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1, licenseNo);
                        stmt.setString(2, licenceNo_old);
                        stmt.executeUpdate();
                        stmt.close();
                        System.out.println("Updated Successfully!");
                        break;

                    case 2:
                        System.out.print("Enter new Driver ID: ");
                        String driverID = scanner.nextLine();
                        sql = "UPDATE Vehicle SET DriverID = ? WHERE LicenseNo = ?;";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1, driverID);
                        stmt.setString(2, licenceNo_old);
                        stmt.executeUpdate();
                        stmt.close();
                        System.out.println("Updated Successfully!");
                        break;

                    case 3:
                        System.out.print("Enter new Model: ");
                        String model = scanner.nextLine();
                        System.out.print("Enter new Manufacturer: ");
                        String manufacturer = scanner.nextLine();
                        sql = "UPDATE VehicleModelManufacturer v INNER JOIN Vehicle vh ON vh.model = v.model SET v.Model = ?, v.Manufacturer = ? WHERE LicenseNo = ?;";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1, model);
                        stmt.setString(2, manufacturer);
                        stmt.setString(3, licenceNo_old);
                        stmt.executeUpdate();
                        stmt.close();
                        System.out.println("Updated Successfully!");
                        break;
                    case 4:
                        System.out.print("Enter new Color: ");
                        String color = scanner.nextLine();
                        sql = "UPDATE Vehicle SET Color = ? WHERE LicenseNo = ?;";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1, color);
                        stmt.setString(2, licenceNo_old);
                        stmt.executeUpdate();
                        stmt.close();
                        System.out.println("Updated Successfully!");
                        break;
                    case 5:
                        System.out.print("Enter new Year (YYYY-MM-DD): ");
                        String year = scanner.nextLine();
                        sql = "UPDATE Vehicle SET Year = year WHERE LicenseNo = ?;";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1, year);
                        stmt.setString(2, licenceNo_old);
                        stmt.executeUpdate();
                        stmt.close();
                        System.out.println("Updated Successfully!");
                        break;

                    case 6:
                        System.out.print("Enter new Vehicle Category: ");
                        String category = scanner.nextLine();

                        String vehicleCategory = "Select * FROM Permit WHERE LicenseNo= ?;";
                        stmt = conn.prepareStatement(vehicleCategory);
                        stmt.setString(1, licenceNo_old);
                        ResultSet resultSet = stmt.executeQuery();
                        if (resultSet.next()) {
                            System.out.println("[ERROR] Permit Exists!, Please delete Permit and then try again.");
                        } else {
                            sql = "UPDATE Vehicle SET VehicleCategory = ? WHERE LicenseNo = ?;";
                            stmt = conn.prepareStatement(sql);
                            stmt.setString(1, category);
                            stmt.setString(2, licenceNo_old);
                            stmt.executeUpdate();
                            stmt.close();
                            System.out.println("Updated Successfully!");
                        }
                        break;
                    case 7: return;
                     default: System.out.println("Please Enter a valid choice");
                     break;
                }



        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }

    private void deletePermit(Connection conn) {
        try {
            System.out.print("(DELETE) Enter permit ID : ");
            final String permitID = scanner.nextLine();


            String permitExists = "Select PLName, ZoneID, SpaceNo FROM PermitLocation WHERE PermitID = ?;";
            PreparedStatement stmt = conn.prepareStatement(permitExists);
            stmt.setString(1, permitID);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String PlName = resultSet.getString("PLName");
                String Zone = resultSet.getString("ZoneID");
                int Space = resultSet.getInt("SpaceNo");

                String deletePermit = "DELETE from Permit where PermitID = ?;";
                PreparedStatement stmt1 = conn.prepareStatement(deletePermit);
                stmt1.setString(1, permitID);
                stmt1.executeQuery();
                stmt1.close();

                final String updateAvailabilityStatus = "UPDATE ParkingLocation SET AvailabilityStatus = true WHERE PLName = ? AND ZoneID = ? AND SpaceNo = ? ;";
                PreparedStatement statement5 = conn.prepareStatement(updateAvailabilityStatus);
                statement5.setString(1, PlName);
                statement5.setString(2, Zone);
                statement5.setInt(3, Space);
                statement5.executeUpdate();
                statement5.close();
                System.out.println("Availability Status Updated");
                System.out.println("Permit Deleted");


            } else {
                System.out.println("Please enter valid information");
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void createPermit(Connection conn) {

        try {
            System.out.println("Enter information for (new) Permit:");
            // createPermit (startDate, expirationDate, expirationTime, permitType, driverID, licenseNo, PLName, zoneID, SpaceNo, StaffID)
            // createPermit ( expirationDate, expirationTime, )

            System.out.print("Enter permit ID: ");
            final String permitID = scanner.nextLine();

            System.out.print("Enter driver ID: ");
            final String driverID = scanner.nextLine();

            System.out.print("Enter License No: ");
            final String licenseNo = scanner.nextLine();

            System.out.print("Enter Parking Lot: ");
            final String parkingLot = scanner.nextLine();

            System.out.print("Enter Zone ID: ");
            final String zoneID = scanner.nextLine();

            System.out.print("Enter Space No (integer): ");
            final int spaceID = scanner.nextInt();
            scanner.nextLine();
            final int StaffID = 1;

            System.out.print("Enter the permit type: ");
            final String permitType = scanner.nextLine();


            System.out.print("Enter the expiration date (yyyy-mm-dd): ");
            final String expirationDate = scanner.nextLine();


            System.out.print("Enter the expiration time (HH:MM:SS): ");
            final String expirationTime = scanner.nextLine();

            String startDate = null;
            while (true) {

                System.out.println("Choose the following for Start date: \n");
                System.out.println("1.Use today's date");
                System.out.println("2.Enter custom date");
                final int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("Start Date: " + LocalDate.now());
                        startDate = LocalDate.now().toString();
                        break;
                    case 2:
                        System.out.println("Enter a Start date (yyyy-mm-dd): ");
                        startDate = scanner.nextLine();
                        break;
                    default:
                        System.out.println("Please enter a valid input");

                }
                if (choice == 1 || choice == 2) {
                    break;
                }

            }
            if (checkParkingEligibilityWithLicenseNo(conn, parkingLot, zoneID, spaceID, licenseNo) && checkZoneCompatabilityForNewPermit(zoneID, driverID, conn) && checkParkingLocationAvailability(conn, parkingLot, zoneID, spaceID) && checkParkingCount(conn, driverID, permitType)) {
                final String insertToPermit = "INSERT INTO Permit (PermitID, StaffID, LicenseNo, StartDate, ExpirationDate, ExpirationTime, PermitType) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = conn.prepareStatement(insertToPermit);
                statement.setString(1, permitID);
                statement.setInt(2, 1);
                statement.setString(3, licenseNo);
                statement.setString(4, startDate);
                statement.setString(5, expirationDate);
                statement.setString(6, expirationTime);
                statement.setString(7, permitType);
                statement.executeUpdate();
                statement.close();

//
//                final String insertToDriverVehiclePermit = "INSERT INTO DriverVehiclePermit (DriverID, LicenseNo) VALUES (?, ?)";
//                PreparedStatement statement1 = conn.prepareStatement(insertToDriverVehiclePermit);
//                statement1.setString(1,driverID);
//                statement1.setString(2, licenseNo);
//                statement1.executeUpdate();
//                statement1.close();

                final String insertToPermitLocation = "INSERT INTO PermitLocation (PermitID, PLName, ZoneID, SpaceNo) VALUES (?, ?, ?, ?)";
                PreparedStatement statement3 = conn.prepareStatement(insertToPermitLocation);
                statement3.setString(1, permitID);
                statement3.setString(2, parkingLot);
                statement3.setString(3, zoneID);
                statement3.setInt(4, spaceID);
                statement3.executeUpdate();
                statement3.close();

                final String updateAvailabilityStatus = "UPDATE ParkingLocation SET AvailabilityStatus = 0 WHERE PLName = ? AND ZoneID = ? AND SpaceNo = ? ;";
                PreparedStatement statement4 = conn.prepareStatement(updateAvailabilityStatus);
                statement4.setString(1, parkingLot);
                statement4.setString(2, zoneID);
                statement4.setInt(3, spaceID);
                statement4.executeUpdate();
                statement4.close();

                System.out.println("New permit Successfully created!");
            }


        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }


    }

    private boolean checkParkingCount(Connection conn, String driverID, String permitType) {
        try {
            String checkPermitCountSQL = " Select count(*) AS PermitCount, d.status FROM Vehicle v INNER JOIN Permit p on p.LicenseNo = v.LicenseNo INNER JOIN Driver d on d.driverID = v.driverID WHERE v.DriverID = ?";
            PreparedStatement checkPermitCount = conn.prepareStatement(checkPermitCountSQL);
            checkPermitCount.setString(1, driverID);
            ResultSet resultSet = checkPermitCount.executeQuery();
            ResultSet resultSet1 = checkPermitCount.executeQuery();
            resultSet1.next();
            if(resultSet1.getString("Status") == null){
                System.out.println("Permits within allowed limit .... ✓");
                return true;
            }
            if (resultSet.next()) {
                switch (resultSet.getString("Status")) {
                    case "E":
                        if (resultSet.getInt("PermitCount") < 2 ) {
                            System.out.println("Permits within allowed limit .... ✓");
                            return true;
                        } else {
                            if (resultSet.getInt("PermitCount") == 2 && (Objects.equals(permitType, "Special Event") || Objects.equals(permitType, "Park & Ride"))) {
                                System.out.println("Permits within allowed limit .... ✓");
                                return true;
                            }
                            System.out.println("Permits within allowed limit .... X");
                            return false;
                        }
                    case "V":
                        if (resultSet.getInt("PermitCount") == 1) {
                        System.out.println("Permits within allowed limit .... X");
                        return false;
                        }
                        break;
                    case "S":
                        if (resultSet.getInt("PermitCount") == 1) {
                            if (resultSet.getInt("PermitCount") == 1 && (Objects.equals(permitType, "Special Event") || Objects.equals(permitType, "Park & Ride"))) {
                                System.out.println("Permits within allowed limit .... ✓");
                                return true;
                            }
                            System.out.println("Permits within allowed limit .... X");
                            return false;
                        }
                        break;
                    default:
                        System.out.println("Please enter a valid input");
                        return false;
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return false;
    }

    private void updatePermit(Connection conn) {
        try {

            System.out.print("Enter permit ID to be updated: ");
            final String permitID = scanner.nextLine();

            final String sqlQuery = "Select * from PermitLocation natural join Permit where Permit.PermitID = ?;";

            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, permitID);
            ResultSet resultSet = statement.executeQuery();
            ResultSet resultSet1 = statement.executeQuery();
            if(resultSet1.next()){
                System.out.println("\nPERMIT DETAILS:");
                resultSetService.viewFromResultSet(resultSet);
            }else{
                System.out.println("Please Enter Valid Information");
                return ;
            }


            System.out.println("Select information to update:");
            System.out.println("1. Permit ID");
            System.out.println("2. Start Date");
            System.out.println("3. Expiration Date");
            System.out.println("4. Expiration Time");
            System.out.println("5. Permit Type");
            System.out.println("6. License No"); //CHECK
            System.out.println("7. Parking Location"); //CHECK


            int choice = scanner.nextInt();
            scanner.nextLine();
            String sql = null;
            PreparedStatement stmt = null;
            switch (choice) {
                case 1:
                    System.out.print("Enter new permit ID value: ");
                    String new_permitID = scanner.nextLine();
                    sql = "UPDATE Permit SET PermitID = ? WHERE PermitID = ?;";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, new_permitID);
                    stmt.setString(2, permitID);
                    int result = stmt.executeUpdate();
                    if(result == 1){
                        System.out.println("Permit updated successfully");
                    }else{
                        System.out.println("Please enter valid information");
                    }
                    stmt.close();
                    break;
                case 2:
                    System.out.print("Enter new Start Date (yyyy-mm-dd): ");
                    String startDate = scanner.nextLine();
                    sql = "UPDATE Permit SET StartDate = ? WHERE PermitID = ?;";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, startDate);
                    stmt.setString(2, permitID);
                    int result1 = stmt.executeUpdate();
                    if(result1 == 1){
                        System.out.println("Permit updated successfully");
                    }else{
                        System.out.println("Please enter valid information");
                    }
                    stmt.close();
                    break;
                case 3:
                    System.out.print("Enter new Expiration Date (yyyy-mm-dd): ");
                    String expirationDate = scanner.nextLine();
                    sql = "UPDATE Permit SET ExpirationDate = ? WHERE PermitID = ?;";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, expirationDate);
                    stmt.setString(2, permitID);
                    int result2 = stmt.executeUpdate();
                    if(result2 == 1){
                        System.out.println("Permit updated successfully");
                    }else{
                        System.out.println("Please enter valid information");
                    }
                    stmt.close();
                    break;
                case 4:
                    System.out.print("Enter new Expiration Time (HH:MM:SS): ");
                    String expirationTime = scanner.nextLine();
                    sql = "UPDATE Permit SET ExpirationTime = ? WHERE PermitID = ?;";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, expirationTime);
                    stmt.setString(2, permitID);
                    int result3 = stmt.executeUpdate();
                    if(result3 == 1){
                        System.out.println("Permit updated successfully");
                    }else{
                        System.out.println("Please enter valid information");
                    }
                    stmt.close();
                    break;
                case 5:
                    System.out.print("Enter new Permit Type:  ");
                    String permitType = scanner.nextLine();
                    sql = "UPDATE Permit SET PermitType = ? WHERE PermitID = ?;";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, permitType);
                    stmt.setString(2, permitID);
                    stmt.executeUpdate();

                    int result4 = stmt.executeUpdate();
                    if(result4 == 1){
                        System.out.println("Permit updated successfully");
                    }else{
                        System.out.println("Please enter valid information");
                    }
                    stmt.close();

                    break;
                case 6:
                    System.out.print("Enter new license No:  ");
                    String licenseNo = scanner.nextLine();
                    updateLicenseNo(licenseNo, permitID, conn);
                    break;
                case 7:
                    boolean value = false;
                    while (!value) {
                        System.out.print("Enter new Parking Lot: ");
                        String PLName = scanner.nextLine();
                        System.out.print("Enter new Zone: ");
                        String Zone = scanner.nextLine();
                        System.out.print("Enter new Space: ");
                        int Space = scanner.nextInt();
                        value = UpdateParkingLocation(conn, PLName, Zone, Space, permitID);
                    }
                    System.out.println("Permit updated succesfully");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid choice: " + choice);
            }

        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }

    }

    private void updateLicenseNo(String licenseNo, String permitID, Connection conn) {
        try {

            String vehicleCategory = "Select PLName, ZoneID, SpaceNo FROM PermitLocation WHERE PermitID = ?;";
            PreparedStatement stmt = conn.prepareStatement(vehicleCategory);
            stmt.setString(1, permitID);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String PlName = resultSet.getString("PLName");
                String Zone = resultSet.getString("ZoneID");
                int Space = resultSet.getInt("SpaceNo");
                if (checkParkingEligibilityWithLicenseNo(conn, PlName, Zone, Space, licenseNo)) {
                    String sql = "UPDATE Permit SET LicenseNo = ? WHERE PermitID = ?;";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, licenseNo);
                    stmt.setString(2, permitID);
                    stmt.executeUpdate();
                    stmt.close();
                    System.out.println("Permit Updated Successfully");
                } else {
                    System.out.println("Please enter valid information");
                }
            }else{
                System.out.println("Please enter valid information");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private boolean UpdateParkingLocation(Connection conn, String plName, String zone, int space, String permitID) {

        try {

            if (checkParkingLocationAvailability(conn, plName, zone, space)
                    && checkParkingEligibility(conn, plName, zone, space, permitID) && checkZoneCompatability(zone, permitID, conn)) {

                UpdateAvailabilityStatuses(conn, plName, zone, space, permitID);

                String updatePermitLocation = "UPDATE PermitLocation SET  PLName = ? , ZoneID = ? , SpaceNo = ? WHERE PermitID = ? ;";
                PreparedStatement update = conn.prepareStatement(updatePermitLocation);
                update.setString(1, plName);
                update.setString(2, zone);
                update.setInt(3, space);
                update.setString(4,permitID);
                update.executeUpdate();
                System.out.println("Permit Updated Successfully");
                return true;
            } else {
                System.out.println("Please enter valid information");
                return false;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

    private void UpdateAvailabilityStatuses(Connection conn, String plName, String zone, int space, String permitID) {

        try {

            String vehicleCategory = "Select PLName, ZoneID, SpaceNo FROM PermitLocation WHERE PermitID = ?;";
            PreparedStatement stmt = conn.prepareStatement(vehicleCategory);
            stmt.setString(1, permitID);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String PlName_old = resultSet.getString("PLName");
                String Zone_old = resultSet.getString("ZoneID");
                int Space_old = resultSet.getInt("SpaceNo");

                final String updateAvailabilityStatusOld = "UPDATE ParkingLocation SET AvailabilityStatus = true WHERE PLName = ? AND ZoneID = ? AND SpaceNo = ? ;";
                PreparedStatement statement4 = conn.prepareStatement(updateAvailabilityStatusOld);
                statement4.setString(1, PlName_old);
                statement4.setString(2, Zone_old);
                statement4.setInt(3, Space_old);
                statement4.executeUpdate();
                statement4.close();

                final String updateAvailabilityStatusNew = "UPDATE ParkingLocation SET AvailabilityStatus = false WHERE PLName = ? AND ZoneID = ? AND SpaceNo = ? ;";
                PreparedStatement statement5 = conn.prepareStatement(updateAvailabilityStatusNew);
                statement5.setString(1, plName);
                statement5.setString(2, zone);
                statement5.setInt(3, space);
                statement5.executeUpdate();
                statement5.close();
                System.out.println("Availability Statuses Updated");
            } else {
                System.out.println("Please enter valid information");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }


    public boolean checkParkingLocationAvailability(Connection conn, String plName, String zone, int space) {

        try {

            String parkingLoc = "Select * FROM ParkingLocation where PLName = ? AND ZoneID = ? AND SpaceNo = ?;";
            PreparedStatement getParkingLocation = conn.prepareStatement(parkingLoc);
            getParkingLocation.setString(1, plName);
            getParkingLocation.setString(2, zone);
            getParkingLocation.setInt(3, space);
            ResultSet resultSet = getParkingLocation.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getBoolean("AvailabilityStatus")) {
                    System.out.println("Checking Parking Availability .... ✓");
                    return true;
                } else {
                    System.out.println("Checking Parking Availability .... X");
                    return false;
                }
            } else {
                System.out.println("Please enter valid Parking Location");
                return false;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }


    public boolean checkParkingEligibilityWithLicenseNo(Connection conn, String plName, String zone, int space, String licenseNo) {
        try {
            String new_spaceType = null;
            String vehicleType = null;
            String spaceType = "Select SpaceType FROM Space where PLName = ? AND ZoneID = ? AND SpaceNo = ?;";
            PreparedStatement stmt = conn.prepareStatement(spaceType);
            stmt.setString(1, plName);
            stmt.setString(2, zone);
            stmt.setInt(3, space);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                new_spaceType = resultSet.getString("SpaceType");
//                System.out.println("Checking if you qualify for Space .... ✓");
            } else {
                System.out.println("Please enter valid information");
                return false;
            }

            String vehicleTypeSql = "Select v.VehicleCategory FROM Vehicle v  where v.LicenseNo = ?;";
            PreparedStatement stmt1 = conn.prepareStatement(vehicleTypeSql);
            stmt1.setString(1, licenseNo);
            ResultSet resultSet1 = stmt1.executeQuery();
            if (resultSet1.next()) {
                vehicleType = resultSet1.getString("VehicleCategory");
                if (vehicleType.compareTo(new_spaceType) == 0) {
                    System.out.println("Checking if you qualify for Space .... ✓");
                    return true;
                } else {
                    System.out.println("Checking if you qualify for Space .... X");
                    return false;
                }
            } else {
                System.out.println("Please enter valid information");
                return false;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean checkParkingEligibility(Connection conn, String plName, String zone, int space, String permitID) {
        try {
            String new_spaceType = null;
            String vehicleType = null;
            String spaceType = "Select SpaceType FROM Space where PLName = ? AND ZoneID = ? AND SpaceNo = ?;";
            PreparedStatement stmt = conn.prepareStatement(spaceType);
            stmt.setString(1, plName);
            stmt.setString(2, zone);
            stmt.setInt(3, space);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                new_spaceType = resultSet.getString("SpaceType");
//                System.out.println("Checking if you qualify for Space .... ✓");
            } else {
                System.out.println("Please enter valid information");
                return false;
            }

            String vehicleTypeSql = "Select v.VehicleCategory FROM Vehicle v INNER JOIN Permit p on p.LicenseNo = v.LicenseNo where  p.permitID = ?;";
            PreparedStatement stmt1 = conn.prepareStatement(vehicleTypeSql);
            stmt1.setString(1, permitID);
            ResultSet resultSet1 = stmt1.executeQuery();
            if (resultSet1.next()) {
                vehicleType = resultSet1.getString("VehicleCategory");
                if (vehicleType.compareTo(new_spaceType) == 0) {
                    System.out.println("Checking if you qualify for Space .... ✓");
                    return true;
                } else {
                    System.out.println("Checking if you qualify for Space .... X");
                    return false;
                }
            } else {
                System.out.println("Please enter valid information");
                return false;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean checkZoneCompatabilityForNewPermit(String Zone, String DriverID, Connection conn) {
        try {
            String driverStatusSql = "Select d.status FROM Driver d WHERE d.DriverID = ?";
            PreparedStatement stmt = conn.prepareStatement(driverStatusSql);
            stmt.setString(1, DriverID);
            ResultSet resultSet = stmt.executeQuery();
            String[] employees = new String[]{"A", "B", "C", "D"};
            String[] students = new String[]{"AS", "BS", "CS", "DS"};
            String[] visitors = new String[]{"V"};
            if (resultSet.next()) {
                String status = resultSet.getString("Status");
                switch (status) {
                    case "S":
                        if (!Arrays.asList(students).contains(Zone)) {
                            System.out.println("Checking if you qualify for Zone .... X");
                            return false;
                        }
                        break;
                    case "E":
                        if (!Arrays.asList(employees).contains(Zone)) {
                            System.out.println("Checking if you qualify for Zone .... X");
                            return false;
                        }
                        break;
                    case "V":
                        if (!Arrays.asList(visitors).contains(Zone)) {
                            System.out.println("Checking if you qualify for Zone .... X");
                            return false;
                        }
                        break;
                    default:
                        System.out.println("An error occurred");

                }
                System.out.println("Checking if you qualify for Zone .... ✓");
                return true;

            } else {
                System.out.println("Please enter valid Information");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean checkZoneCompatability(String Zone, String permitID, Connection conn) {
        try {
            String driverStatusSql = "Select d.status FROM Driver d INNER JOIN Vehicle v on v.DriverID = d.DriverID INNER JOIN Permit p ON p.LicenseNo = v.licenseNo WHERE p.PermitID=?;";
            PreparedStatement stmt = conn.prepareStatement(driverStatusSql);
            stmt.setString(1, permitID);
            ResultSet resultSet = stmt.executeQuery();
            String[] employees = new String[]{"A", " B", "C", "D"};
            String[] students = new String[]{"AS", "BS", "CS", "DS"};
            String[] visitors = new String[]{"V"};
            if (resultSet.next()) {
                String status = resultSet.getString("Status");
                switch (status) {
                    case "S":
                        if (!Arrays.asList(students).contains(Zone)) {
                            System.out.println("Checking if you qualify for Zone .... X");
                            return false;
                        }
                        break;
                    case "E":
                        if (!Arrays.asList(employees).contains(Zone)) {
                            System.out.println("Checking if you qualify for Zone .... X");
                            return false;
                        }
                        break;
                    case "V":
                        if (!Arrays.asList(visitors).contains(Zone)) {
                            System.out.println("Checking if you qualify for Zone .... X");
                            return false;
                        }
                        break;
                    default:
                        System.out.println("An error occurred");

                }
                System.out.println("Checking if you qualify for Zone .... ✓");
                return true;

            } else {
                System.out.println("Please enter valid Parking Location");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

}
