/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example;

import java.io.PrintWriter;
import javax.ejb.Local;

/**
 *
 * @author subha
 */
@Local
public interface ActNiceBeanLocal {
    boolean findDetails( Actor actor);
    void findCommonMovie( PrintWriter out );
    void publishData(PrintWriter out);
    void findIMDBNumber(Actor actor);
    void findListOfMovies(Actor actor);
    
}