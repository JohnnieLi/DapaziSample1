package com.example.johnnie.dapazisample1.model;

/**
 * Created by Johnnie on 2016-08-20.
 */
public class DealModel {

    private long dealId;
    private String category;
    private String name;
    private String address;
    private String information;
    private String FAQ;




    public long getDealId() {
        return dealId;
    }

    public void setDealId(long dealId) {
        this.dealId = dealId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getFAQ() {
        return FAQ;
    }

    public void setFAQ(String FAQ) {
        this.FAQ = FAQ;
    }
}
