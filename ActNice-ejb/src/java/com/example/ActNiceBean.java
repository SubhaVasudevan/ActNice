/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import org.json.JSONObject;
/**
 *
 * @author subha
 */
@Stateful
public class ActNiceBean implements ActNiceBeanLocal {

    //@PersistenceContext
    //private EntityManager em;
    
    private List<Actor> actors;
    HashSet<Movie> movieList ;
    Hashtable<String, Actor> actorTable;
    
    @PostConstruct
    private void init( ) {
        actors = new ArrayList<>( );
        movieList = new HashSet<Movie>();
        actorTable = new Hashtable<String, Actor>();
    }
        
        
    /*
     * to find the IMDB number of a certain actor using the name
     * add it as a property of the actor
     */
    @Override
    public void findIMDBNumber(Actor actor) {
        BufferedReader in = null;
        
        //link to find the IMDB number
    	try {
            URL oracle = new URL("http://www.imdb.com/xml/find?json=1&nr=1&nm=on&q=" + actor.getName());
            URLConnection openConnection = oracle.openConnection();	
            openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            in = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
    	}catch(Exception e){
            System.out.println("Could not read from URL");
            findIMDBNumber(actor);
    	}
    
        String inputLine;

        try {
            //find the required number and the correct name
            while ((inputLine = in.readLine()) != null) {
                if(inputLine.contains("id")) {
                    int index = inputLine.indexOf("id");
                    String IMDBNumber = inputLine.substring(index + 5, index + 14) ;
                    index = inputLine.indexOf("\"name\":\"");
                    int endIndex = inputLine.indexOf("\",\"description");
                    String name = inputLine.substring(index + 8, endIndex);
                    actor.setIMDBId(IMDBNumber);
                    actor.setImName(name);
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ActNiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
     *finding the list of movies by a specific actor
     */
    @Override
    public void findListOfMovies(Actor actor){
        BufferedReader in = null;
        
        //using IMDB URL to access the link
    	try {
            URL oracle = new URL("http://www.imdb.com/name/" + actor.getIMDBId() +"/");
            URLConnection openConnection = oracle.openConnection();	
            openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            in = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
    		
    	}catch(Exception e){
            System.out.println("Could not read from URL");
            findListOfMovies(actor);
    	}
    
        String inputLine;

        
        //in this case the data is returned as HTML
        //parsing HTML to retrieve the required information
        boolean image = false;
        try {
            //find the required number
            while ((inputLine = in.readLine()) != null) {
                
                //find the image of the actor
                if(!image){
                    if(inputLine.indexOf("<img id=\"name-poster\"") >= 0) {
                        in.readLine();
                        in.readLine();
                        in.readLine();
                        in.readLine();
                        inputLine = in.readLine();
                        int index = inputLine.indexOf("src=\"");
                        int endIndex = inputLine.indexOf(".jpg");
                        String url = inputLine.substring(index+5, endIndex+4);
                        actor.setImageURL(url);
                    }
                }
                if(inputLine.contains("filmo-row")) {
                    int index = inputLine.indexOf("tt");
                    String movieNumber = inputLine.substring(index, index + 9) ;
                    actor.addMovieId(movieNumber);
                }
            }
            in.close();
        } catch (Exception ex) {
            Logger.getLogger(ActNiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
    
    /*
    * Finding the follwoing details about the actor
    * IMDB number
    * Name
    * List of Movies
    * Profile Picture
    */
    @Override
    public boolean findDetails(Actor product) {
        try {   
            //if already present in the hashtable fetch from it
            if(actorTable.containsKey(product.getName())) {
                Actor temp = actorTable.get(product.getName());
                product.setIMDBId(temp.getIMDBId());
                product.setImageURL(temp.getImageURL());
                product.setMovies(temp.getMovies());
                product.setImName(temp.getImName());
            }else{
            //search for the IMDB number of the actor     
                findIMDBNumber(product);   
                findListOfMovies(product);
                actorTable.put(product.getName(), product);
            }
        } catch (Exception ex) {
            findDetails(product);
            Logger.getLogger(ActNiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        actors.add(product);
        return true;
    }

    /*
    * Finding the list of common movies between the actors
    * fetching details bout the movie from the IMDB website
    * mulitple threads are created to fetch teh movies
    */
    @Override
    public void findCommonMovie( PrintWriter out ) {
        
        //print the names and images of the artists
        out.println("<h3>" + actors.get(0).getImName() + "&nbsp;&&nbsp;" + actors.get(1).getImName() + "</h3><br>");
        for( Actor product : actors ) {
            out.println("<img src=\"" + product.getImageURL() + "\" alt=\""+ product.getName()+"\" width=\"120\" height=\"170\"> &nbsp;");
        }
        
        //create threads to find details about the movie
        Set<String> commonList = new HashSet<String>(actors.get(0).getMovies());
        commonList.retainAll(actors.get(1).getMovies());
        MyThread[] threads = new MyThread[commonList.size()];
        int threadNo = 0;
        for(String movieName : commonList) {
            threads[threadNo] = new MyThread(movieName);
            threads[threadNo].start();
            threadNo++;
        }
   
        //make sure the data from all the threads is collected
        for(int i = 0 ; i < threadNo ; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(ActNiceBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    /*
    * publishing data to the UI
    * publishes - name of the movie
    *           - year of release
    *           - poster
    *           - box office status
    *           - Link to the movie page
    */
    @Override
    public void publishData(PrintWriter out) {
        List<Movie> list = new ArrayList<Movie>(movieList);
        
        //sor the list by year of release
        Collections.sort(list);
        out.println("<br><br>");
        if(list.size() > 0) {
            out.println("The common movies are <br>");
        } else {
            out.println("They did not do any movies together");
        }
        out.println("</div>");
        
        //for each movie
        for(Movie nm : list) {
            out.println("<p><a href=\"http://www.imdb.com/title/" + nm.getIMDBName()+"/\"><img src=\"" + nm.getPoster()+"\" alt=\""+ nm.getName()+"\" width=\"60\" height=\"85\"></img></a>");
            out.println("<a href=\"http://www.imdb.com/title/" + nm.getIMDBName()+"/\"><font size=\"6\">"+ nm.getName() + "(" + nm.getYear() + ")</font></a></p>");
            out.println("<div  style=\"width:" + (int)(nm.getRating()*100) + "px;height:20px;background-color:#00CED1;border:1px solid #000;\" align =\"right\">"
                    +"<font size =\"2\"> IMDB Rating " + nm.getRating() + "</font></div><br>" );
        }
        actors.clear();
        movieList.clear();
      
    }
   
    /*
    * This is a helper funciton to read all data from the 
    * input stream over a URL connection
    */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    
    /*
    * A runnable class to fetch the details of each movie
    * mulitpe threads are created at the same time to read data from URLs 
    * and write them back to hashtable
    * No synchronization mechanism is necessary since hashtbale handles
    * synchronization by default
    */
    class MyThread extends Thread {
    
        String movieName;
        public MyThread(String name) {
            movieName = name;
        }

        @Override
        public void run() {
           JSONObject json;
            URL oracle = null;
            BufferedReader in = null;
            try {
                oracle = new URL("http://www.omdbapi.com/?i="+movieName+"&plot=short&r=json");
                URLConnection openConnection = oracle.openConnection();	
                openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                in = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
                String jsonText = readAll(in);
                json = new JSONObject(jsonText);
                in.close();
                String rating = (String)json.get("Rated");
        
                if(rating.equals("PG-13") || rating.equals("G") || rating.equals("PG") || rating.equals("R") || rating.equals("NC-17")){
                    Movie nm = new Movie();
                    nm.setIMDBName(movieName);
                    nm.setName((String)json.get("Title"));
                    nm.setYear((String)json.get("Year"));
                    nm.setRating((String)json.get("imdbRating"));
                    nm.setPoster((String)json.get("Poster"));
                    movieList.add(nm);
                }
            }catch(Exception e){
                run();
                System.out.println("fdgdfgffhfgh");
            }
    	}
    }
}

