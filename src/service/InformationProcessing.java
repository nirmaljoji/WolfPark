package service;

import service.helpers.ResultSetService;

import java.sql.*;
import java.util.Scanner;

public class InformationProcessing {
    Scanner sc = new Scanner(System.in);
    ResultSetService resultSetService = new ResultSetService();

    /* Display list of operations for user to select */
    public void run(Connection conn) {

        VehiclePermit vp = new VehiclePermit();
        Citations c = new Citations();
        try {
            while (true) {
                System.out.println("\nINFORMATION PROCESSING:");
                System.out.println("1. Add new Driver");
                System.out.println("2. Update Driver");
                System.out.println("3. Delete Driver");
                System.out.println("4. Create Parking Lot");
                System.out.println("5. Update Parking Lot");
                System.out.println("6. Delete Parking Lot");
                System.out.println("7. Create Zone");
                System.out.println("8. Delete Zone");
                System.out.println("9. Create Space");
                System.out.println("10. Update Space");
                System.out.println("11. Delete Space");
                System.out.println("12. Create Permit");
                System.out.println("13. Update Permit");
                System.out.println("14. Delete Permit");
                System.out.println("15. Appeal Citation");
                System.out.println("16. Pay Citation");
                System.out.println("17. Return to Main Menu");


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
                        deleteZoneInformation(conn);
                        break;
                    case 9:
                        enterSpaceInformation(conn);
                        break;
                    case 10:
                        updateSpaceInformation(conn);
                        break;
                    case 11:
                        deleteSpaceInformation(conn);
                        break;
                    case 12:
                        vp.createPermit(conn);
                        break;
                    case 13:
                        vp.updatePermit(conn);
                        break;
                    case 14:
                        vp.deletePermit(conn);
                        break;
                    case 15:
                        c.appealCitation(conn);
                        break;
                    case 16:
                        c.payCitationFee(conn);
                        break;
                    case 17:
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
            System.out.println("Enter Driver ID : ");
            String olddriverId = sc.nextLine();
            System.out.println("Choose the Information you want to update");
            System.out.println("1. Driver ID");
            System.out.println("2. Driver name");
            String pick = sc.nextLine();
            switch (pick) {
                case ("1"): {
                    System.out.println("Enter new Driver ID: ");
                    String newdriverId = sc.nextLine();
                    String query = "UPDATE Driver SET DriverID = ? WHERE DriverID = ?;";
                    try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                        preparedStatement.setString(1, newdriverId);
                        preparedStatement.setString(2, olddriverId);
                        int result = preparedStatement.executeUpdate();
                        if(result == 1){
                            System.out.println("Driver ID is successfully updated");
                        }else{
                            System.out.println("Please enter valid information");
                        }
                    } catch (Exception ex) {
                        System.out.println("Exception:" + ex.getMessage());
                    }
                }
                break;
                case ("2"): {
                    System.out.println("Enter new Driver Name: ");
                    String newdrivername = sc.nextLine();
                    String query = "UPDATE Driver SET DriverName = ? WHERE DriverID = ?;";
                    try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                        preparedStatement.setString(1, newdrivername);
                        preparedStatement.setString(2, olddriverId);

                        int result = preparedStatement.executeUpdate();
                        if (result == 0) {
                            System.out.println("Please Enter Valid Information");
                        } else {
                            System.out.println(" Driver information updated successfully");
                        }


                    } catch (Exception ex) {
                        System.out.println("Exception:" + ex.getMessage());
                    }
                }
                break;
                default:
                    System.out.println("Enter a valid choice.");
                    break;
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
                System.out.println(result);
                if (result == 0) {
                    System.out.println("Please Enter Valid Information");
                } else {
                    System.out.println("Deleted  successfully");
                }
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
                System.out.println("Parking Lot Information Updated successfully.");

            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }

    }

    /* Method to update an entry in the Parking Lot table*/
    public void updateParkingLotInformation(Connection conn) { //cascade
        try {
            System.out.println("Enter the Parking Lot Information to be Updated: ");
            String parkingLotName = sc.nextLine();
            System.out.print("Choose the Information you want to update");
            System.out.println("1. Parking Lot Name: ");
            System.out.println("2. Address: ");
            String pick = sc.nextLine();

            switch (pick) {
                case ("1"): {
                    System.out.println("Enter new Parking Lot Name : ");
                    String newplName = sc.nextLine();
                    String query = "UPDATE ParkingLot SET  PLName = ? WHERE PLName = ?;";

                    try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                        preparedStatement.setString(1, newplName);
                        preparedStatement.setString(2, parkingLotName);
                        int result = preparedStatement.executeUpdate();
                        if(result==1){
                            System.out.println("Parking  is successfully update.");
                        }else{
                            System.out.println("Please enter valid information");
                        }

                    } catch (Exception ex) {
                        System.out.println("Exception:" + ex.getMessage());
                    }
                }
                break;
                case ("2"): {
                    System.out.println("Enter new Address : ");
                    String address = sc.nextLine();
                    String query = "UPDATE ParkingLot SET  Address = ? WHERE PLName = ?;";
                    try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                        preparedStatement.setString(1, address);
                        preparedStatement.setString(2, parkingLotName);

                        int result = preparedStatement.executeUpdate();
                        if (result == 0) {
                            System.out.println("Please Enter Valid Information");
                        } else {
                            System.out.println(" Driver information updated successfully");
                        }


                    } catch (Exception ex) {
                        System.out.println("Exception:" + ex.getMessage());
                    }
                }
                break;
                default:
                    System.out.println("Enter a valid choice.");
                    break;
            }

        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    /* Method to delete an entry from Parking Lot table*/
    public void deleteParkingLotInformation(Connection conn) { //cascade
        try {
            System.out.println("Enter the Parking Lot Information.");
            System.out.print("Enter the parking lot name: ");
            String parkingLotName = sc.nextLine().toUpperCase();

            conn.setAutoCommit(false);
            String query = "DELETE p FROM Permit p INNER JOIN PermitLocation on p.PermitID = PermitLocation.PermitID WHERE PermitLocation.PLName = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, parkingLotName);// Assuming parkingLotName is a String
                preparedStatement.executeUpdate();
                String query2 = "DELETE FROM ParkingLot WHERE PLName = ?";
                PreparedStatement stmt = conn.prepareStatement(query2);
                stmt.setString(1, parkingLotName);
                int result2 = stmt.executeUpdate();
                if (result2 == 1) {
                    conn.commit();
                    System.out.println("Parking Lot Deleted Successfully");
                } else {
                    System.out.println("Please Enter Valid Information");
                }
            } catch (Exception ex) {
                conn.rollback();
                System.out.println("Transaction rolled back: " + ex.getMessage());
            } finally {
                conn.setAutoCommit(true);
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
            System.out.print("Enter the Zone ID : ");
            String zoneId = sc.nextLine();
            String query = "INSERT INTO Zone (PLName, ZoneID) VALUES (?, ?);";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, parkingLotName);
                preparedStatement.setString(2, zoneId);
                int result = preparedStatement.executeUpdate();
                if (result >= 1) {
                    System.out.println("Zone information inserted successfully");
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }


    /* Method to delete an entry in the  Zone table*/
    public void deleteZoneInformation(Connection conn) {
        try {

            System.out.println("Enter the Parking Lot Information.");
            System.out.print("Enter the parking lot name: ");
            String parkingLotName = sc.nextLine();
            System.out.print("Enter the Zone ID: ");
            String zoneId = sc.nextLine();

            conn.setAutoCommit(false);
            String query = "DELETE p FROM Permit p INNER JOIN PermitLocation on p.PermitID = PermitLocation.PermitID WHERE PermitLocation.PLName = ? AND PermitLocation.ZoneID= ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, parkingLotName);// Assuming parkingLotName is a String
                preparedStatement.setString(2, zoneId);
                preparedStatement.executeUpdate();
                String query2 = "DELETE FROM Zone WHERE PLName = ? AND ZoneID = ?";
                PreparedStatement stmt = conn.prepareStatement(query2);
                stmt.setString(1, parkingLotName);
                stmt.setString(2, zoneId);
                int result2 = stmt.executeUpdate();
                if (result2 == 1) {
                    conn.commit();
                    System.out.println("Zone Deleted Successfully");
                } else {
                    System.out.println("Please Enter Valid Information");
                }
            }catch (Exception ex){
                conn.rollback();
                System.out.println("Transaction rolled back: "+ ex.getMessage());
            }finally {
                conn.setAutoCommit(true);
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
            System.out.println("Enter the space type (Regular, Handicap, Electric, Compact Car): ");
            String spaceType = sc.nextLine();
            String query = "INSERT INTO Space (PLName, ZoneID, SpaceNo, SpaceType) VALUES (?, ?, ?, ?);";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, parkingLotName);
                preparedStatement.setString(2, zoneId);
                preparedStatement.setString(3, spaceNumber);
                preparedStatement.setString(4, spaceType);

                int result = preparedStatement.executeUpdate();
                System.out.println("Space  created successfully");
                String query2 = "INSERT INTO ParkingLocation (PLName, ZoneID, SpaceNo, SpaceType, StaffID, Availability Status) VALUES  (?,?,?,?,?,?);";
                try (PreparedStatement preparedStatement1 = conn.prepareStatement(query2)) {
                    preparedStatement1.setString(1, parkingLotName);
                    preparedStatement1.setString(2, zoneId);
                    preparedStatement1.setString(3, spaceNumber);
                    preparedStatement1.setString(4, spaceType);
                    preparedStatement1.setString(5, "1");
                    int result_reflect = preparedStatement1.executeUpdate();
                }
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

            String selectQuery = "SELECT AvailabilityStatus from ParkingLocation WHERE PLName = ? AND ZoneID =? AND SpaceNo = ?;";
            try (PreparedStatement stmt1 = conn.prepareStatement(selectQuery)) {
                stmt1.setString(1, parkingLotName);
                stmt1.setString(2, zoneId);
                stmt1.setString(3, spaceNumber);
                ResultSet resultSet = stmt1.executeQuery();
                ResultSet resultSet2 = stmt1.executeQuery();
                if(!resultSet2.next()){
                    System.out.println("Please enter Valid information.");
                    return;
                }
                resultSet.next();
                if (resultSet.getBoolean("AvailabilityStatus")) {
                    System.out.println("Enter the new space type (Regular, Handicap, Electric, Compact Car): ");
                    String spaceType = sc.nextLine();
                    String query = "UPDATE Space SET SpaceType= ? WHERE PLName = ? AND ZoneID = ? AND SpaceNo = ?;";
                    try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                        preparedStatement.setString(1, spaceType);
                        preparedStatement.setString(2, parkingLotName);
                        preparedStatement.setString(3, zoneId);
                        preparedStatement.setString(4, spaceNumber);
                        preparedStatement.executeUpdate();
                        int result = preparedStatement.executeUpdate();
                        if (result == 0) {
                            System.out.println("Please Enter a Valid SpaceType.");
                        } else {
                            System.out.println("Space information Updated Successfully.");
                        }
                    } catch (Exception ex) {
                        System.out.println("Exception:" + ex.getMessage());
                    }
                } else {
                    System.out.println("You can not assign a space type for a space for which permit has already been assigned.");
                }
            }

        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    /* Method to delete an entry in the  Zone table*/
    public void deleteSpaceInformation(Connection conn) {
        try {
            System.out.println("Enter the Parking Lot Information.");
            System.out.print("Enter the parking lot name: ");
            String parkingLotName = sc.nextLine();
            System.out.print("Enter the Zone ID : ");
            String zoneId = sc.nextLine();
            System.out.print("Enter the space number : ");
            int spaceNumber = sc.nextInt();
            sc.nextLine();
            conn.setAutoCommit(false);
            String query = "DELETE p FROM Permit p INNER JOIN PermitLocation on p.PermitID = PermitLocation.PermitID WHERE PermitLocation.PLName = ? AND PermitLocation.ZoneID= ? AND PermitLocation.SpaceNo = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, parkingLotName);// Assuming parkingLotName is a String
                preparedStatement.setString(2, zoneId);
                preparedStatement.setInt(3, spaceNumber);
                preparedStatement.executeUpdate();
                String query2 = "DELETE FROM Space WHERE PLName = ? AND ZoneID = ? AND SpaceNo = ?";
                PreparedStatement stmt = conn.prepareStatement(query2);
                stmt.setString(1, parkingLotName);
                stmt.setString(2, zoneId);
                stmt.setInt(3, spaceNumber);
                int result2 = stmt.executeUpdate();
                if (result2 == 1) {
                    conn.commit();
                    System.out.println("Space Deleted Successfully");
                } else {
                    System.out.println("Please Enter Valid Information");
                }
            } catch (Exception ex) {
                conn.rollback();
                System.out.println("Transaction rolled back");
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());

        }
    }

}