Description of Employee Management System
This is a Java-based console application project that manages employee records, including personal details, address, and work experience, using an Oracle database. It provides a menu driven interface where users can do the following operations.
1.	Insert Employee
The system collects employee information while ensuring the validity of the input data and subsequently stores it in a database. It automatically generates and assigns a unique employee identification number, commencing from 100 and incrementing it automatically. The database comprises three tables: the employee table, the address table, and the work experience table, into which the relevant data is inserted accordingly.
2.	Update Employee
An employee can be located in the database by utilizing their employee ID, and any field can be updated while ensuring that the input is validated.
3.	Delete Employee
An employee is located in the database by their employee ID, and all associated information pertaining to that employee is removed from every relevant table. This action effectively deletes the employee from the database. Furthermore, if the table is found to be empty following the deletion, the sequence generator is reset.
4.	Display Employee
An inquiry is conducted within the database utilizing the employee id, which results in the retrieval and presentation of the employee's information.
5.	Exit Console
Allows user to exit the program safely.
Technologies and methods used :
1.	JDBC : To connect and have interaction between IntelliJ and Oracle database.
2.	Oracle : To store and manage employees data.
3.	Prepared Statements : for executing SQL queries securely.
4.	Transactions : To ensure changes commit only when it is completed.
5.	Validations : To check if the input that user is giving is valid for that field.
6.	Exception Handling : try-catch blocks which handles SQL and input errors, IllegalArgumentException which ensures valid user input, SQLException which handles database errors.

![image](https://github.com/user-attachments/assets/e3f2759a-efaa-49c8-b0ad-164883a4e0dd)
Asked for the input for initializing the operations.
![image](https://github.com/user-attachments/assets/572c28b2-1a09-402c-b0e4-e14438227971)
Inserted the employee details.

