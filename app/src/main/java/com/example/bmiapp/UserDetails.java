package com.example.bmiapp;

public class UserDetails {
    String name,email,weight,height;

    UserDetails(String name,String email,String weight,String height){
        this.name=name;
        this.email=email;
        this.weight=weight;
        this.height=height;
    }
    UserDetails(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
