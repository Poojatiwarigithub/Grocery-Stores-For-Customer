package com.customers.grocerystoresforcustomer.Model;

public class Customersdetails {
    String id;
    String customerName;
    String customerEmailid;
    String customerContact;
    String customerCity;
    String customerarelocation;
    String imageURL;
    String status;

    public Customersdetails() {

    }

    public Customersdetails(String id, String customerName, String customerEmailid, String customerContact, String customerCity, String customerarelocation, String imageURL, String status) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmailid = customerEmailid;
        this.customerContact = customerContact;
        this.customerCity = customerCity;
        this.imageURL = imageURL;
        this.status = status;
        this.customerarelocation = customerarelocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmailid() {
        return customerEmailid;
    }

    public void setCustomerEmailid(String customerEmailid) {
        this.customerEmailid = customerEmailid;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerarelocation() {
        return customerarelocation;
    }

    public void setCustomerarelocation(String customerarelocation) {
        this.customerarelocation = customerarelocation;
    }
}
