package com.shaheed.klassify.models;

/**
 * Created by Shaheed on 1/31/2015.
 * Shaheed Ahmed Dewan Sagar
 * Ahsanullah University of Science & Technology
 * Email : sdewan64@gmail.com
 */
public class Ads {

    private String adId;
    private String adTitle;
    private String adDescription;
    private String adOwner;
    private String adType;
    private String adCategory;
    private String adSubCategory;
    private String adThumbImageLink;
    private String adPrice;
    private String adPhone;
    private String adEmail;

    private String adView = "0";

    public Ads(){}

    public Ads(String adId,String adTitle,String adDescription,String adOwner,String adType,String adCategory,String adSubCategory,String adThumbImageLink,String adPrice,String adPhone,String adEmail){
        this.adId = adId;
        this.adTitle = adTitle;
        this.adDescription = adDescription;
        this.adOwner = adOwner;
        this.adType = adType;
        this.adCategory = adCategory;
        this.adSubCategory = adSubCategory;
        this.adThumbImageLink = adThumbImageLink;
        this.adPrice = adPrice;
        this.adPhone = adPhone;
        this.adEmail = adEmail;
    }

    public void setAdId(String adId){
        this.adId = adId;
    }

    public void setAdTitle(String adTitle){
        this.adTitle = adTitle;
    }

    public void setAdDescription(String adDescription){
        this.adDescription = adDescription;
    }

    public void setAdOwner(String adOwner){
        this.adOwner = adOwner;
    }

    public void setAdType(String adType){
        this.adType = adType;
    }

    public void setAdCategory(String adCategory){
        this.adCategory = adCategory;
    }

    public void setAdSubCategory(String adSubCategory){
        this.adSubCategory = adSubCategory;
    }

    public void setAdThumbImageLink(String adThumbImageLink){
        this.adThumbImageLink = adThumbImageLink;
    }

    public void setAdPrice(String adPrice){
        this.adPrice = adPrice;
    }

    public void setAdView(String adView){
        this.adView = adView;
    }



    public String getAdId(){
        return this.adId;
    }

    public String getAdTitle(){
        return this.adTitle;
    }

    public String getAdDescription(){
        return this.adDescription;
    }

    public String getAdOwner(){
        return this.adOwner;
    }

    public String getAdType(){
        return this.adType;
    }

    public String getAdCategory(){
        return this.adCategory;
    }

    public String getAdSubCategory(){
        return this.adSubCategory;
    }

    public String getAdThumbImageLink(){
        return this.adThumbImageLink;
    }

    public String getAdPrice(){
        return this.adPrice;
    }

    public String getAdPhone(){
        return this.adPhone;
    }

    public String getAdEmail(){
        return this.adEmail;
    }

    public String getAdView(){
        return this.adView;
    }

}
