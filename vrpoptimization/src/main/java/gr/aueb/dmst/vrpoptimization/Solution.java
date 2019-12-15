/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.dmst.vrpoptimization;

/**
 *
 * @author imakrigiannis
 */

import java.util.ArrayList;

/**
 *
 * @author mzaxa
 */
public class Solution {

        double cost;
        ArrayList <Route> routes;

        public Solution()
        {
            routes = new ArrayList();
            cost = 0;
        }
}
