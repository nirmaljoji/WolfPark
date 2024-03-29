# Wolf Parking Management System
## Project Overview

This document outlines the design and implementation details for the Wolf Parking Management System, a database system for managing parking lots and users on campus. The system is designed to be used by administrators of the parking services and will maintain information on drivers, parking lots, zones, spaces, permits, vehicles, and citations.


<img src="https://github.com/nirmaljoji/WolfPark/assets/40449660/447e0061-cac8-47fd-8076-ead26fabe4cb" width="400" />


## System Operations 



The Wolf Parking Management System will support the following operations:

1. **Information Processing**
   - Enter/update/delete basic information about drivers, parking lots, zones, spaces, and permits.
   - Assign zones to each parking lot and a type to a given space.
   - Process citation appeals and update citation payment status accordingly.

2. **Maintaining Permits and Vehicle Information**
   - Assign permits to drivers based on their status.
   - Enter/update permit information and vehicle ownership information.
   - Remove or add vehicles to permits.

3. **Generating and Maintaining Citations**
   - Generate/maintain information for each citation.
   - Detect parking violations by checking if a car has a valid permit in the lot.
   - Allow drivers to pay or appeal citations.

4. **Reports**
   - Generate a report for citations.
   - For each lot, generate a report for the total number of citations in all zones for a given time range.
   - Return the list of zones for each lot as tuple pairs (lot, zone).
   - Return the number of cars currently in violation.
   - Return the number of employees with permits for a given parking zone.
   - Return permit information given an ID or phone number.
   - Return an available space number given a space type in a given parking lot.

## Instructions for execution

* Clone the github repository to your local device.
* In the file , Contstants.java , Enter your unity id and password.
* Run Main.java to execute the application.


## Database Schema

The database consists  of the following entities:

1. **Driver Information**
   - Name
   - Status (‘S’, ‘E’, or ‘V’ for student, employee, or visitor)
   - ID (UnivID for students and employees, phone number for visitors)

2. **Parking Lot Information**
   - Name
   - Address
   - Zones
   - Spaces

3. **Zone Information**
   - Zone ID (A, B, C, D, AS, BS, CS, DS, V)
   
4. **Space Information**
   - Space Number
   - Space Type (e.g., “electric,” “handicap,” “compact car,” default is “regular”)
   - Availability Status

5. **Permit Information**
   - Permit ID
   - Lot
   - Zone ID
   - Space Type
   - Car License Number
   - Start Date
   - Expiration Date
   - Expiration Time
   - Vehicle List
   - Associated UnivID or Phone Number
   - Permit Type (“residential,” “commuter,” “peak hours,” “special event,” “Park & Ride”)

6. **Vehicle Information**
   - Car License Number Plate
   - Model
   - Color
   - Manufacturer
   - Year

7. **Citation Information**
   - Citation Number
   - Car License Number
   - Model
   - Color
   - Citation Date
   - Citation Time
   - Lot
   - Category
   - Fee ($25, $30, $40 with discounts for handicap users)
   - Payment Status
