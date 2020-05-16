package com.company;

import java.util.Date;

//BLUEPRINTS OF THE ITEM
public class Item {

    String productName;
    String productID;
    int quantity;
    Date boughtDate;


    public Item() {
        this.productName = "";
        this.productID = "";
        this.quantity = 0;
        this.boughtDate = new Date();
    }

    public Item(String productName, String productID, int quantity, Date boughtDate) {

        this.productName = productName;
        this.productID = productID;
        this.quantity = quantity;
        this.boughtDate = boughtDate;

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getBoughtDate() {
        return boughtDate;
    }

    public void setBoughtDate(Date boughtDate) {
        this.boughtDate = boughtDate;
    }

    public String toString() {
        return productID;
    }

    public Integer toInt() {
        return quantity;
    }
}
