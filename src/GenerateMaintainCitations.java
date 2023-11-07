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


public class GenerateMaintainCitations {


}
