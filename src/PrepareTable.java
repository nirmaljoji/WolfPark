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

                stmt.addBatch(" CREATE TABLE Driver ( DriverID BIGINT PRIMARY KEY, DriverName VARCHAR(255) NOT NULL, Status VARCHAR(1) CHECK (Status IN ('S', 'E', 'V')), CHECK ( (Status = 'V' AND CHAR_LENGTH(CONVERT(DriverID, CHAR(10))) = 10) ) );");

                stmt.addBatch(" CREATE TABLE VehicleModelManufacturer ( Model VARCHAR(255) PRIMARY KEY, Manufacturer VARCHAR(255) );");

                stmt.addBatch(" CREATE TABLE Vehicle ( LicenseNo VARCHAR(255) PRIMARY KEY, DriverID BIGINT NOT NULL, Model VARCHAR(255) NOT NULL, Color VARCHAR(255), Year INT, VehicleCategory VARCHAR(32), FOREIGN KEY (DriverID) REFERENCES Driver(DriverID) ON DELETE CASCADE, FOREIGN KEY (Model) REFERENCES VehicleModelManufacturer(Model), CHECK (VehicleCategory in ('Electric', 'Handicap', 'Compact car', 'Regular')) );");

                stmt.addBatch(" CREATE TABLE Permit ( PermitID INT PRIMARY KEY, StaffID INT NOT NULL, LicenseNo VARCHAR(255) NOT NULL, StartDate DATE NOT NULL, ExpirationDate DATE NOT NULL, ExpirationTime TIME NOT NULL, PermitType VARCHAR(32), FOREIGN KEY (StaffID) REFERENCES Staff(StaffID), FOREIGN KEY (LicenseNo) REFERENCES Vehicle(LicenseNo) ON DELETE CASCADE, CHECK (PermitType in ('Residential', 'Commuter', 'Peak Hours', 'Special Event', 'Park & Ride')) );");

                stmt.addBatch(" CREATE TABLE DriverVehiclePermit ( DriverID BIGINT NOT NULL, LicenseNo VARCHAR(255) NOT NULL, PRIMARY KEY (DriverID, LicenseNo), FOREIGN KEY (DriverID) REFERENCES Driver(DriverID) ON DELETE CASCADE, FOREIGN KEY (LicenseNo) REFERENCES Vehicle(LicenseNo) ON DELETE CASCADE );");

                stmt.addBatch(" CREATE TABLE ParkingLot ( PLName VARCHAR(255) PRIMARY KEY, StaffID INT NOT NULL, Address VARCHAR(255) UNIQUE NOT NULL, FOREIGN KEY (StaffID) REFERENCES Staff(StaffID) );");

                stmt.addBatch(" CREATE TABLE Zone ( PLName VARCHAR(255) NOT NULL, ZoneID VARCHAR(2) NOT NULL, PRIMARY KEY (PLName, ZoneID), FOREIGN KEY (PLName) REFERENCES ParkingLot(PLName) ON DELETE CASCADE, CHECK (ZoneID in ('A', 'B', 'C', 'D', 'AS', 'BS', 'CS', 'DS', 'V')) );");

                stmt.addBatch(" CREATE TABLE Space ( PLName VARCHAR(255) NOT NULL, ZoneID VARCHAR(2) NOT NULL, SpaceNo INT NOT NULL, SpaceType VARCHAR(255) NOT NULL, PRIMARY KEY (PLName, ZoneID, SpaceNo), FOREIGN KEY (PLName, ZoneID) REFERENCES Zone(PLName, ZoneID) ON DELETE CASCADE, CHECK (SpaceType in ('Electric', 'Handicap', 'Compact car', 'Regular')) );");

                stmt.addBatch( " CREATE TABLE PermitLocation ( PermitID INT PRIMARY KEY, PLName VARCHAR(255) NOT NULL, ZoneID VARCHAR(2) NOT NULL, SpaceNo INT NOT NULL, FOREIGN KEY (PermitID) REFERENCES Permit(PermitID) ON DELETE CASCADE, FOREIGN KEY (PLName, ZoneID, SpaceNo) REFERENCES Space(PLName, ZoneID, SpaceNo) );");

                stmt.addBatch( " CREATE TABLE ParkingLocation ( PLName VARCHAR(255) NOT NULL, ZoneID VARCHAR(2), SpaceNo INT NOT NULL, StaffID INT NOT NULL, AvailabilityStatus BOOLEAN NOT NULL, PRIMARY KEY (PLName, ZoneID, SpaceNo), FOREIGN KEY (PLName, ZoneID, SpaceNo) REFERENCES Space(PLName, ZoneID, SpaceNo) ON DELETE CASCADE, FOREIGN KEY (StaffID) REFERENCES Staff(StaffID) );");

                stmt.addBatch( " CREATE TABLE CitationCategory ( CitationName VARCHAR(32) PRIMARY KEY, Fees INT NOT NULL, CHECK (CitationName in ('Invalid Permit', 'Expired Permit', 'No Permit')) );");

                stmt.addBatch( " CREATE TABLE Citation ( CitationNo INT PRIMARY KEY, CitationDate DATE NOT NULL, CitationTime TIME NOT NULL, PaymentStatus ENUM ('Pending', 'Complete', 'Not Required'), StaffID INT NOT NULL, LicenseNo VARCHAR(255) NOT NULL, PLName VARCHAR(255) NOT NULL, CitationName VARCHAR(32), FOREIGN KEY (CitationName) REFERENCES CitationCategory(CitationName), FOREIGN KEY (StaffID) REFERENCES Staff(StaffID), FOREIGN KEY (LicenseNo) REFERENCES Vehicle(LicenseNo), FOREIGN KEY (PLName) REFERENCES ParkingLot(PLName), CHECK (CitationName in ('Invalid Permit', 'Expired Permit', 'No Permit')));");

                stmt.addBatch( " CREATE TABLE Appeals ( DriverID BIGINT NOT NULL, CitationNo INT NOT NULL, AppealStatus VARCHAR(32), FOREIGN KEY (DriverID) REFERENCES Driver(DriverID), FOREIGN KEY (CitationNo) REFERENCES Citation(CitationNo), CHECK (AppealStatus in ('Requested', 'Rejected', 'Accepted')) );");

                stmt.executeBatch();


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
