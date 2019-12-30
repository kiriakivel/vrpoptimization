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
 * @author team7
 */
public class mainClass {


    /**
     * @param args the command line arguments
     */
     public static void main(String[] args) {
         // TODO code application logic here
         Vrp vrp = new Vrp(200, 3000);
         vrp.GenerateNetworkRandomly();
         //Greedy solution
         vrp.Solve();
         //Solution with Relocation Move Local Search type
         vrp.operateLocalSearch(vrp.Solve());
     }

}
