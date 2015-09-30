package com.sky.pinkvelocity.warehouse.app;

/**
 * Created by hac10 on 30/09/15.
 */

import java.util.List;


public class Customer {
    private String title;
    private String firstName;
    private String lastName;
    private String usrName;
    private String email;

    private String address1;
    private String address2;
    private String town;
    private String county;
    private String postcode;

    private boolean emailNotifications;
    private boolean smsNotifications;
    private boolean emailOffers;
    private boolean smsOffers;
    private boolean postOffers;


    public String getFirstName() {
        return firstName;
    }

    public void setfirstName(String firstName) {
        this.firstName = firstName;
    }

    public List<Order> getOrders() {
        return null;
    }

    public void addOrder(Order order) {

    }

    public String getTitleAndFullName(){
        return title + " " + firstName + " " + lastName;
    }

    public String getAddress(){
        return address1 + " " + address2 + " " + town + " " + county + " " + postcode;
    }
}
