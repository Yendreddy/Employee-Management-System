package EmpManagementSystem;

public class WorkExperience {
    String companyName;
    String jobRole;
    public WorkExperience(String companyName, String jobRole){
        this.companyName = validateString(companyName,"company name");
        this.jobRole = validateString(jobRole,"job role");
    }
    private static String validateString(String value, String fieldName){//checks if given string input is valid or not
        if(!value.matches("[A-Za-z]+")){
            throw new IllegalArgumentException(fieldName + "should contain only alphabets");
        }
        return value;
    }
    public String toString(){
        return String.format("%s,%s",companyName,jobRole);
    }
    public String getCompanyName() {
        return companyName;
    }

    public String getJobRole() {
        return jobRole;
    }
}
