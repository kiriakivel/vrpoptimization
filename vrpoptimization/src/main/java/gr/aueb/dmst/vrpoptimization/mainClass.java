
package gr.aueb.dmst.vrpoptimization;

import java.util.ArrayList;
import java.util.Random;


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
     }

}
