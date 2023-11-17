package service.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PrepareTable {
    // Method to create all necessary tables in the database.
    public static void createTable(Connection conn){
        try {

            Statement stmt = null;
            try {

                stmt = conn.createStatement();
                // Disabling foreign key checks to safely drop tables if they exist.
                stmt.addBatch("SET FOREIGN_KEY_CHECKS=0;");
                // Dropping existing tables to ensure fresh setup.
                stmt.addBatch("DROP TABLE IF EXISTS Staff, Driver, VehicleModelManufacturer, Vehicle, Permit, DriverVehiclePermit, ParkingLot, Zone, Space, PermitLocation, ParkingLocation, CitationCategory, Citation, Appeals;");
                // Re-enabling foreign key checks.
                stmt.addBatch("SET FOREIGN_KEY_CHECKS=1;");

                // Creating tables with appropriate constraints and foreign key relationships.
                stmt.addBatch(" CREATE TABLE Staff( StaffID INT PRIMARY KEY, Role VARCHAR(32) NOT NULL, CHECK (Role in ('Admin', 'Security')) );");

                stmt.addBatch(" CREATE TABLE Driver ( DriverID VARCHAR(10) PRIMARY KEY, DriverName VARCHAR(255) NOT NULL, Status VARCHAR(1) NOT NULL CHECK (Status IN ('S', 'E', 'V')),CHECK ((Status IN ('S', 'E')) OR (Status = 'V' AND CHAR_LENGTH(DriverID) = 10)));");

                stmt.addBatch(" CREATE TABLE VehicleModelManufacturer ( Model VARCHAR(255) PRIMARY KEY, Manufacturer VARCHAR(255) );");

                stmt.addBatch(" CREATE TABLE Vehicle ( LicenseNo VARCHAR(255) PRIMARY KEY, DriverID VARCHAR(10) NOT NULL, Model VARCHAR(255) NOT NULL, Color VARCHAR(255), Year YEAR, VehicleCategory VARCHAR(32) NOT NULL, FOREIGN KEY (DriverID) REFERENCES Driver(DriverID) ON DELETE CASCADE ON UPDATE CASCADE , FOREIGN KEY (Model) REFERENCES VehicleModelManufacturer(Model) ON UPDATE CASCADE, CHECK (VehicleCategory in ('Electric', 'Handicap', 'Compact car', 'Regular')) );");

                stmt.addBatch(" CREATE TABLE Permit ( PermitID VARCHAR(10) PRIMARY KEY, StaffID INT NOT NULL, LicenseNo VARCHAR(255) NOT NULL, StartDate DATE NOT NULL, ExpirationDate DATE NOT NULL, ExpirationTime TIME NOT NULL, PermitType VARCHAR(32) NOT NULL, FOREIGN KEY (StaffID) REFERENCES Staff(StaffID), FOREIGN KEY (LicenseNo) REFERENCES Vehicle(LicenseNo) ON DELETE CASCADE ON UPDATE CASCADE, CHECK (PermitType in ('Residential', 'Commuter', 'Peak Hours', 'Special Event', 'Park & Ride')) );");

                stmt.addBatch(" CREATE TABLE ParkingLot ( PLName VARCHAR(255) PRIMARY KEY, StaffID INT NOT NULL, Address VARCHAR(255) UNIQUE NOT NULL, FOREIGN KEY (StaffID) REFERENCES Staff(StaffID) );");

                stmt.addBatch(" CREATE TABLE Zone ( PLName VARCHAR(255) NOT NULL, ZoneID VARCHAR(2) NOT NULL, PRIMARY KEY (PLName, ZoneID), FOREIGN KEY (PLName) REFERENCES ParkingLot(PLName) ON DELETE CASCADE ON UPDATE CASCADE, CHECK (ZoneID in ('A', 'B', 'C', 'D', 'AS', 'BS', 'CS', 'DS', 'V')) );");

                stmt.addBatch(" CREATE TABLE Space ( PLName VARCHAR(255) NOT NULL, ZoneID VARCHAR(2) NOT NULL, SpaceNo INT NOT NULL, SpaceType VARCHAR(255) NOT NULL, PRIMARY KEY (PLName, ZoneID, SpaceNo), FOREIGN KEY (PLName, ZoneID) REFERENCES Zone(PLName, ZoneID) ON DELETE CASCADE ON UPDATE CASCADE, CHECK (SpaceType in ('Electric', 'Handicap', 'Compact car', 'Regular')) );");

                stmt.addBatch( " CREATE TABLE PermitLocation ( PermitID VARCHAR(10) PRIMARY KEY, PLName VARCHAR(255) NOT NULL, ZoneID VARCHAR(2) NOT NULL, SpaceNo INT NOT NULL, FOREIGN KEY (PermitID) REFERENCES Permit(PermitID) ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY (PLName, ZoneID, SpaceNo) REFERENCES Space(PLName, ZoneID, SpaceNo) ON DELETE CASCADE ON UPDATE CASCADE );");

                stmt.addBatch( " CREATE TABLE ParkingLocation ( PLName VARCHAR(255) NOT NULL, ZoneID VARCHAR(2), SpaceNo INT NOT NULL, StaffID INT NOT NULL, AvailabilityStatus BOOLEAN NOT NULL, PRIMARY KEY (PLName, ZoneID, SpaceNo), FOREIGN KEY (PLName, ZoneID, SpaceNo) REFERENCES Space(PLName, ZoneID, SpaceNo) ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY (StaffID) REFERENCES Staff(StaffID) );");

                stmt.addBatch( " CREATE TABLE CitationCategory ( CitationName VARCHAR(32) PRIMARY KEY, Fees DOUBLE NOT NULL, CHECK (CitationName in ('Invalid Permit', 'Expired Permit', 'No Permit', 'Handicap Invalid Permit', 'Handicap Expired Permit', 'Handicap No Permit')) );");

                stmt.addBatch( " CREATE TABLE Citation ( CitationNo VARCHAR(10) PRIMARY KEY, CitationDate DATE NOT NULL, CitationTime TIME NOT NULL, PaymentStatus ENUM ('Pending', 'Complete', 'Not Required'), StaffID INT NOT NULL, LicenseNo VARCHAR(255) NOT NULL, PLName VARCHAR(255) NOT NULL, CitationName VARCHAR(32), FOREIGN KEY (CitationName) REFERENCES CitationCategory(CitationName), FOREIGN KEY (StaffID) REFERENCES Staff(StaffID), FOREIGN KEY (LicenseNo) REFERENCES Vehicle(LicenseNo) ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY (PLName) REFERENCES ParkingLot(PLName) ON DELETE CASCADE ON UPDATE CASCADE, CHECK (CitationName in ('Invalid Permit', 'Expired Permit', 'No Permit', 'Handicap Invalid Permit', 'Handicap Expired Permit', 'Handicap No Permit')));");

                stmt.addBatch( " CREATE TABLE Appeals ( DriverID VARCHAR(10) NOT NULL, CitationNo VARCHAR(10) NOT NULL, AppealStatus VARCHAR(32) NOT NULL, FOREIGN KEY (DriverID) REFERENCES Driver(DriverID) ON DELETE CASCADE ON UPDATE CASCADE , FOREIGN KEY (CitationNo) REFERENCES Citation(CitationNo) ON DELETE CASCADE ON UPDATE CASCADE, CHECK (AppealStatus in ('Requested', 'Rejected', 'Accepted')) );");

                stmt.executeBatch();
                System.out.print("All required tables created.....");

            } finally {
                System.out.print("All required tables created.....");
            }
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    }

    // Method to insert initial data into tables.
    public static void insertData(Connection conn){
        try {

            Statement stmt = null;

            try {

                stmt = conn.createStatement();

                // Adding INSERT statements to the batch for initial data population using given demo data
                stmt.addBatch("INSERT INTO Staff (StaffID, Role) VALUES (1, 'Admin'), (2, 'Security');");
                stmt.addBatch("INSERT INTO Driver (DriverID, DriverName, Status) VALUES ('7729119111', 'Sam BankmanFried', 'V'), ('266399121', 'John Clay', 'E'), ('366399121','Julia Hicks', 'E'), ('466399121', 'Ivan Garcia', 'E'), ('122765234', 'Sachin Tendulkar','S'), ('9194789124', 'Charles Xavier', 'V'), ('9999999999', 'Joji', 'V');");
                stmt.addBatch("INSERT INTO ParkingLot (PLName, StaffID, Address) VALUES ('Poulton Deck', 1, '1021 Main Campus Dr Raleigh, NC, 27606'), ('Partners Way Deck', 1, '851 Partners Way Raleigh, NC, 27606'), ('Dan Allen Parking Deck', 1, '110 Dan Allen Dr Raleigh, NC, 27607');");
                stmt.addBatch("INSERT INTO VehicleModelManufacturer (Model, Manufacturer) VALUES ('GT-R-Nismo', 'Nissan'), ('Model S', 'Tesla'), ('M2 Coupe', 'BMW'), ('Continental GT Speed', 'Bentley'), ('Civic SI', 'Honda'), ('Taycan Sport Turismo', 'Porsche'), ('Macan GTS', null);");
                stmt.addBatch("INSERT INTO Vehicle (LicenseNo, DriverID, Model, Color, Year, VehicleCategory) VALUES ('SBF', '7729119111', 'GT-R-Nismo', 'Pearl White TriCoat', 2024, 'Regular'), ('Clay1', '266399121', 'Model S', 'Ultra Red', 2023, 'Electric'), ('Hicks1', '366399121', 'M2 Coupe', 'Zandvoort Blue', 2024, 'Regular'), ('Garcia1', '466399121', 'Continental GT Speed', 'Blue Fusion', 2024, 'Regular'), ('CRICKET', '122765234', 'Civic SI', 'Sonic Gray Pearl', 2024, 'Compact Car'), ('PROFX', '9194789124', 'Taycan Sport Turismo', 'Frozenblue Metallic', 2024, 'Handicap'),('VAN-9910', '9999999999', 'Macan GTS', 'Papaya Metallic', null, 'Regular');");
                stmt.addBatch("INSERT INTO Permit (PermitID, StaffID, LicenseNo, StartDate, ExpirationDate, ExpirationTime, PermitType) VALUES ('VSBF1C', '1', 'SBF', '2023-01-01', '2024-01-01', '06:00:00', 'Commuter'), ('EJC1R', '1', 'Clay1', '2010-01-01', '2030-01-01', '06:00:00', 'Residential'), ('EJH2C', '1', 'Hicks1', '2023-01-01', '2024-01-01', '06:00:00', 'Commuter'), ('EIG3C', '1', 'Garcia1', '2023-01-01', '2024-01-01', '06:00:00', 'Commuter'), ('SST1R', '1', 'CRICKET', '2022-01-01', '2023-09-30', '06:00:00', 'Residential'), ('VCX1SE', '1', 'PROFX', '2023-01-01', '2023-11-15', '06:00:00', 'Special event');");
                stmt.addBatch("INSERT INTO Zone (PLName, ZoneID) VALUES ('Poulton Deck', 'AS'), ('Poulton Deck', 'B'), ('Poulton Deck', 'C'), ('Partners Way Deck', 'D'), ('Partners Way Deck', 'A'), ('Partners Way Deck', 'BS'), ('Dan Allen Parking Deck', 'CS'), ('Dan Allen Parking Deck', 'DS'), ('Dan Allen Parking Deck', 'V');");
                stmt.addBatch("INSERT INTO Space (PLName, ZoneID, SpaceNo, SpaceType) VALUES ('Poulton Deck', 'AS', 1, 'Compact Car'), ('Partners Way Deck', 'A', 1, 'Electric'), ('Partners Way Deck', 'A', 2, 'Regular'), ('Partners Way Deck', 'A', 3, 'Regular'), ('Dan Allen Parking Deck', 'V', 1, 'Handicap'), ('Dan Allen Parking Deck', 'V', 2, 'Regular'), ('Poulton Deck', 'B', 1, 'Regular'), ('Poulton Deck', 'C', 1, 'Handicap'), ('Poulton Deck', 'C', 2, 'Electric'), ('Partners Way Deck', 'D', 1, 'Handicap'), ('Partners Way Deck', 'BS', 1, 'Electric'), ('Partners Way Deck', 'BS', 2, 'Compact Car'), ('Dan Allen Parking Deck', 'CS', 1, 'Electric'), ('Dan Allen Parking Deck', 'DS', 1, 'Compact Car');");
                stmt.addBatch("INSERT INTO CitationCategory (CitationName, Fees) VALUES ('Invalid Permit', 25.00), ('Expired Permit', 30.00), ('No Permit', 40.00), ('Handicap Invalid Permit', 12.50), ('Handicap Expired Permit', 15.00), ('Handicap No Permit', 20.00);");
                stmt.addBatch("INSERT INTO ParkingLocation ( PLName, ZoneID, SpaceNo, StaffID, AvailabilityStatus ) VALUES ('Poulton Deck', 'AS', 1, 1, FALSE), ('Partners Way Deck', 'A', 1, 1, FALSE), ('Partners Way Deck', 'A', 2, 1, FALSE), ('Partners Way Deck', 'A', 3, 1, FALSE), ('Dan Allen Parking Deck', 'V', 1,1,FALSE), ('Dan Allen Parking Deck', 'V', 2, 1, FALSE), ('Poulton Deck', 'B', 1, 1, TRUE), ('Poulton Deck', 'C', 1, 1, TRUE), ('Poulton Deck', 'C', 2, 1, TRUE), ('Partners Way Deck', 'D', 1, 1, TRUE), ('Partners Way Deck', 'BS', 1, 1, TRUE), ('Partners Way Deck', 'BS', 2, 1, TRUE), ('Dan Allen Parking Deck', 'CS', 1, 1, TRUE), ('Dan Allen Parking Deck', 'DS', 1, 1, TRUE);");
                stmt.addBatch("INSERT INTO Citation (CitationNo, CitationDate, CitationTime, PaymentStatus, StaffID, LicenseNo, PLName, CitationName) VALUES ('NP1', '2021-10-11', '08:00:00', 'Complete', 2, 'VAN-9910', 'Dan Allen Parking Deck', 'No Permit'), ('EP1', '2023-10-01', '10:00:00', 'Pending', 2, 'CRICKET', 'Poulton Deck', 'Expired Permit');");
                stmt.addBatch("INSERT INTO PermitLocation (PermitID, PLName, ZoneID, SpaceNo) VALUES ('VSBF1C', 'Dan Allen Parking Deck', 'V', 1), ('EJC1R', 'Partners Way Deck', 'A', 1), ('EJH2C', 'Partners Way Deck', 'A', 2), ('EIG3C', 'Partners Way Deck', 'A', 3), ('SST1R', 'Poulton Deck', 'AS', 1), ('VCX1SE', 'Dan Allen Parking Deck', 'V', 2);");
                stmt.executeBatch();


            } finally {
                System.out.print("Tables have been populated.....");
            }
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    }

}
