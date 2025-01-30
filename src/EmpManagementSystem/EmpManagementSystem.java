package EmpManagementSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmpManagementSystem {
    public static final String jdbcUrl = "jdbc:oracle:thin:@//localhost:1521/xepdb1";
    public static final String username = "Yendreddy";
    public static final String password = "Durga1710";

    public static void main(String[] args) {
        //Takes input from the keyboard
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Select one of the options 1.Insert 2.Update 3.Delete 4.Display 5.Exit");
            System.out.println("Enter your choice:");
            int n = sc.nextInt();
            // Consume newline
            sc.nextLine();

            switch (n) {
                case 1 -> insertEmployee(sc);
                case 2 -> updateEmployee(sc);
                case 3 -> deleteEmployee(sc);
                case 4 -> displayEmployee(sc);
                case 5 -> exit = exitSystem();
                default -> System.out.println("Number should be 1 to 5");
            }
        }
    }

    /**
     * Finds an employee by their employee ID in the Oracle database.
     *
     * @param empId The employee ID to search for.
     * @return An Employee object if found, otherwise null.
     */
    private static Employee findEmployeeById(int empId) {

        String query = "SELECT e.name, e.designation, e.phone_number, e.salary, e.email, " +
                "a.street_number, a.street_name, a.city, a.state1, a.country, " +
                "w.company_name, w.job_role " +
                "FROM Employee3 e " +
                "LEFT JOIN Address3 a ON e.emp_id = a.emp_id " +
                "LEFT JOIN WorkExperience3 w ON e.emp_id = w.emp_id " +
                "WHERE e.emp_id = ?";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, empId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String designation = rs.getString("designation");
                String phoneNumber = rs.getString("phone_number");
                double salary = rs.getDouble("salary");
                String email = rs.getString("email");

                String streetNumber = rs.getString("street_number");
                String streetName = rs.getString("street_name");
                String city = rs.getString("city");
                String state = rs.getString("state1");
                String country = rs.getString("country");

                Address address = new Address(streetNumber, streetName, city, state, country);

                List<WorkExperience> workHistory = new ArrayList<>();
                do {
                    String companyName = rs.getString("company_name");
                    String jobRole = rs.getString("job_role");
                    if (companyName != null && jobRole != null) {
                        workHistory.add(new WorkExperience(companyName, jobRole));
                    }
                } while (rs.next());

                return new Employee(name, designation, phoneNumber, salary, email, address, workHistory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Resets the sequence to start from the next available employee ID.
     *
     * @param connection Database connection object.
     * @throws SQLException if a database access error occurs.
     */
    private static void resetSequence(Connection connection) throws SQLException {
        String resetSequenceSQL =
                "DECLARE " +
                        "    max_emp_id NUMBER; " +
                        "BEGIN " +
                        "    SELECT COALESCE(MAX(emp_id), 99) INTO max_emp_id FROM Employee3; " +
                        "    EXECUTE IMMEDIATE 'DROP SEQUENCE emp_id_seq'; " +
                        "    EXECUTE IMMEDIATE 'CREATE SEQUENCE emp_id_seq START WITH ' || (max_emp_id + 1) || ' INCREMENT BY 1'; " +
                        "END;";
        try {
            PreparedStatement resetSequenceStatement = connection.prepareStatement(resetSequenceSQL);
            resetSequenceStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserts the employee details and assign them to an employee id
     *
     * @param sc Scanner object for user input.
     * @throws SQLException if a database access error occurs.
     */
    private static void insertEmployee(Scanner sc) {
        try {
            System.out.println("Insert");

            System.out.println("name:");
            String name = getStringInput(sc);
            sc.nextLine();

            System.out.println("designation:");
            String designation = getStringInput(sc);
            sc.nextLine();

            System.out.println("phone number");
            String phoneNumber = getPhoneInput(sc);
            sc.nextLine();

            System.out.println("salary");
            double salary = getDoubleInput(sc);
            sc.nextLine();

            System.out.println("email");
            String email = getEmailInput(sc);
            sc.nextLine();

            System.out.println("address");

            System.out.println("street number");
            String streetNumber = sc.next();
            sc.nextLine();

            System.out.println("street name");
            String streetName = getStringInput(sc);
            sc.nextLine();

            System.out.println("city");
            String city = getStringInput(sc);
            sc.nextLine();

            System.out.println("state");
            String state = getStringInput(sc);
            sc.nextLine();

            System.out.println("country");
            String country = getStringInput(sc);
            sc.nextLine();

            Address address = new Address(streetNumber, streetName, city, state, country);
            System.out.println("Work experience");

            List<WorkExperience> workHistory = new ArrayList<>();
            System.out.println("Enter number of work experiences:");
            int numWorkExperiences = sc.nextInt();
            sc.nextLine();

            for (int i = 0; i < numWorkExperiences; i++) {
                System.out.println("Enter company name:");
                String companyName = sc.next();
                sc.nextLine();

                System.out.println("Enter job role:");
                String jobRole = sc.next();
                sc.nextLine();

                WorkExperience workExperience = new WorkExperience(companyName, jobRole);
                workHistory.add(workExperience);
            }

            try {
                Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

                // Start transaction
                connection.setAutoCommit(false);

                //Reset the sequence
                resetSequence(connection);

                String insertEmployeeQuery = "INSERT INTO Employee3 (emp_id,name, designation, phone_number, salary, email) VALUES (emp_id_seq.NEXTVAL,?, ?, ?, ?, ?)";
                PreparedStatement employeeStatement = connection.prepareStatement(insertEmployeeQuery, new String[]{"emp_id"});
                employeeStatement.setString(1, name);
                employeeStatement.setString(2, designation);
                employeeStatement.setString(3, phoneNumber);
                employeeStatement.setDouble(4, salary);
                employeeStatement.setString(5, email);
                employeeStatement.executeUpdate();
                int empId = 0;
                var rs = employeeStatement.getGeneratedKeys();
                if (rs.next()) {
                    empId = rs.getInt(1);
                }
                System.out.println("Generated empId: " + empId);

                String insertAddressQuery = "INSERT INTO Address3 (emp_id, street_number, street_name, city, state1, country) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement addressStatement = connection.prepareStatement(insertAddressQuery);
                addressStatement.setInt(1, empId);
                addressStatement.setString(2, address.streetNumber);
                addressStatement.setString(3, address.streetName);
                addressStatement.setString(4, address.city);
                addressStatement.setString(5, address.state);
                addressStatement.setString(6, address.country);
                addressStatement.executeUpdate();

                String insertWorkExperienceQuery = "INSERT INTO WorkExperience3 (emp_id, company_name, job_role) VALUES (?, ?, ?)";
                PreparedStatement workExperienceStatement = connection.prepareStatement(insertWorkExperienceQuery);
                for (WorkExperience workExperience : workHistory) {
                    workExperienceStatement.setInt(1, empId);
                    workExperienceStatement.setString(2, workExperience.companyName);
                    workExperienceStatement.setString(3, workExperience.jobRole);
                    workExperienceStatement.executeUpdate();
                }

                // Commit transaction
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error" + e.getMessage());
        }
    }

    /**
     * Update the employee details by searching the employee by its employee id
     *
     * @param sc Scanner object for user input.
     * @throws SQLException if a database access error occurs.
     */
    private static void updateEmployee(Scanner sc) {
        System.out.println("Enter Employee ID to update:");
        int empId = sc.nextInt();
        sc.nextLine();
        Employee emp = findEmployeeById(empId);
        if (emp != null) {
            System.out.println("Enter new name:");
            String name = getStringInput(sc);
            sc.nextLine();
            if (!name.isEmpty()) emp.show("name", name);

            System.out.println("Enter new designation:");
            String designation = getStringInput(sc);
            sc.nextLine();
            if (!designation.isEmpty()) emp.show("designation", designation);

            System.out.println("Enter new phone number:");
            String phoneNumber = getPhoneInput(sc);
            sc.nextLine();
            if (!phoneNumber.isEmpty()) emp.show("phone number", phoneNumber);

            System.out.println("Enter new salary:");
            double salary = getDoubleInput(sc);
            sc.nextLine();
            if (salary != 0) emp.show("salary", Double.parseDouble(String.valueOf(salary)));

            System.out.println("Enter new email:");
            String email = getEmailInput(sc);
            sc.nextLine();
            if (!email.isEmpty()) emp.show("email", email);

            System.out.println("Enter new address details:");

            System.out.println("Street number:");
            String streetNumber = sc.next();
            sc.nextLine();

            System.out.println("Street name:");
            String streetName = getStringInput(sc);
            sc.nextLine();

            System.out.println("City:");
            String city = getStringInput(sc);
            sc.nextLine();

            System.out.println("State:");
            String state = getStringInput(sc);
            sc.nextLine();

            System.out.println("Country:");
            String country = getStringInput(sc);
            sc.nextLine();

            if (!streetNumber.isEmpty() || !streetName.isEmpty() || !city.isEmpty() || !state.isEmpty() || !country.isEmpty()) {
                Address address = new Address(
                        streetNumber.isEmpty() ? emp.address.streetNumber : streetNumber,
                        streetName.isEmpty() ? emp.address.streetName : streetName,
                        city.isEmpty() ? emp.address.city : city,
                        state.isEmpty() ? emp.address.state : state,
                        country.isEmpty() ? emp.address.country : country
                );
                emp.show("address", address);
            }

            System.out.println("Enter new work history details:");
            System.out.println("Enter number of work experiences:");

            int numWorkExperiences = sc.nextInt();
            sc.nextLine();

            System.out.println("Company name:");
            String companyName = getStringInput(sc);
            sc.nextLine();

            System.out.println("Job role:");
            String jobRole = getStringInput(sc);
            sc.nextLine();

            if (!companyName.isEmpty() || !jobRole.isEmpty()) {
                List<WorkExperience> workHistory = new ArrayList<>();
                workHistory.add(new WorkExperience(
                        companyName.isEmpty() ? emp.workHistory.get(0).companyName : companyName,
                        jobRole.isEmpty() ? emp.workHistory.get(0).jobRole : jobRole
                ));
                emp.show("work experience", workHistory);
            }
            System.out.println("Employee details updated successfully.");
        } else {
            System.out.println("Employee ID not found.");
        }
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            connection.setAutoCommit(false);
            resetSequence(connection);

            // Update Employee table
            String updateEmployeeQuery = "UPDATE Employee3 SET name = ?, designation = ?, phone_number = ?, salary = ?, email = ? WHERE emp_id = ?";
            try {
                PreparedStatement employeeStatement = connection.prepareStatement(updateEmployeeQuery);
                employeeStatement.setString(1, emp.getName());
                employeeStatement.setString(2, emp.getDesignation());
                employeeStatement.setString(3, emp.getPhoneNumber());
                employeeStatement.setDouble(4, emp.getSalary());
                employeeStatement.setString(5, emp.getEmail());
                employeeStatement.setInt(6, empId);
                employeeStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Update Address table
            String updateAddressQuery = "UPDATE Address3 SET street_number = ?, street_name = ?, city = ?, state1 = ?, country = ? WHERE emp_id = ?";
            try {
                PreparedStatement addressStatement = connection.prepareStatement(updateAddressQuery);
                addressStatement.setString(1, emp.address.streetNumber);
                addressStatement.setString(2, emp.address.streetName);
                addressStatement.setString(3, emp.address.city);
                addressStatement.setString(4, emp.address.state);
                addressStatement.setString(5, emp.address.country);
                addressStatement.setInt(6, empId);
                addressStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Delete existing work experiences for the employee
            String deleteWorkExperienceQuery = "DELETE FROM WorkExperience3 WHERE emp_id = ?";
            try {
                PreparedStatement deleteWorkExperienceStatement = connection.prepareStatement(deleteWorkExperienceQuery);
                deleteWorkExperienceStatement.setInt(1, empId);
                deleteWorkExperienceStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Insert new work experiences
            String insertWorkExperienceQuery = "INSERT INTO WorkExperience3 (emp_id, company_name, job_role) VALUES (?, ?, ?)";
            try {
                PreparedStatement workExperienceStatement = connection.prepareStatement(insertWorkExperienceQuery);
                for (WorkExperience workExperience : emp.workHistory) {
                    workExperienceStatement.setInt(1, empId);
                    workExperienceStatement.setString(2, workExperience.companyName);
                    workExperienceStatement.setString(3, workExperience.jobRole);
                    workExperienceStatement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            connection.commit();
            System.out.println("Employee details updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete the employee by finding it by employee id
     *
     * @param sc Scanner object for user input.
     * @throws SQLException if a database access error occurs.
     */
    private static void deleteEmployee(Scanner sc) {
        System.out.println("Enter Employee ID to delete:");
        int empId = sc.nextInt();
        sc.nextLine();
        Employee emp = findEmployeeById(empId);
        if (emp != null) {
            try {
                Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
                connection.setAutoCommit(false); // Start transaction

                // Delete work experience details
                String deleteWorkExperienceSQL = "DELETE FROM WorkExperience3 WHERE emp_id = ?";
                try {
                    PreparedStatement deleteWorkExperienceStatement = connection.prepareStatement(deleteWorkExperienceSQL);
                    deleteWorkExperienceStatement.setInt(1, empId);
                    deleteWorkExperienceStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // Delete address details
                String deleteAddressSQL = "DELETE FROM Address3 WHERE emp_id = ?";
                try {
                    PreparedStatement deleteAddressStatement = connection.prepareStatement(deleteAddressSQL);
                    deleteAddressStatement.setInt(1, empId);
                    deleteAddressStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // Delete employee details
                String deleteEmployeeSQL = "DELETE FROM Employee3 WHERE emp_id = ?";
                try {
                    PreparedStatement deleteEmployeeStatement = connection.prepareStatement(deleteEmployeeSQL);
                    deleteEmployeeStatement.setInt(1, empId);
                    deleteEmployeeStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // Reset the sequence if all records are deleted
                String checkRecordsSQL = "SELECT COUNT(*) FROM Employee3";
                try {
                    PreparedStatement checkRecordsStatement = connection.prepareStatement(checkRecordsSQL);
                    ResultSet rs = checkRecordsStatement.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        resetSequence(connection);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                connection.commit();
                System.out.println("Employee deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Employee ID not found.");
        }
    }

    /**
     * Display the employee details by finding it by employee id
     *
     * @param sc Scanner object for user input.
     */
    private static void displayEmployee(Scanner sc) {
        System.out.println("Enter Employee ID to display:");
        int empId = sc.nextInt();
        sc.nextLine();

        Employee emp = findEmployeeById(empId);
        if (emp != null) {
            System.out.println("Employee Details:");
            System.out.println("ID: " + empId);
            System.out.println("Name: " + emp.getName());
            System.out.println("Designation: " + emp.getDesignation());
            System.out.println("Phone Number: " + emp.getPhoneNumber());
            System.out.println("Salary: " + emp.getSalary());
            System.out.println("Email: " + emp.getEmail());
            System.out.println("Address: " + emp.getAddress().getStreetNumber() + ", " +
                    emp.getAddress().getStreetName() + ", " +
                    emp.getAddress().getCity() + ", " +
                    emp.getAddress().getState() + ", " +
                    emp.getAddress().getCountry());
            System.out.println("Work Experience:");
            for (WorkExperience workExperience : emp.getWorkHistory()) {
                System.out.println("Company Name: " + workExperience.getCompanyName());
                System.out.println("Job Role: " + workExperience.getJobRole());
            }
        } else {
            System.out.println("Employee ID not found.");
        }
    }

    /**
     * Exit the console
     *
     * @param sc Scanner object for user input.
     *@return true to indicate the system should exit
     */
    private static boolean exitSystem() {
        System.out.println("Exiting the console.");
        return true;
    }

    /**
     * Validates and retrieves a positive double input from the user.
     *
     * @param sc Scanner object for user input.
     * @return A positive double value entered by the user.
     */
    private static double getDoubleInput(Scanner sc){
        double value;
        while (true){
            try {
                value = Double.parseDouble(sc.next());
                if(value<0){
                    throw new IllegalArgumentException("value must be greater than zero");
                }
                return value;
            }catch (Exception e){
                System.out.println("Invalid input, please enter a positive number");
            }
        }
    }

    /**
     * checks if given string input contains only alphabets or not
     *
     * @param sc Scanner object for user input
     * @return proper string entered by the user
     */
    private static String getStringInput(Scanner sc){
        while (true){
            String input = sc.next().trim();
            if(input.isEmpty() || input.matches("[A-Za-z]+")){
                return input;
            }else {
                System.out.println("Invalid input, Only alphabets are allowed");
            }
        }
    }

    /**
     * checks if given phone number input is valid or not
     *
     * @param sc Scanner object for user input.
     * @return valid phone number entered by user
     */
    private static String getPhoneInput(Scanner sc) {
        while (true){
            String input = sc.next().trim();
            if(!input.matches("[1-9]\\d{9}")){
                System.out.println("phone number should have 10 digits and the first digit should not be zero");
            }else {
                return input;
            }
        }
    }

    /**
     * Checks if the given email input is valid or not
     *
     * @param sc Scanner object for user input.
     * @return valid email entered by user
     */
    private static String getEmailInput(Scanner sc){
        while (true) {
            String input = sc.next().trim();
            if(!input.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9._]+\\.[A-Za-z]{2,6}$")) {
                System.out.println("Invalid email address");
            } else{
                return input;
            }
        }
    }
}
