package EmpManagementSystem;

import java.util.List;
import java.util.Objects;


public class Employee {

    private static int autoIncrementId = 100;
    public int empId;
    public String name;
    public String designation;
    public String phoneNumber;
    public double salary;
    public String email;
    Address address;
    List<WorkExperience> workHistory;
    public Employee(String name, String designation, String phoneNumber, Double salary, String email, Address address, List<WorkExperience> workHistory){//Initializes Employee object
        this.empId = autoIncrementId++;
        this.name = validateString(name, "name");
        this.designation = validateString(designation,"designation");
        this.phoneNumber = validatePhone(phoneNumber);
        this.salary = validateSalary(salary);
        this.email = validateEmail(email);
        this.address = Objects.requireNonNull(address, "Address should not be null");
        this.workHistory = workHistory;
    }
    private static String validateString(String value, String fieldName){//checks if given string input is valid or not allows only proper string
        if(!value.matches("[A-Za-z]+")){
            throw new IllegalArgumentException(fieldName + "should contain only alphabets");
        }
        return value;
    }
    private static String validatePhone(String phoneNumber){//checks if phone number is of 10 digits and whether the first digit is zero or not
        if(!phoneNumber.matches("[1-9]\\d{9}")){
            throw new IllegalArgumentException("phone number should have 10 digits and the first digit should not be zero");
        }
        return phoneNumber;
    }
    private static double validateSalary(double salary){//checks if salary is less than or equals to zero
        if(salary<0){
            throw new IllegalArgumentException("Salary should not be less than zero");
        }
        return salary;
    }
    private static String validateEmail(String email){//checks if email input is valid or not
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9._]+\\.[A-Za-z]{2,6}$";
        if(!email.matches(emailRegex)){
            throw new IllegalArgumentException("Invalid email address");
        }
        return email;
    }
    public int getEmpId(){//returns the incremented employee id
        return this.empId;
    }
    public String toString(){
        return String.format("%d, %s, %s, %s, %.2f, %s, %s, %s",empId,name,designation,phoneNumber,salary,email,address,workHistory==null?"None":workHistory);
    }
    public void show(String detail, Object value){
        switch (detail.toLowerCase()){
            case "name" -> this.name = (String) value;
            case "designation" -> this.designation = (String) value;
            case "phone number" -> this.phoneNumber = (String) value;
            case "salary" -> this.salary = (Double) value;
            case "email" -> this.email = (String) value;
            case "address" -> this.address = (Address)value;
            case "work experience" -> this.workHistory = (List<WorkExperience>) value;
            default -> throw new IllegalArgumentException("Not in the required fields"+ detail);
        }
    }
    public String getName() {
        return name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public double getSalary() {
        return salary;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public List<WorkExperience> getWorkHistory() {
        return workHistory;
    }

}


