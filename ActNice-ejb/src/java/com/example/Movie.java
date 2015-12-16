/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

/**
 *
 * @author Subha
 */
public class Movie implements Comparable<Movie> {
    String name;
    int year;
    Float IMDBrating;
    String poster = null;
    String IMDBName;
    
    public void setName(String name) {
        this.name = name;
    } 
    
    public void setYear(String year) {
        this.year = Integer.parseInt(year);
    }
    
    public void setRating(String rating) {
        this.IMDBrating = Float.parseFloat(rating);
    }
    
    public String getName() {
        return name;
    }
    
    public int getYear() {
        return year;
    }
    
    public float getRating() {
        return IMDBrating;
    }
    
    public void setPoster(String poster) {
        this.poster = poster;
    }
    
    public String getPoster(){
        return poster;
    }

    @Override
    public int compareTo(Movie o) {
        return o.getYear() - year;
    }
    
    public void setIMDBName(String name){
        this.IMDBName = name;
    }
    
    public String getIMDBName(){
        return IMDBName;
    }
}
