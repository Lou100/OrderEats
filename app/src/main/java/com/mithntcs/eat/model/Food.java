package com.mithntcs.eat.model;

/**
 * Created by Mithlesh Kumar Sharma on 15,March,2020
 * NTCS Company
 */
public class Food {

    private String Name;
    private String Image;
    private String Discription;
    private String Price;
    private String Discount;
    private String MenuID;

    public Food() {
    }

    public Food(String name, String image, String discription, String price, String discount, String menuID) {
        Name = name;
        Image = image;
        Discription = discription;
        Price = price;
        Discount = discount;
        MenuID = menuID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getMenuID() {
        return MenuID;
    }

    public void setMenuID(String menuID) {
        MenuID = menuID;
    }
}
