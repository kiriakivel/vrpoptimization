import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;


public class Vrp {
 	double BestSolutionCost;
     double[][] distanceMatrix;
     ArrayList<Node> allNodes;
     ArrayList<Node> customers;
     Map<Integer,Double> maptest  = new HashMap<Integer,Double>();
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
             cust.demand = 100*(1 + ran.nextInt(5));
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

     public Solution Solve() {

         Solution s = new Solution();

         ApplyNearestNeighborMethod(s);
         CalculateLatestRoute(s);
     	System.out.println(CalculateCostSol(s));
     	System.out.println(s.routes.size());

     //   for(int i = 0 ; i < 30; i++){
      //   SolutionDrawer.drawRoutes(allNodes, s, Integer.toString(i));

       //  }
        // TabucSearch(s);

 		return s;
     }





     private void CalculateLatestRoute(Solution s)
     {
 		int totalNodes = 0;
 		double highdis = 0;
 		int whereishigh = 0;
 		for (int i = 0 ; i < s.routes.size(); i++)
 		{
 			   Route rt = s.routes.get(i);
 			   double totalCost = 0;
 			   double distance = 0;
 			   totalNodes = rt.nodes.size();
 			   for(int j = 0 ; j < rt.nodes.size()-1; j++)
 			   {
 			    Node A = rt.nodes.get(j);
 		        Node B = rt.nodes.get(j + 1);
                 totalCost += distanceMatrix[A.ID][B.ID];
 			   }
 			    totalCost = (totalCost/35) ;
                 totalCost += (15/60) * totalNodes;
 		        distance = totalCost;
 			    maptest.put(i,distance);
 			    if (highdis < distance)
 			      {
 			          highdis = distance;
 			        	whereishigh = i;
 				}
 		   }
 		double check = 0;
 		   for (Map.Entry<Integer,Double> entry : maptest.entrySet()){
 		               System.out.println("Key = " + entry.getKey() +      ", Value = " + entry.getValue());
 		               check+= entry.getValue();
 				}
 				System.out.println("Longest distance route is gonna be route with ID " + whereishigh + " with estimated time : " + highdis);
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
 					if(routeList.size() < 25){
                     CreateAndPushAnEmptyRouteInTheSolution(solution);
 				}
 				else{
 					System.out.println("Truck limit has been reached");
 					modelIsFeasible = false;
 					break;
 				}
                 }
             }
         }

         if (modelIsFeasible == false) {
 			System.out.println("The demand is larger than capacity,the model cannot be feasible");
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
         int totalNodes = 0;

         for (int i = 0; i < sol.routes.size(); i++)
         {
             Route rt = sol.routes.get(i);
             totalNodes += rt.nodes.size();

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
         totalCost = (totalCost/35) ;
         totalCost += (15/60) * totalNodes ;


         System.out.println(totalCost);

         return totalCost;

     }
 	public void findBestRelocationMove(RelocationMove rm, Solution s) {
		double bestMoveCost = Double.MAX_VALUE;
		for (int from = 0; from < s.routes.size(); from++)
 		{

 			//to route for
 			for (int to = 0; to< s.routes.size(); to++)
 			{
 				//for (int relToIndex = 1; relToIndex < s.routes.get(to).nodes.size() - 1; relToIndex++)
 				//{
 				for (int relFromIndex = 1; relFromIndex < s.routes.get(from).nodes.size() - 1; relFromIndex++)
 				{
 					//Node A is the predecessor of B
 					Node A = s.routes.get(from).nodes.get(relFromIndex - 1);

 					//Node B is the relocated node
 					Node B = s.routes.get(from).nodes.get(relFromIndex);

 					//Node C is the successor of B
 					Node C = s.routes.get(from).nodes.get(relFromIndex + 1);

 					//We will iterate through all possible re-insertion positions for B
 					for (int afterToInd = 0; afterToInd < s.routes.get(to).nodes.size() -1; afterToInd ++)
 					{
 						// Why do we have to write this line?
 						// This line has to do with the nature of the 1-0 relocation
 						// If afterInd == relIndex -> this would mean the solution remains unaffected
 						// If afterInd == relIndex - 1 -> this would mean the solution remains unaffected
 						if ((afterToInd != relFromIndex && afterToInd != relFromIndex - 1)||from != to)
 						{
 							//Node F the node after which B is going to be reinserted
 							Node F = s.routes.get(to).nodes.get(afterToInd);

 							//Node G the successor of F
 							Node G = s.routes.get(to).nodes.get(afterToInd + 1);

 							//The arcs A-B, B-C, and F-G break
 							double costRemovedFrom = distanceMatrix[A.ID][B.ID] + distanceMatrix[B.ID][C.ID];
 							double costRemovedTo = distanceMatrix[F.ID][G.ID];
 							//double costRemoved = costRemoved1 + costRemoved2;

 							//The arcs A-C, F-B and B-G are created
 							double costAddedFrom = distanceMatrix[A.ID][C.ID];
 							double costAddedTo  = distanceMatrix[F.ID][B.ID] + distanceMatrix[B.ID][G.ID];
 							//double costAdded = costAdded1 + costAdded2;

 							//This is the cost of the move, or in other words
 							//the change that this move will cause if applied to the current solution
 							//double moveCost = costAdded - costRemoved;
 							double moveCostFrom = costAddedFrom - costRemovedFrom;
 							double moveCostTo = costAddedTo - costRemovedTo;

 							//If this move is the best found so far
 							double moveCost = moveCostFrom+moveCostTo;
 							if ((moveCost < bestMoveCost)&&(from == to || (s.routes.get(to).load + s.routes.get(from).nodes.get(relFromIndex).demand<=s.routes.get(to).capacity)))
 							{
 								//set the best cost equal to the cost of this solution
 								bestMoveCost = moveCost;

 								//store its characteristics
 								rm.positionOfRelocated = relFromIndex;
 								rm.positionToBeInserted = afterToInd;
 								rm.moveCostTo = moveCostTo;
 								rm.moveCostFrom = moveCostFrom;
 								rm.fromRoute = from;
 								rm.toRoute = to;
 								rm.moveCost = moveCost;
 								if (from != to) {
 									rm.newLoadFrom = s.routes.get(from).load - s.routes.get(from).nodes.get(relFromIndex).demand;
 									rm.newLoadTo = s.routes.get(to).load + s.routes.get(from).nodes.get(relFromIndex).demand;
 								} else {
 									rm.newLoadFrom = s.routes.get(from).load;
 									rm.newLoadTo = s.routes.get(to).load;
 								}

 									//System.out.println("From route: " + rm.fromRoute + ", To Route: " + rm.toRoute + ", New Load From:" + rm.newLoadFrom + ", New Load To:" + rm.newLoadTo);
 							}


 						}



 					}
 				}

 			}
 		}
 	}
 	public void applyRelocationMove(RelocationMove rm, Solution s)
 	{

 		Node relocatedNode = s.routes.get(rm.fromRoute).nodes.get(rm.positionOfRelocated);


 		s.routes.get(rm.fromRoute).nodes.remove(rm.positionOfRelocated);


 		if (((rm.positionToBeInserted < rm.positionOfRelocated) && (rm.toRoute == rm.fromRoute))||(rm.toRoute!=rm.fromRoute))
 		{
 			s.routes.get(rm.toRoute).nodes.add(rm.positionToBeInserted + 1, relocatedNode);
 		}

 		else
 		{
 			s.routes.get(rm.toRoute).nodes.add(rm.positionToBeInserted, relocatedNode);
 		}


 		s.cost = s.cost + rm.moveCost;
 		s.routes.get(rm.toRoute).cost = s.routes.get(rm.toRoute).cost + rm.moveCostTo;
 		s.routes.get(rm.fromRoute).cost = s.routes.get(rm.fromRoute).cost + rm.moveCostFrom;
 		if  (rm.toRoute != rm.fromRoute) {
 			s.routes.get(rm.toRoute).load = rm.newLoadTo;
 			s.routes.get(rm.fromRoute).load = rm.newLoadFrom;
 		}
 		else {
 			s.routes.get(rm.toRoute).load = rm.newLoadTo;
 		}

 	}

 	public void initializeRelocationMove(RelocationMove rm) {
 		rm.positionOfRelocated = -1;
 		rm.positionToBeInserted = -1;
 		rm.fromRoute = 0;
 		rm.toRoute = 0;
 		rm.moveCostFrom = Double.MAX_VALUE;
 		rm.moveCostTo = Double.MAX_VALUE;
 		rm.moveCostFrom = Double.MAX_VALUE;
 	}

 	public Solution operateLocalSearch(Solution s) {
 		boolean terminationCondition = false;

 		//this is a counter for holding the local search iterator
 		int localSearchIterator = 0;

 		//Here we apply the best relocation move local search scheme
 		//This is an object for holding the best relocation move that can be applied to the candidate solution
 		RelocationMove rm = new RelocationMove();
 		initializeRelocationMove(rm);
 		while (terminationCondition == false)
 		{

 			findBestRelocationMove(rm, s);

 			// If rm (the identified best relocation move) is a cost improving move, or in other words
 			// if the current solution is not a local optimum
 			if (rm.moveCost < 0)
 			{
 				//This is a function applying the relocation move rm to the candidate solution
 				applyRelocationMove(rm, s);
 				localSearchIterator = localSearchIterator + 1;

 			}
 			else
 			{
 				//if no cost improving relocation move was found,
 				//or in other words if the current solution is a local optimum
 				//terminate the local search algorithm
 				terminationCondition = true;
 			}
 		}
 		CalculateLatestRoute(s);
     	System.out.println(CalculateCostSol(s));
     	System.out.println(s.routes.size());
 		return s;

 	}
 }


