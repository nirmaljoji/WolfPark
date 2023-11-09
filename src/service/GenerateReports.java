//getTotalCitationsPerLot()
//        return the total number of citations in each parking lot
//
//        getTotalCitations( startDate, endDate)
//        returns total number of citations given in all zones in each parking lot whose date is between startDate and endDate
//
//        getZones( )
//        returns list of zones for each lot as tuple pairs (plName, zoneID)
//
//        getCarViolations( )
//        returns the number of cars that are currently in violation
//
//        getEmployees( zoneID)
//        returns the number of employees having permits for a given parking zone
//
//        getPermitInformation( driverID )
//        returns permit information given the univID or phone number
//
//
//        getAvailableSpace( spaceType, plName )
//        returns an available space number along with zoneID given a space type in a given parking lot.

package service;

import java.sql.Connection;
import java.util.Scanner;

public class GenerateReports {
    Scanner scanner = new Scanner(System.in);
    ResultSetService resultSetService = new ResultSetService();


    public void run(Connection conn){

        try{

            while (true){
                System.out.println("\nGENERATE REPORTS:");
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
