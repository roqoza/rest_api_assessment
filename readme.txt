This is a spring-boot application based on the tutorial by SARATH MURALEEDHARAN 
http://www.techforumist.com/spring-boot-security-jpa-using-jwt-angularjs-tutorial-1/ 
(http://www.techforumist.com/spring-boot-security-jpa-using-jwt-angularjs-tutorial-1/)
It can be run in both java 1.7 and 1.8
Maven 4.0 is used for the build
H2 embedded database is used for persistence

To run the application:
unzip the file to jwt-spring-boot-assessment
I use Git Bach as my command line tool
run mvn -e clean install to build the application (make sure this is inside the folder jwt-spring-boot-assessment)
run mvn spring-boot:run to start the application
on a web browser type http://localhost:8080 to start using the application

There are 2 roles: admin and user. When adding a user, then it should be specified where a user is admin/user.

1. Allow users to submit their details containing a username, phone number and password.
User can register or an admin user can add a new user.
2. Add functionality that allows the user to login/logout.
Once a user is registered or added, then that user can login/logout.
3. Create a view that contains a list of all the unique users that are registered.
Reg Users brings up a list of all the registered users.
4. Create a view that contains a count or a list of users that have called login within the last 5 minutes.
Last Login(5 min) brings up a list of all the users who last logged in less than 5 minutes ago.
5. Restrict the functionality in 3. to authenticated users only.
Auth Users brings up a list of all users that are currently logged in and their token has not expired.
6. The list or number of users in 4. must update dynamically. When a 2nd user has started a session, the counter or list in 4. must increase.
7. Expire the security token after 3 minutes if the user is inactive. When a user's token expires or is logged out, the counter or list in 4. must decrease.
The sucurity token expires after 3 minutes
8. Optional: Include auditing for user interactions.
