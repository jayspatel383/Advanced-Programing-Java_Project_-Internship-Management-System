# Advanced-Programing-Java_Project_-Internship-Management-System
Below are the steps to run the project 

1. Download the project and open in intelij 
2. open the application.properties file and do the changes mentioned in it like port number and the database password etc
3. Once the changes are made please create a database called internship_db;
4. Once the database is created run the spring boot application 
5. once the application starts the hibernate will check if the tables exists if not , the tables will be created Automatically 
6. once the application is running please check the port-number on which the tomcat is running the application on 




-- First, clear existing data (if any)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE intern_skills;
TRUNCATE TABLE activity_log;
TRUNCATE TABLE internship_applications;
TRUNCATE TABLE intern;
TRUNCATE TABLE company;
TRUNCATE TABLE skill;
SET FOREIGN_KEY_CHECKS = 1;

// you may insert the following dummy data in the tables created 
//Insert basic skills

INSERT INTO skill (name) VALUES ('Java');
INSERT INTO skill (name) VALUES ('Python');
INSERT INTO skill (name) VALUES ('SQL');

//Insert basic companies

INSERT INTO company (name, location) VALUES ('Google', 'USA');
INSERT INTO company (name, location) VALUES ('Microsoft', 'USA');

//Insert one intern

INSERT INTO intern (name, email, university) 
VALUES ('Test Intern', 'test@email.com', 'Gisma University');

//Assign skills

INSERT INTO intern_skills (intern_id, skill_id) VALUES (1, 1);
INSERT INTO intern_skills (intern_id, skill_id) VALUES (1, 2);

###... Below is the apis i have provided which can be used to test if the data are added and CRUD operations are performed 

1. Get All Skills
GET http://localhost:8080/skills

2. Get All Companies
GET http://localhost:8080/companies

3. Get All Interns
GET http://localhost:8080/interns

4. Get Applications by Status
GET http://localhost:8080/applications/status/PENDING

5. Get Activity Logs for an Intern
GET http://localhost:8080/activitylogs/intern/1

6. Create a New Application
POST http://localhost:8080/applications
Content-Type: application/json

{
"internId": 1,
"companyId": 2,
"status": "PENDING",
"applicationDate": "2026-03-19T10:30:00"
}

**Note : When testing the APIs if an Intern Holds and Application or and Application is assigned to an Intern the Delete method will throw and errow 
for this first the Application must be deleted and then the Intern Will be Deleted 
**Note: All the apis provided above are the get method other methods like Post , Delete , Put , can be also performed** 




