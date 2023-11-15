package service;

import service.helpers.ResultSetService;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InformationProcessing {
    Scanner sc = new Scanner(System.in);
    ResultSetService resultSetService = new ResultSetService();
    
    public void run(Connection conn){

        try{
	    /* Display list of operations for user to select */
            while (true){
                System.out.println("\nINFORMATION PROCESSING:");
                System.out.println("1. Enter Driver Information. \n");
                System.out.println("2. Update Driver Information. \n");
                System.out.println("3. Delete Driver Information. \n");
                System.out.println("4. Enter Parking Lot Information. \n");
                System.out.println("5. Update Parking Lot Information. \n");
                System.out.println("6. Delete Parking Lot Information. \n");
                System.out.println("7. Enter Zone Information. \n");
                System.out.println("8. Update Zone Information. \n");
                System.out.println("9. Delete Zone Information. \n");
                System.out.println("10. Enter Space Information. \n");
                System.out.println("11. Update Space Information. \n");
                System.out.println("12. Delete Space Information. \n");
                System.out.println("13. Enter Permit Information. \n");
                System.out.println("14. Update Permit Information. \n");
                System.out.println("15. Delete Permit Information. \n");
                System.out.println("16. Assign Zone \n");
                System.out.println("17. Assign Space Type \n");
                System.out.println("18. Appeal Citation \n");
                System.out.println("19. Pay Citation \n");
		System.out.printlN("20. Exit to Main Menu "):
                
                
                System.out.println("Enter you choice: ");
                int choice = sc.nextInt();

                    
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

        }catch( Exception ex){
            System.out.println("Exception: "+ ex.getMessage());

        }

    }


    public void enterDriverInformation(Connection conn){
	    /* Method to read information of a new Driver entry*/
            try{
                System.out.println("Enter information for new driver.");
                System.out.print("Enter driver ID : ");
                String driverId =  sc.nextLine();
                System.out.println("Enter the driver name : ");
                String driverName = sc.nextLine();
                
                System.out.println("Enter the Status of the driver :");
                String status = sc.nextLine();
                
                String query = "INSERT INTO Driver (DriverID, DriverName, Status) VALUES (" + driverId + ", " + driverName + ", " + status + ");";

                try (PreparedStatement preparedStatement = conn.prepareStatement(query); 
                        ResultSet resultSet = preparedStatement.executeQuery()) {
                       while (resultSet.next()) {
                           int id = resultSet.getInt("driverId");
                           String name = resultSet.getString("driverName");
                           String status1 = resultSet.getString("Status");
                           System.out.println("Driver ID: " + id + ", Name: " + name + ", Status: " + status1);
                       }
            }
            finally{
                System.out.println("New Driver created");
            }
        }catch (Exception ex){
            System.out.println("Exception:" + ex.getMessage());
        }
    }

public void updateDriverInformation(Connection conn){
	/* Method to Update Information of an entry in the Driver table*/
        try{
                System.out.println("Enter information for updating driver information.");
                System.out.print("Enter driver ID : ");
                String driverId =  sc.nextLine();
                System.out.println("Enter the new driver name : ");
                String driverName = sc.nextLine();
                String query = "UPDATE Driver SET DriverName = " + driverName + " WHERE DriverID = " + driverId+ "');" ;

                try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                      ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                           int id = resultSet.getInt("driverId");
                           String name = resultSet.getString("driverName");
                           String status1 = resultSet.getString("Status");
                           System.out.println("Driver ID: " + id + ", Name: " + name + ", Status: " + status1);
                   }
                }
           
        }catch (Exception ex){
         System.out.println("Exception:" + ex.getMessage());
        }
         finally{
         System.out.println("Driver details updated");
         }
}


public void deleteDriverInformation(Connection conn){
	/* Method to delete an entry from the Driver table*/
            try{
                System.out.println("Enter the Driver ID of the driver you want to delete.");
                System.out.print("Enter driver ID : ");
                String driverId =  sc.nextLine();
                String query = "DELETE FROM Driver WHERE DriverID = " + driverId+ ";";

                try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                        ResultSet resultSet = preparedStatement.executeQuery()) {
                		System.out.println("Driver ID" + driverId + "is being deleted.");

           }
            finally{
                System.out.println("Driver information deleted succesfully");
            }
        }catch(Exception ex){
            System.out.println("Exception:" + ex.getMessage());
        }
}


    public void enterParkingLotInformation(Connection conn){
	    /* Method to add a new Parking Lot to the Parking Lot table*/
            try{
                System.out.println("Enter the Parking Lot Information.");
                System.out.print("Enter the parking lot name : ");
                String parkingLotName = sc.nextLine();
                System.out.println("Enter the staff ID : ");
                String staffId = sc.nextLine();
                System.out.println("Enter the address : ");
                String address = sc.nextLine();
                String query = "INSERT INTO ParkingLot (PLName, StaffID, Address) VALUES (" + parkingLotName + ", " + staffId + ", " + address + ");";

                try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                	while (resultSet.next()) {
                		String plName = resultSet.getString("parkingLotName");
                		String name = resultSet.getString("staffId");
                		String addr = resultSet.getString("address");
                		System.out.println("Parking Lot Name : " + plName + ", Staff ID : " + name + ", Address : " + addr);
           }
     }
            finally{
                System.out.println("Parking Lot details entered successfully");
            }
        }catch (Exception ex){
            System.out.println("Exception:" + ex.getMessage());
        }

    }

    public void updateParkingLotInformation(Connection conn){
	     /* Method to update an entry in the Parking Lot table*/
            try{
                System.out.println("Enter the Parking Lot Information.");
                System.out.print("Enter the parking lot name : ");
                String parkingLotName = sc.nextLine();
                System.out.println("Enter the address : ");
                String address = sc.nextLine();
                String query = "UPDATE ParkingLot SET Address = " + address + " WHERE PLName = " + parkingLotName + ";";
                try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                        ResultSet resultSet = preparedStatement.executeQuery()) {
                    	while (resultSet.next()) {
                    		String plName = resultSet.getString("parkingLotName");
                    		String name = resultSet.getString("staffId");
                    		String addr = resultSet.getString("address");
                    		System.out.println("Parking Lot Name : " + plName + ", Staff ID : " + name + ", Address : " + addr);
               }
           }
            finally{
                System.out.println("Parking Lot details updated successfully");
            }
        }catch (Exception ex){
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    public void deleteParkingLotInformation(Connection conn){
	     /* Method to delete an entry from Parking Lot table*/
            try{
                System.out.println("Enter the Parking Lot Information.");
                System.out.print("Enter the parking lot name : ");
                String parkingLotName = sc.nextLine().toUpperCase();
                String query = "DELETE FROM ParkingLot WHERE PLName = " + parkingLotName + ";";

                try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                        ResultSet resultSet = preparedStatement.executeQuery()) {
                		System.out.println("Parking Lot" + parkingLotName + "is being deleted.");
            }
            finally{
                System.out.println("Parking Lot details deleted successfully");
            }
        }catch (Exception ex){
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    public void enterZoneInformation(Connection conn){
            try{
		 /* Method to add a new Zone to the Zone table*/
                System.out.println("Enter the Parking Lot Information.");
                System.out.print("Enter the parking lot name : ");
                String parkingLotName = sc.nextLine();
                System.out.println("Enter the Zone ID : ");
                String zoneId = sc.nextLine();
                String query = "INSERT INTO Zone (PLName, ZoneID) VALUES (" + parkingLotName + ", " + zoneId + ";)";

                try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                        ResultSet resultSet = preparedStatement.executeQuery()) {
                       while (resultSet.next()) {
                           String id = resultSet.getString("zoneID");
                           String plName= resultSet.getString("parkingLotName");
                           System.out.println("Parking Lot Name : " + plName + ", Zone ID : " + id);
                       }
            }
            finally{
                System.out.println("Parking Lot details updated with Zone succesfully");
            }
        }catch (Exception ex){
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    public void updateZoneInformation(Connection conn){
        try{
		/* Method to update an entry in the  Zone table*/
                System.out.println("Enter the Parking Lot Information.");
                System.out.print("Enter the parking lot name : ");
                String parkingLotName = sc.nextLine();
                System.out.println("Enter the old Zone ID : ");
                String zoneId = sc.nextLine();
                System.out.println("Enter the new Zone ID : ");
                String newZoneId = sc.nextLine();
                String query = "UPDATE Zone SET ZoneID = " + newZoneId + " WHERE PLName = " + parkingLotName + " AND ZoneID = " + zoneId + ";";

                try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                        ResultSet resultSet = preparedStatement.executeQuery()) {
                       while (resultSet.next()) {
                           String id = resultSet.getString("zoneID");
                           String plName= resultSet.getString("parkingLotName");
                           System.out.println("Parking Lot Name : " + plName + ", Zone ID : " + id);
                       }
            }
            finally{
                System.out.println("Zone details updated succesfully");
            }
        }catch (Exception ex){
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    public void deleteZoneInformation(Connection conn){
            try{
		/* Method to delete an entry in the  Zone table*/
                System.out.println("Enter the Parking Lot Information.");
                System.out.print("Enter the parking lot name : ");
                String parkingLotName = sc.nextLine();
                System.out.println("Enter the Zone ID : ");
                String zoneId = sc.nextLine();
                String query = "DELETE FROM Zone WHERE PLName = " + parkingLotName + " AND ZoneID = " + zoneId + ";";

                
                try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                        ResultSet resultSet = preparedStatement.executeQuery()) {
                		System.out.println("Zone" + zoneId + "is being deleted.");
            }
            finally{
                System.out.println("Zone deleted succesfully");
            }
        }catch (Exception ex){
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    public void enterSpaceInformation(Connection conn){
            try{
		    /* Method to add a space  in the Space table*/
                System.out.println("Enter the Parking Lot Information.");
                System.out.print("Enter the parking lot name : ");
                String parkingLotName = sc.nextLine();
                System.out.println("Enter the Zone ID : ");
                String zoneId = sc.nextLine();
                System.out.println("Enter the space number : ");
                String spaceNumber = sc.nextLine();
                System.out.println("Enter the space type : ");
                String spaceType = sc.nextLine();
                String query = "INSERT INTO Space (PLName, ZoneID, SpaceNo, SpaceType) VALUES (" + parkingLotName + ", " + zoneId + ", " + spaceNumber + ", " + spaceType + ";)";

                try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                        ResultSet resultSet = preparedStatement.executeQuery()) {
                    	while (resultSet.next()) {
                    		String plName = resultSet.getString("parkingLotName");
                    		String spaceNo = resultSet.getString("spaceNumber");
                                String space_type = resultSet.getString("spaceType");
                    		System.out.println("Parking Lot Name : " + plName + ", space number : " + spaceNo + "space type: " + space_type );
               }
            }
            finally{
                System.out.println("Space information created succesfully");
            }
        }catch (Exception ex){
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    public void updateSpaceInformation(Connection conn){
            try{
		    /* Method to update an entry in the  Space table*/
                System.out.println("Enter the Parking Lot Information.");
                System.out.print("Enter the parking lot name : ");
                String parkingLotName = sc.nextLine();
                System.out.println("Enter the Zone ID : ");
                String zoneId = sc.nextLine();
                System.out.println("Enter the space number : ");
                String spaceNumber = sc.nextLine();
                System.out.println("Enter the space type : ");
                String spaceType = sc.nextLine();
                String query = "UPDATE Space SET SpaceType = " + spaceType + " WHERE PLName = " + parkingLotName + " AND ZoneID = " + zoneId + " AND SpaceNo = " + spaceNumber + ";";

                try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                        ResultSet resultSet = preparedStatement.executeQuery()) {
                    	while (resultSet.next()) {
                    		String plName = resultSet.getString("parkingLotName");
                    		String spaceNo = resultSet.getString("spaceNumber");
                            String space_type = resultSet.getString("spaceType");
                    		System.out.println("Parking Lot Name : " + plName + ", space number : " + spaceNo + "space type: " + space_type );
               }
            }
            finally{
                System.out.println("Space information updated succesfully");
            }
        }catch (Exception ex){
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    public void deleteSpaceInformation(Connection conn){
            try{
		    /* Method to delete an entry in the  Zone table*/
                System.out.println("Enter the Parking Lot Information.");
                System.out.print("Enter the parking lot name : ");
                String parkingLotName = sc.nextLine();
                System.out.println("Enter the Zone ID : ");
                String zoneId = sc.nextLine();
                System.out.println("Enter the space number : ");
                String spaceNumber = sc.nextLine();
                String query = "DELETE FROM Space WHERE PLName = " + parkingLotName + " AND ZoneID = " + zoneId + " AND SpaceNo = " + spaceNumber + ";";

                try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                        ResultSet resultSet = preparedStatement.executeQuery()) {
                		System.out.println("Space" + spaceNumber + "is being deleted.");
                }
                
            finally{
                System.out.println("Space information deleted succesfully");
            }
        }catch (Exception ex){
            System.out.println("Exception:" + ex.getMessage());
    
        }
  }


    public void deletePermitInformation(Connection conn){
        try{
		/* Method to delete an entry in the  Permit table*/
                System.out.println("Enter the Parking Lot Information.");
                System.out.println("Enter the permit ID : ");
                int permitId = sc.nextInt();
                
                String query = "DELETE P, DVP, PL FROM Permit P " + "JOIN DriverVehiclePermit DVP ON P.LicenseNo = DVP.LicenseNo " +"JOIN PermitLocation PL ON P.PermitID = PL.PermitID " +
                        "WHERE P.PermitID = " + permitId + ";";

                
                try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                        ResultSet resultSet = preparedStatement.executeQuery()) {
                		System.out.println("Permit Info" + permitId + "is being deleted.");
                }

            finally{
                System.out.println("Permit information deleted succesfully");
            }
        } 
            catch(Exception ex){
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    public void assignZone(Connection conn){
        try{
		/* Method to add a zone to a Parking Lot */
                System.out.println("Enter the Parking Lot Information.");
                System.out.println("Enter the parking lot name : ");
                String parkingLotName= sc.nextLine();
                System.out.println("Enter the zone ID : ");
                String zoneId = sc.nextLine();
                
                String query = "INSERT INTO Zone (PLName, ZoneID) VALUES (" + parkingLotName + ", " + zoneId + ");";

                try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                        ResultSet resultSet = preparedStatement.executeQuery()) {
                    	while (resultSet.next()) {
                    		String plName = resultSet.getString("parkingLotName");
                    		String zone_id = resultSet.getString("zoneId");
                    		System.out.println("Parking Lot Name : " + plName + ", zone ID : " + zone_id);
               }

            }
            finally{
                System.out.println("Assigned Zone");
            }
        }catch (Exception ex){
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    public void assignSpaceType(Connection conn){
        try{
	
		/* Method to add a space to a parking lot */
                System.out.println("Enter the Parking Lot Information.");
                System.out.println("Enter the parking lot name : ");
                String parkingLotName= sc.nextLine();
                System.out.println("Enter the zone ID : ");
                String zoneId = sc.nextLine();
                System.out.println("Enter the space number : ");
                int spaceNumber = sc.nextInt();
                System.out.println("Enter the space type : ");
                String spaceType = sc.nextLine();
                
                String query = "INSERT INTO Space (PLName, ZoneID, SpaceNo, SpaceType) " + "VALUES ('" + parkingLotName + "', " + zoneId + ", " + spaceNumber + ", '" + spaceType + "');";

                try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                        ResultSet resultSet = preparedStatement.executeQuery()) {
                    	while (resultSet.next()) {
                    		String plName = resultSet.getString("parkingLotName");
                    		String zone_id = resultSet.getString("zoneId");
                            String spaceNo = resultSet.getString("spaceNumber");
                            String space_type = resultSet.getString("spaceType");
                    		System.out.println("Parking Lot Name : " + plName + ", zone ID : " + zone_id + ",Space Number: " + spaceNo + ", Space Type: " + space_type);
               }

            }
            finally{
                System.out.println("Assigned space type");
            }
        }catch (Exception ex){
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    public void applyCitation(Connection conn){
        try{
		/* Method to add a zone to a Parking Lot */
                System.out.println("Enter the Parking Lot Information.");
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
	   return ;}
   }
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

