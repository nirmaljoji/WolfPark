import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PrepareTable {
    static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/"+Constants.UnityID;

    public static void createTable(){
        try {

            // Load the driver. This creates an instance of the driver
            // and calls the registerDriver method to make MySql Thin
            // driver, available to clients.

            Class.forName("org.mariadb.jdbc.Driver");

            String user = Constants.UnityID;
            String passwd = Constants.SqlPassword;

            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {


                conn = DriverManager.getConnection(jdbcURL, user, passwd);
                stmt = conn.createStatement();

                stmt.addBatch("SET FOREIGN_KEY_CHECKS=0;");
                stmt.addBatch("DROP TABLE IF EXISTS Staff, Driver, VehicleModelManufacturer, Vehicle, Permit, DriverVehiclePermit, ParkingLot, Zone, Space, PermitLocation, ParkingLocation, CitationCategory, Citation, Appeals;");
                stmt.addBatch("SET FOREIGN_KEY_CHECKS=1;");

                stmt.addBatch(" CREATE TABLE Staff( StaffID INT PRIMARY KEY, Role VARCHAR(32), CHECK (Role in ('Admin', 'Security')) );");

                stmt.addBatch(" CREATE TABLE Driver ( DriverID VARCHAR(10) PRIMARY KEY, DriverName VARCHAR(255) NOT NULL, Status VARCHAR(1) CHECK (Status IN ('S', 'E', 'V')), CHECK ( (Status = 'V' AND CHAR_LENGTH(CONVERT(DriverID, CHAR(10))) = 10) ) );");

                stmt.addBatch(" CREATE TABLE VehicleModelManufacturer ( Model VARCHAR(255) PRIMARY KEY, Manufacturer VARCHAR(255) );");

                stmt.addBatch(" CREATE TABLE Vehicle ( LicenseNo VARCHAR(255) PRIMARY KEY, DriverID VARCHAR(10) NOT NULL, Model VARCHAR(255) NOT NULL, Color VARCHAR(255), Year INT, VehicleCategory VARCHAR(32), FOREIGN KEY (DriverID) REFERENCES Driver(DriverID) ON DELETE CASCADE, FOREIGN KEY (Model) REFERENCES VehicleModelManufacturer(Model), CHECK (VehicleCategory in ('Electric', 'Handicap', 'Compact car', 'Regular')) );");

                stmt.addBatch(" CREATE TABLE Permit ( PermitID VARCHAR(10) PRIMARY KEY, StaffID INT NOT NULL, LicenseNo VARCHAR(255) NOT NULL, StartDate DATE NOT NULL, ExpirationDate DATE NOT NULL, ExpirationTime TIME NOT NULL, PermitType VARCHAR(32), FOREIGN KEY (StaffID) REFERENCES Staff(StaffID), FOREIGN KEY (LicenseNo) REFERENCES Vehicle(LicenseNo) ON DELETE CASCADE, CHECK (PermitType in ('Residential', 'Commuter', 'Peak Hours', 'Special Event', 'Park & Ride')) );");

                stmt.addBatch(" CREATE TABLE DriverVehiclePermit ( DriverID VARCHAR(10) NOT NULL, LicenseNo VARCHAR(255) NOT NULL, PRIMARY KEY (DriverID, LicenseNo), FOREIGN KEY (DriverID) REFERENCES Driver(DriverID) ON DELETE CASCADE, FOREIGN KEY (LicenseNo) REFERENCES Vehicle(LicenseNo) ON DELETE CASCADE );");

                stmt.addBatch(" CREATE TABLE ParkingLot ( PLName VARCHAR(255) PRIMARY KEY, StaffID INT NOT NULL, Address VARCHAR(255) UNIQUE NOT NULL, FOREIGN KEY (StaffID) REFERENCES Staff(StaffID) );");

                stmt.addBatch(" CREATE TABLE Zone ( PLName VARCHAR(255) NOT NULL, ZoneID VARCHAR(2) NOT NULL, PRIMARY KEY (PLName, ZoneID), FOREIGN KEY (PLName) REFERENCES ParkingLot(PLName) ON DELETE CASCADE, CHECK (ZoneID in ('A', 'B', 'C', 'D', 'AS', 'BS', 'CS', 'DS', 'V')) );");

                stmt.addBatch(" CREATE TABLE Space ( PLName VARCHAR(255) NOT NULL, ZoneID VARCHAR(2) NOT NULL, SpaceNo INT NOT NULL, SpaceType VARCHAR(255) NOT NULL, PRIMARY KEY (PLName, ZoneID, SpaceNo), FOREIGN KEY (PLName, ZoneID) REFERENCES Zone(PLName, ZoneID) ON DELETE CASCADE, CHECK (SpaceType in ('Electric', 'Handicap', 'Compact car', 'Regular')) );");

                stmt.addBatch( " CREATE TABLE PermitLocation ( PermitID VARCHAR(10) PRIMARY KEY, PLName VARCHAR(255) NOT NULL, ZoneID VARCHAR(2) NOT NULL, SpaceNo INT NOT NULL, FOREIGN KEY (PermitID) REFERENCES Permit(PermitID) ON DELETE CASCADE, FOREIGN KEY (PLName, ZoneID, SpaceNo) REFERENCES Space(PLName, ZoneID, SpaceNo) );");

                stmt.addBatch( " CREATE TABLE ParkingLocation ( PLName VARCHAR(255) NOT NULL, ZoneID VARCHAR(2), SpaceNo INT NOT NULL, StaffID INT NOT NULL, AvailabilityStatus BOOLEAN NOT NULL, PRIMARY KEY (PLName, ZoneID, SpaceNo), FOREIGN KEY (PLName, ZoneID, SpaceNo) REFERENCES Space(PLName, ZoneID, SpaceNo) ON DELETE CASCADE, FOREIGN KEY (StaffID) REFERENCES Staff(StaffID) );");

                stmt.addBatch( " CREATE TABLE CitationCategory ( CitationName VARCHAR(32) PRIMARY KEY, Fees INT NOT NULL, CHECK (CitationName in ('Invalid Permit', 'Expired Permit', 'No Permit')) );");

                stmt.addBatch( " CREATE TABLE Citation ( CitationNo VARCHAR(10) PRIMARY KEY, CitationDate DATE NOT NULL, CitationTime TIME NOT NULL, PaymentStatus ENUM ('Pending', 'Complete', 'Not Required'), StaffID INT NOT NULL, LicenseNo VARCHAR(255) NOT NULL, PLName VARCHAR(255) NOT NULL, CitationName VARCHAR(32), FOREIGN KEY (CitationName) REFERENCES CitationCategory(CitationName), FOREIGN KEY (StaffID) REFERENCES Staff(StaffID), FOREIGN KEY (LicenseNo) REFERENCES Vehicle(LicenseNo), FOREIGN KEY (PLName) REFERENCES ParkingLot(PLName), CHECK (CitationName in ('Invalid Permit', 'Expired Permit', 'No Permit')));");

                stmt.addBatch( " CREATE TABLE Appeals ( DriverID VARCHAR(10) NOT NULL, CitationNo VARCHAR(10) NOT NULL, AppealStatus VARCHAR(32), FOREIGN KEY (DriverID) REFERENCES Driver(DriverID), FOREIGN KEY (CitationNo) REFERENCES Citation(CitationNo), CHECK (AppealStatus in ('Requested', 'Rejected', 'Accepted')) );");

                stmt.executeBatch();

//                while (rs.next()) {
//                    String s = rs.getString("COF_NAME");
//                    float n = rs.getFloat("PRICE");
//                    System.out.println(s + "  " + n);
//                }

            } finally {
                //close(rs);
                close(stmt);
                close(conn);
            }
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    }

    public static void insertData(){
        try {

            // Load the driver. This creates an instance of the driver
            // and calls the registerDriver method to make MySql Thin
            // driver, available to clients.

            Class.forName("org.mariadb.jdbc.Driver");

            String user = Constants.UnityID;
            String passwd = Constants.SqlPassword;

            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {


                conn = DriverManager.getConnection(jdbcURL, user, passwd);
                stmt = conn.createStatement();

                stmt.addBatch("INSERT INTO Staff (StaffID, Role) VALUES (1, 'Admin'), (2, 'Security');");
                stmt.addBatch("INSERT INTO Driver (DriverID, DriverName, Status) VALUES ('7729119111', 'Sam BankmanFried', 'V'), ('266399121', 'John Clay', 'E'), ('366399121','Julia Hicks', 'E'), ('466399121', 'Ivan Garcia', 'E'), ('122765234', 'Sachin Tendulkar','S'), ('9194789124', 'Charles Xavier', 'V');");
                stmt.addBatch("INSERT INTO ParkingLot (PLName, StaffID, Address) VALUES ('Poulton Deck', 1, '1021 Main Campus Dr Raleigh, NC, 27606'), ('Partners Way Deck', 1, '851 Partners Way Raleigh, NC, 27606'), ('Dan Allen Parking Deck', 1, '110 Dan Allen Dr Raleigh, NC, 27607');");
                stmt.addBatch("INSERT INTO VehicleModelManufacturer (Model, Manufacturer) VALUES ('GT-R-Nismo', 'Nissan'), ('Model S', 'Tesla'), ('M2 Coupe', 'BMW'), ('Continental GT Speed', 'Bentley'), ('Civic SI', 'Honda'), ('Taycan Sport Turismo', 'Porsche');");
                stmt.addBatch("INSERT INTO Vehicle (LicenseNo, DriverID, Model, Color, Year, VehicleCategory) VALUES ('SBF', '7729119111', 'GT-R-Nismo', 'Pearl White TriCoat', 2024, 'Regular'), ('Clay1', '266399121', 'Model S', 'Ultra Red', 2023, 'Electric'), ('Hicks1', '366399121', 'M2 Coupe', 'Zandvoort Blue', 2024, 'Regular'), ('Garcia1', '466399121', 'Continental GT Speed', 'Blue Fusion', 2024, 'Regular'), ('CRICKET', '122765234', 'Civic SI', 'Sonic Gray Pearl', 2024, 'Compact Car'), ('PROFX', '9194789124', 'Taycan Sport Turismo', 'Frozenblue Metallic', 2024, 'Handicap');");
                stmt.addBatch("INSERT INTO Permit (PermitID, StaffID, LicenseNo, StartDate, ExpirationDate, ExpirationTime, PermitType) VALUES ('VSBF1C', '1', 'SBF', '2023-01-01', '2024-01-01', '06:00:00', 'Commuter'), ('EJC1R', '1', 'Clay1', '2010-01-01', '2030-01-01', '06:00:00', 'Residential'), ('EJH2C', '1', 'Hicks1', '2023-01-01', '2024-01-01', '06:00:00', 'Commuter'), ('EIG3C', '1', 'Garcia1', '2023-01-01', '2024-01-01', '06:00:00', 'Commuter'), ('SST1R', '1', 'CRICKET', '2022-01-01', '2023-09-30', '06:00:00', 'Residential'), ('VCX1SE', '1', 'PROFX', '2023-01-01', '2023-11-15', '06:00:00', 'Special event');");
                stmt.addBatch("INSERT INTO Zone (PLName, ZoneID) VALUES ('Poulton Deck', 'AS'), ('Poulton Deck', 'B'), ('Poulton Deck', 'C'), ('Partners Way Deck', 'D'), ('Partners Way Deck', 'A'), ('Partners Way Deck', 'BS'), ('Dan Allen Parking Deck', 'CS'), ('Dan Allen Parking Deck', 'DS'), ('Dan Allen Parking Deck', 'V');");
                stmt.addBatch("INSERT INTO Space (PLName, ZoneID, SpaceNo, SpaceType) VALUES ('Poulton Deck', 'AS', 1, 'Compact Car'), ('Partners Way Deck', 'A', 1, 'Electric'), ('Partners Way Deck', 'A', 2, 'Regular'), ('Partners Way Deck', 'A', 3, 'Regular'), ('Dan Allen Parking Deck', 'V', 1, 'Handicap'), ('Dan Allen Parking Deck', 'V', 2, 'Regular'), ('Poulton Deck', 'B', 1, 'Regular'), ('Poulton Deck', 'C', 1, 'Handicap'), ('Poulton Deck', 'C', 2, 'Electric'), ('Partners Way Deck', 'D', 1, 'Handicap'), ('Partners Way Deck', 'BS', 1, 'Electric'), ('Partners Way Deck', 'BS', 2, 'Compact Car'), ('Dan Allen Parking Deck', 'CS', 1, 'Electric'), ('Dan Allen Parking Deck', 'DS', 1, 'Compact Car');");
                stmt.executeBatch();



//
//
//                while (rs.next()) {
//                    String s = rs.getString("COF_NAME");
//                    float n = rs.getFloat("PRICE");
//                    System.out.println(s + "  " + n);
//                }

            } finally {
                //close(rs);
                close(stmt);
                close(conn);
            }
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    }
    static void close(Connection conn) {
        if(conn != null) {
            try { conn.close(); } catch(Throwable whatever) {}
        }
    }

    static void close(Statement st) {
        if(st != null) {
            try { st.close(); } catch(Throwable whatever) {}
        }
    }

    static void close(ResultSet rs) {
        if(rs != null) {
            try { rs.close(); } catch(Throwable whatever) {}
        }
    }
}
