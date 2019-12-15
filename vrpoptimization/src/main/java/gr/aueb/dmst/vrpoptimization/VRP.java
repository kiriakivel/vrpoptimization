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
public class VRP {

    double[][] distanceMatrix;
    ArrayList<Node> allNodes;
    ArrayList<Node> customers;
    Random ran;
    Node depot;
    int numberOfCustomers;
    int capacity;
    

    public VRP(int totalCustomers, int cap) {
        numberOfCustomers = totalCustomers;
        capacity = cap;
        ran = new Random(1);
    }

    void GenerateNetworkRandomly() {
        CreateAllNodesAndCustomerLists(numberOfCustomers);
        //CalculateDistanceMatrix();
    }

    public void CreateAllNodesAndCustomerLists(int numberOfCustomers) {
        //Create the list with the customers
        customers = new ArrayList();
        
        
        for (int i = 0; i < numberOfCustomers; i++) {
            Node cust = new Node();

            cust.x = ran.nextInt(100);
            cust.y = ran.nextInt(100);
            cust.demand = 10 + ran.nextInt(20);

            customers.add(cust);
        }

        //Build the allNodes array and the corresponding distance matrix
        allNodes = new ArrayList();

        depot = new Node();
        depot.x = 50;
        depot.y = 50;
        depot.demand = 0;
        allNodes.add(depot);
        for (int i = 0; i < customers.size(); i++) {
            Node cust = customers.get(i);
            allNodes.add(cust);
        }    
        

        for (int i = 0; i < allNodes.size(); i++) {
            Node nd = allNodes.get(i);
            nd.ID = i;
        }
       
    }
    public void CalculateDistanceMatrix() {

        distanceMatrix = new double[allNodes.size()][allNodes.size()];
        for (int i = 0; i < allNodes.size(); i++) {
            Node from = allNodes.get(i);

            for (int j = 0; j < allNodes.size(); j++) {
                Node to = allNodes.get(j);

                double Delta_x = (from.x - to.x);
                double Delta_y = (from.y - to.y);
                double distance = Math.sqrt((Delta_x * Delta_x) + (Delta_y * Delta_y));

                distance = Math.round(distance);

                distanceMatrix[i][j] = distance;
            }
        }
    }

}