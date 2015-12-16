/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Actor implements Serializable {
    String name;
    String IMDBid;
    Set<String> movies = new HashSet<String>();
    String imageURL;
    String IMName;
    
    public void setImName(String name) {
        this.IMName = name;
    }
    
    public String getImName() {
        return IMName;
    }
    
    public Actor(String name) {
        this.name = name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setIMDBId(String id) {
        this.IMDBid = id;
    }
    
    public String getIMDBId(){
        return IMDBid;
    }
    
    public void addMovieId( String movieName) {
        movies.add(movieName);
    }
    
    public Set<String> getMovies() {
        return movies;
    }
    
    public void setMovies(Set<String> movie) {
        this.movies = movie;
    }
    
    public void setImageURL(String url) {
        this.imageURL = url;
    }
    
    public String getImageURL() {
        return imageURL;
    }
    
}

