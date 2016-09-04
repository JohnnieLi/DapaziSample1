package com.example.johnnie.ottawainfo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Johnnie on 2016-08-20.
 */
public class DealerModel implements Serializable{

    private long dealId;
    private String category;
    private String name;
    private String address;
    private String information;
    private String FAQ;
    private String imageUri;


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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public String toString() {
        return  name
                +"\n"+information
                +"\nAddress: "+address
                +"\nCall: "+FAQ;
    }


}
