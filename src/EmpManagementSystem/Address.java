package EmpManagementSystem;
public class Address {
    String streetNumber;
    String streetName;
    String city;
    String state;
    String country;
    public Address(String streetNumber, String streetName, String city, String state, String country){
        this.streetNumber = streetNumber;
        this.streetName = validateString(streetName,"Street name");
        this.city = validateString(city,"city");
        this.state = validateString(state,"state");
        this.country = validateString(country,"country");
    }
    private static String validateString(String value, String fieldName){//checks if given string input is valid or not
        if(!value.matches("[A-Za-z]+")){
            throw new IllegalArgumentException(fieldName + "should contain only alphabets");
        }
        return value;
    }
    public String toString(){
        return String.format("%s, %s, %s, %s, %s",streetNumber,streetName,city,state,country);
    }
    public String getStreetNumber() {
        return streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }
}
