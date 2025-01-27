package EmpManagementSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class EmpManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);//Takes input from the keyboard
        List<Employee> employees = new ArrayList<>();
        boolean exit = false;

        while (!exit) {
            System.out.println("Select one of the options 1.Insert 2.Update 3.Delete 4.Display 5.Exit");
            System.out.println("Enter your choice:");
            int n = sc.nextInt();
            sc.nextLine();

            switch (n) {
                case 1 -> insertEmployee(sc, employees);
                case 2 -> updateEmployee(sc, employees);
                case 3 -> deleteEmployee(sc, employees);
                case 4 -> displayEmployee(sc, employees);
                case 5 -> exit = exitSystem();
                default -> System.out.println("Number should be 1 to 5");
            }
        }
    }

    static void insertEmployee(Scanner sc, List<Employee> employees) {//Inserts the employee details and assign them to an employee id
        try{
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

            System.out.println("company name");
            String companyName = getStringInput(sc);
            sc.nextLine();

            System.out.println("job role");
            String jobRole = getStringInput(sc);
            sc.nextLine();

            workHistory.add(new WorkExperience(companyName, jobRole));
            employees.add(new Employee( name, designation, phoneNumber, salary, email, address, workHistory));
        }catch(IllegalArgumentException e){
            System.out.println("Error" +e.getMessage());
        }
        }


    static void updateEmployee(Scanner sc, List<Employee> employees) {//Update the employee details by searching the employee by its employee id
        System.out.println("Enter Employee ID to update:");
        int empId = sc.nextInt();
        sc.nextLine();
        Employee emp = findEmployeeById(employees, empId);
        if (emp != null) {
            System.out.println("Enter new name:");
            String name = getStringInput(sc);
            if (!name.isEmpty()) emp.show("name", name);

            System.out.println("Enter new designation:");
            String designation = getStringInput(sc);
            if (!designation.isEmpty()) emp.show("designation", designation);

            System.out.println("Enter new phone number:");
            String phoneNumber = getPhoneInput(sc);
            if (!phoneNumber.isEmpty()) emp.show("phone number", phoneNumber);

            System.out.println("Enter new salary:");
            double salary = getDoubleInput(sc);
            if (salary != 0) emp.show("salary", Double.parseDouble(String.valueOf(salary)));

            System.out.println("Enter new email:");
            String email = getEmailInput(sc);
            if (!email.isEmpty()) emp.show("email", email);

            System.out.println("Enter new address details:");
            System.out.println("Street number:");
            String streetNumber = sc.next();
            sc.nextLine();
            System.out.println("Street name:");
            String streetName = getStringInput(sc);
            System.out.println("City:");
            String city = getStringInput(sc);
            System.out.println("State:");
            String state = getStringInput(sc);
            System.out.println("Country:");
            String country = getStringInput(sc);
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
            System.out.println("Company name:");
            String companyName = getStringInput(sc);
            System.out.println("Job role:");
            String jobRole = getStringInput(sc);
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
    }

    static void deleteEmployee(Scanner sc, List<Employee> employees) {//Delete the employee by finding it by employee id
        System.out.println("Enter Employee ID to delete:");
        int empId = sc.nextInt();
        sc.nextLine();
        Employee emp = findEmployeeById(employees, empId);
        if (emp != null) {
            employees.remove(emp);
            System.out.println("Employee deleted successfully.");
        } else {
            System.out.println("Employee ID not found.");
        }
    }

    static void displayEmployee(Scanner sc, List<Employee> employees) {//Display the employee details by finding it by employee id
        System.out.println("Enter Employee ID to display:");
        int empId = sc.nextInt();
        sc.nextLine();
        Employee emp = findEmployeeById(employees, empId);
        if (emp != null) {
            System.out.println(emp);
        } else {
            System.out.println("Employee ID not found.");
        }
    }

    static boolean exitSystem() {//Exit the console
        System.out.println("Exiting the console.");
        return true;
    }

    static Employee findEmployeeById(List<Employee> employees, int empId) {//Find whether the give id matches with any of the employee id in the employees list
        for (Employee emp : employees) {
            if (emp.getEmpId() == empId) {
                return emp;
            }
        }
        return null;
    }
    private static double getDoubleInput(Scanner sc){//checks if given salary input is valid or not
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
    private static String getStringInput(Scanner sc){//checks if given string input is valid or not
        while (true){
            String input = sc.next().trim();
            if(!input.matches("[A-Za-z]+")){
                System.out.println("Invalid input, Only alphabets are allowed");
            }else {
                return input;
            }
        }
    }
    private static String getPhoneInput(Scanner sc){//checks if given phone number input is valid or not
        while (true){
            String input = sc.next().trim();
            if(!input.matches("[1-9]\\d{9}")){
                System.out.println("phone number should have 10 digits and the first digit should not be zero");
            }else {
                return input;
            }
        }
    }
    private static String getEmailInput(Scanner sc){//Checks if the given email input is valid or not
        while (true){
            String input = sc.next().trim();
            if(!input.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9._]+\\.[A-Za-z]{2,6}$")){
                System.out.println("Invalid email address");
            }else{
                return input;
            }
        }
    }
}
