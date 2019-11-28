# Innovaccer-SummerGeeks-SDE-Intern
Innovaccer's SummerGeeks 2020 Assignment

# Overview
There are two android applications :
* **Reception** : This application is handled by the receptionist (administrator). It serves four purposes:     
    * Dashboard -> To monitor all the visitors that are currently in the facility.   
    * All Hosts -> To view all hosts that are registered.
    * Visitors Out -> To check the record of all the visitors who already visited the facility.
    * Register New Host -> To add a new host.
* **VisitorManagement** : This application is handled by the visitor at the reception desk. It serves two purpose:
    * Visitor-In -> To check-in the facility.
    * Visitor-Out -> To check-out of the facility.
    
## Resources Used
* Android Studio (Java and XML)
* Backend - Firebase Realtime Database
* Java Mail API (for auto mailing using JAVA)
* Textlocal API (for automated text messaging)

## Approach
* The Reception application is handled by the receptionist (administrator).
* The VisitorManagement application, at the reception desk, is handled by the visitor entering or exiting the facility.
* As there can be multiple hosts, the admin can add new hosts.
* On check-in, each visitor gets a unique token, which is used at the time of check-out.
* As it is a public register meaning each visitor won't have to authenticate itself.

## Application Flow

* The admin adds a new host by accessing the **Register New Host** option from the Navigation drawer of Dashboard.

Access the Navigation Drawer             |  Fill the required details and click the register button 
:----------------------------------------------:|:----------------------------------------------:
 ![NavDrawer](Screenshots/Screenshot_navigation_drawer.png)  |   ![RegHost](Screenshots/Screenshot_registernewhost.png)
 