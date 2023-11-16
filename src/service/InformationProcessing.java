package service;

import service.helpers.ResultSetService;

import java.sql.*;
import java.util.Scanner;

public class InformationProcessing {
    Scanner sc = new Scanner(System.in);
    ResultSetService resultSetService = new ResultSetService();

    /* Display list of operations for user to select */
    public void run(Connection conn) {
        try {
            while (true) {
                System.out.println("\nINFORMATION PROCESSING:");
                System.out.println("1. Enter Driver Information.");
                System.out.println("2. Update Driver Information.");
                System.out.println("3. Delete Driver Information.");
                System.out.println("4. Enter Parking Lot Information.");
                System.out.println("5. Update Parking Lot Information.");
                System.out.println("6. Delete Parking Lot Information.");
                System.out.println("7. Enter Zone Information.");
                System.out.println("8. Update Zone Information.");
                System.out.println("9. Delete Zone Information.");
                System.out.println("10. Enter Space Information.");
                System.out.println("11. Update Space Information.");
                System.out.println("12. Delete Space Information.");
                System.out.println("13. Enter Permit Information.");
                System.out.println("14. Update Permit Information.");
                System.out.println("15. Delete Permit Information.");
                System.out.println("16. Assign Zone.");
                System.out.println("17. Assign Space Type.");
                System.out.println("18. Appeal Citation.");
                System.out.println("19. Pay Citation.");
                System.out.println("20. Return to Main Menu. ");


                System.out.println("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        enterDriverInformation(conn);
                        break;
                    case 2:
                        updateDriverInformation(conn);
                        break;
                    case 3:
                        deleteDriverInformation(conn);
                        break;
                    case 4:
                        enterParkingLotInformation(conn);
                        break;
                    case 5:
                        updateParkingLotInformation(conn);
                        break;
                    case 6:
                        deleteParkingLotInformation(conn);
                        break;
                    case 7:
                        enterZoneInformation(conn);
                        break;
                    case 8:
                        updateZoneInformation(conn);
                        break;
                    case 9:
                        deleteZoneInformation(conn);
                        break;
                    case 10:
                        enterSpaceInformation(conn);
                        break;
                    case 11:
                        updateSpaceInformation(conn);
                        break;
                    case 12:
                        deleteSpaceInformation(conn);
                        break;
                    case 13:
                        //enterPermitInformation(conn);
                        break;
                    case 14:
                        //updatePermitInformation(conn);
                        break;
                    case 15:
                        deletePermitInformation(conn);
                        break;
                    case 16:
                        assignZone(conn);
                        break;
                    case 17:
                        assignSpaceType(conn);
                        break;
                    case 18:
                        //applyCitation(conn);
                        break;
                    case 19:
                        //payCitationFees(conn);
                        break;
                    case 20:
                        return;
                    default:
                        System.out.println("Invalid Input");
                }
            }

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());

        }

    }

    /* Method to read information of a new Driver entry*/
    public void enterDriverInformation(Connection conn) {

        try {
            System.out.println("Enter information for new driver.");
            System.out.print("Enter driver ID : ");
            String driverId = sc.nextLine();
            System.out.println("Enter the driver name : ");
            String driverName = sc.nextLine();

            System.out.println("Enter the Status of the driver (S,E,V) :");
            String status = sc.nextLine();

            String query = "INSERT INTO Driver (DriverID, DriverName, Status) VALUES (?, ?, ?);";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, driverId);
                preparedStatement.setString(2, driverName);
                preparedStatement.setString(3, status);

                preparedStatement.executeUpdate();
                System.out.println("New Driver created");
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    /* Method to Update Information of an entry in the Driver table*/
    public void updateDriverInformation(Connection conn) {
        try {
            System.out.println("Enter information for updating driver information.");
            System.out.print("1. Driver ID : ");
            String driverId = sc.nextLine();
            System.out.println("2.Driver name : ");
            String driverName = sc.nextLine();
            String query = "UPDATE Driver SET DriverName = ? WHERE DriverID = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, driverName);
                preparedStatement.setString(2, driverId);
                int result = preparedStatement.executeUpdate();
                System.out.println("Driver details are updated");
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());

        }
    }

    /* Method to delete an entry from the Driver table*/
    public void deleteDriverInformation(Connection conn) {
        try {
            System.out.println("Enter the Driver ID of the driver you want to delete.");
            System.out.print("Enter driver ID : ");
            String driverId = sc.nextLine();
            String query = "DELETE FROM Driver WHERE DriverID = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, driverId);
                int result = preparedStatement.executeUpdate();
                System.out.println("Driver information deleted successfully");
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    /* Method to add a new Parking Lot to the Parking Lot table*/
    public void enterParkingLotInformation(Connection conn) {
        try {
            System.out.println("Enter the Parking Lot Information.");
            System.out.print("Enter the parking lot name : ");
            String parkingLotName = sc.nextLine();
            String staffId = "1";
            System.out.println("Enter the address : ");
            String address = sc.nextLine();
            String query = "INSERT INTO ParkingLot (PLName, StaffID, Address) VALUES (?, ?, ?);";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, parkingLotName);
                preparedStatement.setString(2, staffId);
                preparedStatement.setString(3, address);

                int result = preparedStatement.executeUpdate();
                System.out.println("Parking Lot details entered successfully");
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }

    }

    /* Method to update an entry in the Parking Lot table*/
    public void updateParkingLotInformation(Connection conn) {
        try {
            System.out.println("Enter the Parking Lot Information.");
            System.out.print("Enter the parking lot name : ");
            String parkingLotName = sc.nextLine();
            System.out.println("Enter the address : ");
            String address = sc.nextLine();
            String query = "UPDATE ParkingLot SET Address = ? , PLName = ? WHERE PLName = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, address);
                preparedStatement.setString(2, parkingLotName);
                preparedStatement.setString(3, parkingLotName);
                int result = preparedStatement.executeUpdate();
                System.out.println("Parking Lot details updated successfully");
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }
    /* Method to delete an entry from Parking Lot table*/
    public void deleteParkingLotInformation(Connection conn) {
        try {
            System.out.println("Enter the Parking Lot Information.");
            System.out.print("Enter the parking lot name : ");
            String parkingLotName = sc.nextLine().toUpperCase();
            String query = "DELETE FROM ParkingLot WHERE PLName = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, parkingLotName); // Assuming parkingLotName is a String

                int result = preparedStatement.executeUpdate();
                System.out.println("Parking Lot details deleted successfully");
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    /* Method to add a new Zone to the Zone table*/
    public void enterZoneInformation(Connection conn) {
        try {
            System.out.println("Enter the Parking Lot Information.");
            System.out.print("Enter the parking lot name : ");
            String parkingLotName = sc.nextLine();
            System.out.println("Enter the Zone ID : ");
            String zoneId = sc.nextLine();
            String query = "INSERT INTO Zone (PLName, ZoneID) VALUES (?, ?);";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, parkingLotName);
                preparedStatement.setString(2, zoneId);

                int result = preparedStatement.executeUpdate();
                System.out.println("Parking Lot details updated with Zone successfully");
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    /* Method to update an entry in the  Zone table*/
    public void updateZoneInformation(Connection conn) {
        try {
            System.out.println("Enter the Parking Lot Information.");
            System.out.print("Enter the parking lot name : ");
            String parkingLotName = sc.nextLine();
            System.out.println("Enter the old Zone ID : ");
            String zoneId = sc.nextLine();
            System.out.println("Enter the new Zone ID : ");
            String newZoneId = sc.nextLine();
            String query = "UPDATE Zone SET ZoneID = ? WHERE PLName = ? AND ZoneID = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, newZoneId);
                preparedStatement.setString(2, parkingLotName);
                preparedStatement.setString(3, zoneId);

                int result = preparedStatement.executeUpdate();
                System.out.println("Zone details updated successfully");
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    /* Method to delete an entry in the  Zone table*/
    public void deleteZoneInformation(Connection conn) {
        try {

            System.out.print("Enter the Parking Lot Information.");
            System.out.print("Enter the parking lot name : ");
            String parkingLotName = sc.nextLine();
            System.out.println("Enter the Zone ID : ");
            String zoneId = sc.nextLine();
            String query = "DELETE FROM Zone WHERE PLName = ? AND ZoneID = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, parkingLotName); // Assuming parkingLotName is a String
                preparedStatement.setString(2, zoneId);        // Assuming zoneId is a String

                int result = preparedStatement.executeUpdate();
                System.out.println("Zone deleted successfully");
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    /* Method to add a space  in the Space table*/
    public void enterSpaceInformation(Connection conn) {
        try {
            System.out.println("Enter the Parking Lot Information.");
            System.out.println("Enter the parking lot name : ");
            String parkingLotName = sc.nextLine();
            System.out.println("Enter the Zone ID : ");
            String zoneId = sc.nextLine();
            System.out.println("Enter the space number : ");
            String spaceNumber = sc.nextLine();
            System.out.println("Enter the space type : ");
            String spaceType = sc.nextLine();
            String query = "INSERT INTO Space (PLName, ZoneID, SpaceNo, SpaceType) VALUES (?, ?, ?, ?);";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, parkingLotName); // Assuming parkingLotName is a String
                preparedStatement.setString(2, zoneId);        // Assuming zoneId is a String
                preparedStatement.setString(3, spaceNumber);          // Assuming spaceNo is an integer
                preparedStatement.setString(4, spaceType);     // Assuming spaceType is a String

                int result = preparedStatement.executeUpdate();
                System.out.println("Space  created successfully");
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    /* Method to update an entry in the  Space table*/
    public void updateSpaceInformation(Connection conn) {
        try {
            System.out.println("Enter the Parking Lot Information.");
            System.out.print("Enter the parking lot name : ");
            String parkingLotName = sc.nextLine();
            System.out.println("Enter the Zone ID : ");
            String zoneId = sc.nextLine();
            System.out.println("Enter the space number : ");
            String spaceNumber = sc.nextLine();
            System.out.println("Enter the space type : ");
            String spaceType = sc.nextLine();
            String query = "UPDATE Space SET SpaceType = ? WHERE PLName = ? AND ZoneID = ? AND SpaceNo = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, spaceType);     // Assuming spaceType is a String
                preparedStatement.setString(2, parkingLotName); // Assuming parkingLotName is a String
                preparedStatement.setString(3, zoneId);        // Assuming zoneId is a String
                preparedStatement.setString(4, spaceNumber);      // Assuming spaceNumber is an Integer

                int result = preparedStatement.executeUpdate();
                System.out.println("Space information updated successfully");
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    /* Method to delete an entry in the  Zone table*/
    public void deleteSpaceInformation(Connection conn) {
        try {
            System.out.print("Enter the Parking Lot Information.");
            System.out.print("Enter the parking lot name : ");
            String parkingLotName = sc.nextLine();
            System.out.println("Enter the Zone ID : ");
            String zoneId = sc.nextLine();
            System.out.print("Enter the space number : ");
            String spaceNumber = sc.nextLine();
            String query = "DELETE FROM Space WHERE PLName = ? AND ZoneID = ? AND SpaceNo = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, parkingLotName); // Assuming parkingLotName is a String
                preparedStatement.setString(2, zoneId);        // Assuming zoneId is a String
                preparedStatement.setString(3, spaceNumber);      // Assuming spaceNumber is an Integer

                int result = preparedStatement.executeUpdate();
                System.out.println("Space information deleted successfully");
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());

        }
    }

    /* Method to delete an entry in the  Permit table*/
    public void deletePermitInformation(Connection conn) {
        try {
            System.out.println("Enter the Parking Lot Information.");
            System.out.println("Enter the permit ID : ");
            int permitId = sc.nextInt();

            String query = "DELETE P, DVP, PL FROM Permit P " +
                    "JOIN DriverVehiclePermit DVP ON P.LicenseNo = DVP.LicenseNo " +
                    "JOIN PermitLocation PL ON P.PermitID = PL.PermitID " +
                    "WHERE P.PermitID = ?;";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, permitId); // Assuming permitId is an integer

                int result = preparedStatement.executeUpdate();
                System.out.println("Permit information deleted successfully");
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }
    /* Method to add a zone to a Parking Lot */
    public void assignZone(Connection conn) {
        try {
            System.out.println("Enter the Parking Lot Information.");
            System.out.println("Enter the parking lot name : ");
            String parkingLotName = sc.nextLine();
            System.out.println("Enter the zone ID : ");
            String zoneId = sc.nextLine();

            String query = "INSERT INTO Zone (PLName, ZoneID) VALUES (?, ?);";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, parkingLotName);
                preparedStatement.setString(2, zoneId);

                int result = preparedStatement.executeUpdate();
                System.out.println("Assigned Zone");
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }
    /* Method to add a space to a parking lot */
    public void assignSpaceType(Connection conn) {
        try {
            System.out.println("Enter the Parking Lot Information.");
            System.out.println("Enter the parking lot name : ");
            String parkingLotName = sc.nextLine();
            System.out.println("Enter the zone ID : ");
            String zoneId = sc.nextLine();
            System.out.println("Enter the space number : ");
            String spaceNumber = sc.nextLine();
            System.out.println("Enter the space type : ");
            String spaceType = sc.nextLine();


            String query = "INSERT INTO Space (PLName, ZoneID, SpaceNo, SpaceType) VALUES (?, ?, ?, ?);";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, parkingLotName);
                preparedStatement.setString(2, zoneId);
                preparedStatement.setString(3, spaceNumber);
                preparedStatement.setString(4, spaceType);

                int result = preparedStatement.executeUpdate();
                System.out.println("Assigned space type");
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }


    /*public void applyCitation(Connection conn){
        try{
		/* Method to track a drivers application for citation Parking Lot */
                /*System.out.println("Enter the Parking Lot Information.");
                System.out.println("Enter the driver ID : ");
                String driverId= sc.nextLine();
                System.out.println("Enter the citation Number : ");
                String citationNumber = sc.nextLine();
                System.out.println("Enter the appeal status : ");
                String appealStatus = sc.nextLine();
                String query = "INSERT INTO Appeals (DriverID, CitationNo, AppealStatus) "+"VALUES (" + driverId + ", " + citationNumber + ", " + appealStatus + ");";
         
                try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                        ResultSet resultSet = preparedStatement.executeQuery()) {
                    	while (resultSet.next()) {
                    		String dId = resultSet.getString("driverId");
                    		String cNumber= resultSet.getString("citationNumber");
                            String appealStat= resultSet.getString("appealStatus");
                    		System.out.println("Driver ID : " + dId + ", Citation Number : " + cNumber + ", Appeal Status : " +appealStat);
                        }

            }
            finally{
                System.out.println("Citation applied successfully");
            }
        }catch (Exception ex){
            System.out.println("Exception:" + ex.getMessage());
        }
    }
   public void payCitationFees(Connection conn){
	   /* Method to record payment of Citation fee by the Driver */
    //try{
    //   System.out.println("Enter the citation Number : ");
    //   int citationNumber = sc.nextInt();
    //    String query1 = "SELECT * FROM Citation where CitationNo = ' " + citationNumber + " ' ;";
    //           PreparedStatement statement = conn.prepareStatement(query1);
    //           ResultSet resultSet = statement.executeQuery();

    //		resultSetService.viewFromResultSet(resultSet);

    //		System.out.println("Do you want to proceed with paying your citation fees? /n 1.Yes /n 2.No /n ");
    //		int paywill = sc.nextInt();
    //         if (paywill == 1){
    //         	String paymentStatus = "Paid";
    //         	//         	String query = "UPDATE Citation SET PaymentStatus = '" + paymentStatus + "' WHERE CitationNo = '" + citationNumber + "'";
    //         	try (PreparedStatement preparedStatement = conn.prepareStatement(query);
    //                 ResultSet resultSet1 = preparedStatement.executeQuery()) {
    //              	while (resultSet1.next()) {
    //               		String citation_number = resultSet1.getString("citationNumber");
    //                		String payment_status = resultSet1.getString("paymentStatus");
    //               		//               		System.out.println("Citation Number : " + citation_number + ", Payment Status : " + payment_status );
    //               }
    //           }

    //         }
    //        else {
    //           
    //				resultSetService.viewFromResultSet(resultSet);
    //			System.out.println("Your Citation fee is still Unpaid.");
    //        	
    //        }

    //   }
    //  finally{
    //              System.out.println("Citation fees paid successfully");
    //          }
    //   }catch (Exception ex){
    //                System.out.println("Exception:" + ex.getMessage()); }

    //}
}

