package com.customers.grocerystoresforcustomer.Model;

public class Shopper_Details {
    String id;
    String ShopName;
    String shoppersname;
    String ShopperEmail;
    String ShopperContact;
    String ShopAddress;
    String Shoplandmark;
    String Shopcity;
    String Shopsubcity;
    String ShopPincode;
    String imageURL;
    String status;

    public Shopper_Details() {
    }

    public Shopper_Details(String id, String shopName, String shoppersname,
                           String shopperEmail, String shopperContact, String shopAddress,
                           String shoplandmark, String shopcity, String Shopsubcity, String shopPincode, String imageURL,
                           String status) {
        this.id = id;
        ShopName = shopName;
        this.shoppersname = shoppersname;
        ShopperEmail = shopperEmail;
        ShopperContact = shopperContact;
        ShopAddress = shopAddress;
        Shoplandmark = shoplandmark;
        Shopcity = shopcity;
        this.Shopsubcity = Shopsubcity;
        ShopPincode = shopPincode;
        this.imageURL = imageURL;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getShoppersname() {
        return shoppersname;
    }

    public void setShoppersname(String shoppersname) {
        this.shoppersname = shoppersname;
    }

    public String getShopperEmail() {
        return ShopperEmail;
    }

    public void setShopperEmail(String shopperEmail) {
        ShopperEmail = shopperEmail;
    }

    public String getShopperContact() {
        return ShopperContact;
    }

    public void setShopperContact(String shopperContact) {
        ShopperContact = shopperContact;
    }

    public String getShopAddress() {
        return ShopAddress;
    }

    public void setShopAddress(String shopAddress) {
        ShopAddress = shopAddress;
    }

    public String getShoplandmark() {
        return Shoplandmark;
    }

    public void setShoplandmark(String shoplandmark) {
        Shoplandmark = shoplandmark;
    }

    public String getShopcity() {
        return Shopcity;
    }

    public void setShopcity(String shopcity) {
        Shopcity = shopcity;
    }

    public String getShopPincode() {
        return ShopPincode;
    }

    public void setShopPincode(String shopPincode) {
        ShopPincode = shopPincode;
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

    public String getShopsubcity() {
        return Shopsubcity;
    }

    public void setShopsubcity(String shopsubcity) {
        Shopsubcity = shopsubcity;
    }
}
