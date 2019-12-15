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
public class Vrp {

    double[][] distanceMatrix;
    ArrayList<Node> allNodes;
    ArrayList<Node> customers;
    Random ran;
    Node depot;
    int numberOfCustomers;
    int capacity;
    Solution bestSolutionThroughTabuSearch;

    public Vrp(int totalCustomers, int cap) {
        numberOfCustomers = totalCustomers;
        capacity = cap;
        ran = new Random(1);
    }

    void GenerateNetworkRandomly() {
        CreateAllNodesAndCustomerLists(numberOfCustomers);
        CalculateDistanceMatrix();
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

    void Solve() {

        Solution s = new Solution();

        ApplyNearestNeighborMethod(s);
        for(int i = 0 ; i < 1000; i++){
        SolutionDrawer.drawRoutes(allNodes, s, Integer.toString(i));
        }
       // TabuSearch(s);
    }

    private void SetRoutedFlagToFalseForAllCustomers() {
        for (int i = 0; i < customers.size(); i++) {
            customers.get(i).isRouted = false;
        }
    }

    private void ApplyNearestNeighborMethod(Solution solution) {

        boolean modelIsFeasible = true;
        ArrayList<Route> routeList = solution.routes;

        SetRoutedFlagToFalseForAllCustomers();

        //Q - How many insertions? A - Equal to the number of customers! Thus for i = 0 -> customers.size()
        for (int insertions = 0; insertions < customers.size(); /* the insertions will be updated in the for loop */) {
            //A. Insertion Identification
            CustomerInsertion bestInsertion = new CustomerInsertion();
            bestInsertion.cost = Double.MAX_VALUE;
            Route lastRoute = GetLastRoute(routeList);
            if (lastRoute != null) {
                IdentifyBestInsertion_NN(bestInsertion, lastRoute);
            }
            //B. Insertion Application
            //Feasible insertion was identified
            if (bestInsertion.cost < Double.MAX_VALUE) {
                ApplyCustomerInsertion(bestInsertion, solution);
                insertions++;
            } //C. If no insertion was feasible
            else {
                //C1. There is a customer with demand larger than capacity -> Infeasibility
                if (lastRoute != null && lastRoute.nodes.size() == 2) {
                    modelIsFeasible = false;
                    break;
                } else {
                    CreateAndPushAnEmptyRouteInTheSolution(solution);
                }
            }
        }

        if (modelIsFeasible == false) {
            //TODO
        }
    }

    private Route GetLastRoute(ArrayList<Route> routeList) {
        if (routeList.isEmpty()) {
            return null;
        } else {
            return routeList.get(routeList.size() - 1);
        }
    }

    private void CreateAndPushAnEmptyRouteInTheSolution(Solution currentSolution) {
        Route rt = new Route(capacity);
        rt.nodes.add(depot);
        rt.nodes.add(depot);
        currentSolution.routes.add(rt);
    }

    private void ApplyCustomerInsertion(CustomerInsertion insertion, Solution solution) {
        Node insertedCustomer = insertion.customer;
        Route route = insertion.insertionRoute;

        route.nodes.add(route.nodes.size() - 1, insertedCustomer);

        Node beforeInserted = route.nodes.get(route.nodes.size() - 3);

        double costAdded = distanceMatrix[beforeInserted.ID][insertedCustomer.ID] + distanceMatrix[insertedCustomer.ID][depot.ID];
        double costRemoved = distanceMatrix[beforeInserted.ID][depot.ID];

        route.cost = route.cost + (costAdded - costRemoved);
        route.load = route.load + insertedCustomer.demand;
        solution.cost = solution.cost + (costAdded - costRemoved);

        insertedCustomer.isRouted = true;
    }

    private void IdentifyBestInsertion_NN(CustomerInsertion bestInsertion, Route lastRoute) {
        for (int j = 0; j < customers.size(); j++) {
            // The examined node is called candidate
            Node candidate = customers.get(j);
            // if this candidate has not been pushed in the solution
            if (candidate.isRouted == false) {
                if (lastRoute.load + candidate.demand <= lastRoute.capacity) {
                    ArrayList<Node> nodeSequence = lastRoute.nodes;
                    Node lastCustomerInTheRoute = nodeSequence.get(nodeSequence.size() - 2);

                    double trialCost = distanceMatrix[lastCustomerInTheRoute.ID][candidate.ID];

                    if (trialCost < bestInsertion.cost) {
                        bestInsertion.customer = candidate;
                        bestInsertion.insertionRoute = lastRoute;
                        bestInsertion.cost = trialCost;
                    }
                }
            }
        }
    }


    private double CalculateCostSol(Solution sol)
    {
        double totalCost = 0;

        for (int i = 0; i < sol.routes.size(); i++)
        {
            Route rt = sol.routes.get(i);

            for (int j = 0; j < rt.nodes.size() - 1; j++) {
                Node A = rt.nodes.get(j);
                Node B = rt.nodes.get(j + 1);
                // /35 * 60 gia metatroph se lepta
                // + (15 * nodes.size()) gia na metraei kai to xrono ekfortwshs

                totalCost += distanceMatrix[A.ID][B.ID];
            }
        }
        
        //array list me 25 theseis, me ta lepta kathe forthgou
        //epilegw to max sto telos

        return totalCost;

    }


}
