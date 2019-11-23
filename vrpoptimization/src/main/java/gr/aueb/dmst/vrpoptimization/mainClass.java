/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.dmst.vrpoptimization;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author mzaxa
 */
public class mainClass {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        VRP vrp = new VRP(100, 50);
        vrp.GenerateNetworkRandomly();
        System.out.println("blue");
       // vrp.Solve();
    }
    
}
