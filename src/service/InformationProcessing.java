//        enterDriverInfo( driverID, driverName, status )
//        return confirmation
//
//        updateDriverInfo( driverID, driverName, status )
//        return confirmation
//        If NULL value for any of the fields, then they will not be updated
//
//        deleteDriverInfo( driverID)
//        return confirmation
//        * If NULL value for any of the fields, then they will not be updated
//
//        enterParkingLotInfo( plName, address)
//        return confirmation
//
//        updateParkingLotInfo( plName, address)
//        return confirmation
//        * If NULL value for any of the fields, then they will not be updated
//
//        deleteParkingLotInfo( plName )
//        return confirmation
//        *If NULL value for any of the fields, then they will not be updated
//
//        enterZoneInfo( plName, zoneID )
//        return confirmation
//
//        updateZoneInfo( plName, zoneID )
//        return confirmation
//        *If NULL value for any of the fields, then they will not be updated
//
//        deleteZoneInfo( plName, zoneID )
//        return confirmation
//        *If NULL value for any of the fields, then they will not be deleted
//
//        enterSpaceInfo( plName, zoneID, spaceNo, spaceType, availabilityStatus ) return confirmation
//
//        updateSpaceInfo( plName, zoneID, spaceNo, spaceType, availabilityStatus)
//        return confirmation
//        *If NULL value for any of the fields, then they will not be updated
//
//        deleteSpaceInfo( plName, zoneID, spaceNo, spaceType)
//        return confirmation
//        *If NULL value for any of the fields, then they will not be deleted
//
//        enterPermitInfo( startDate, expirationDate, expirationTime, permitType, driverID, licenseNo, plName, zoneID, spaceNo, staffID )
//        return confirmation
//
//
//        updatePermitInfo( startDate, expirationDate, expirationTime, permitType, driverID, licenseNo, PLName, zoneID, spaceNo, staffID )
//        return confirmation
//        *If NULL value for any of the fields, then they will not be updated
//
//        deletePermitInfo( startDate, expirationDate, expirationTime, permitType, driverID, licenseNo, plName, zoneID, spaceNo, staffID )
//        return confirmation
//        *If NULL value for any of the fields, then they will not be deleted
//
//        assignZone (plName, zoneID)
//        return confirmation
//
//        assignSpaceType (plName, spaceNo, spaceType)
//        return confirmation
//
//
//        appealCitation(citationNumber)
//        returns True if appeal has been raised, else False.
//
//
//        payCitationFee(citationNumber)
//        returns True if payment status has been successfully updated to  paid, else False.

package service;

import service.helpers.ResultSetService;

import java.sql.Connection;
import java.util.Scanner;

public class InformationProcessing {

    Scanner scanner = new Scanner(System.in);
    ResultSetService resultSetService = new ResultSetService();


    public void run(Connection conn){

        try{

            while (true){
                System.out.println("\nINFORMATION PROCESSING:");
                System.out.println("1. Edit here");
                System.out.println("2. Return to Main Menu\n");
                System.out.println("Enter you choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("Do you opertain here (dont write all the code in the block, make use of function calls, use resultset to help format output");
                        break;
                    case 2:
                        return;
                    default:
                        System.out.println("Invalid Input");
                }
                if(choice == 2) {
                    break;
                }
            }

        }catch( Exception ex){
            System.out.println("Exception: "+ ex.getMessage());

        }

    }
}
