//
//createPermit (startDate, expirationDate, expirationTime, permitType, driverID, licenseNo, PLName, zoneID, SpaceNo, StaffID)
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

import java.sql.Connection;
import java.util.Scanner;

public class Citations {
    Scanner scanner = new Scanner(System.in);
    ResultSetService resultSetService = new ResultSetService();


    public void run(Connection conn){

        try{
            while (true){
                System.out.println("\nGENERATE AND MAINTAIN CITATIONS:");
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
