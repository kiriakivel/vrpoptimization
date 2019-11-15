/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.dmst.binpackingoptimization;

/**
 *
 * @author kyriaki
 */
public class BinPackingMain {
    
    /*
      sample code
    */
    public void CreateAllNodesAndServicePointLists() {
    //Create the list with the service points
        servicePoints = new ArrayList();
        Random ran = new Random(1);
        for (int i = 0 ; i < 200; i++)
        {
        Node sp = new Node();
        sp.x = ran.nextInt(100);
        sp.y = ran.nextInt(100);
        sp.demand = 100*(1 + ran.nextInt(5));
        sp.serviceTime = 0.25;
        sp.add(cust);
        }
        //Build the allNodes array and the corresponding distance matrix
        allNodes = new ArrayList();
        depot = new Node();
        depot.x = 50;
        depot.y = 50;
        depot.demand = 0;
        allNodes.add(depot);
        for (int i = 0 ; i < servicePoints.size(); i++)
        {
        Node cust = servicePoints.get(i);
        allNodes.add(cust);
        }

        for (int i = 0 ; i < allNodes.size(); i++)
        {
        Node nd = allNodes.get(i);
        nd.ID = i;
        }

    } 
}
