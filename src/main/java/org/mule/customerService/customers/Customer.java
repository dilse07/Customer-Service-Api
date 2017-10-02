package org.mule.customerService.customers;

public class Customer {
	
    private String id;
    private String firstName;
    private String lastName;
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
    	this.firstName = (firstName != null) ? firstName : "";
    }
    
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = (lastName != null) ? lastName : "";
    }
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = (address != null) ? address : "";
    }
  
}
