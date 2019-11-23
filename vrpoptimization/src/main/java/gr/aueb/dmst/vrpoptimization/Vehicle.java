/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.dmst.vrpoptimization;

import java.util.ArrayList;

/**
 *
 * @author team7
 */
public class Vehicle {
    double available_weight;
    int sum_dist;
    ArrayList <Route> routes;
        
        public Vehicle() 
        {
            routes = new ArrayList();
            available_weight = 3000;
            sum_dist = 0;
            
        }
    
    
    
    
}
