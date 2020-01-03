
package gr.aueb.dmst.vrpoptimization;

import java.util.ArrayList;

/**
 *
 * @author mzaxa
 */
public class Route {
        
    ArrayList <Node> nodes = new ArrayList();
        double cost;
        double load;
        double capacity;
        
        public Route(double cap) 
        {
            cost = 0;
            nodes = new ArrayList();
            load = 0;
            capacity = cap;
        }
}
