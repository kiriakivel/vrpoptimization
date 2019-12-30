import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class Vrp {
	double highdistance;
	double BestSolutionCost;
	static int whereishigh;
    double[][] distanceMatrix;
    ArrayList<Node> allNodes;
    ArrayList<Node> customers;
    Map<Integer,Double> maptest  = new HashMap<Integer,Double>();
    Random ran;
    Node depot;
    int numberOfCustomers;
    int capacity;


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
        customers = new ArrayList();
        for (int i = 0; i < numberOfCustomers; i++) {
            Node cust = new Node();

            cust.x = ran.nextInt(100);
            cust.y = ran.nextInt(100);
            cust.demand = 100*(1 + ran.nextInt(5));
            customers.add(cust);
        }
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

    public void Solve() {
	System.out.println(" **** Setting nodes and routes ****");
    Solution s = new Solution();
    ApplyNearestNeighborMethod(s);
 	 highdistance = CalculateLatestRoute(s);
    System.out.println("Longest route is gonna be route with ID " + whereishigh + " with time " +highdistance);
  	System.out.println("**** Applying Relocation Moves ****");
	boolean terminationCondition = false;
	int localSearchIterator = 0;
		RelocationMove rm = new RelocationMove();
		rm.positionOfRelocated = -1;
		rm.positionToBeInserted = -1;
		rm.fromRoute = 0;
		rm.toRoute = 0;
		rm.moveCostFrom = Double.MAX_VALUE;
		rm.moveCostTo = Double.MAX_VALUE;
		rm.moveCostFrom = Double.MAX_VALUE;
		int dis = 0;
		while (terminationCondition == false)
				{

				findBestRelocationMove(rm, s, distanceMatrix, 21);
				dis = dis +1;
				if (rm.moveCost < 0){
						applyRelocationMove(rm, s, distanceMatrix);
						localSearchIterator = localSearchIterator + 1;
						SolutionDrawer.drawRoutes(allNodes, s, "relocationmove" + Integer.toString(dis));
				}
				else
				{
				terminationCondition = true;
				}
		}
				//for (int j = 0; j<21; j++)
			//	{
			//		int vehicle = j+1;
				//	System.out.print("New Assignment to Vehicle " + vehicle + ": ");
				//	for (int k=0; k<s.routes.get(j).nodes.size(); k++)
				//	{
				//		System.out.print(s.routes.get(j).nodes.get(k).ID + "  ");
				//	}
				//	System.out.println("");
				//	System.out.println("Route Cost: " + s.routes.get(j).cost  + " - Route Load: " + s.routes.get(j).load);
					//System.out.println(" ");

				//}
				 double actcost = (s.cost);
				 actcost = (actcost/35);
				actcost += (15/60)*allNodes.size();
				double highone = (s.routes.get(whereishigh).cost);
				highone = (highone/35);
				highone += (15/60)*allNodes.size();
				if(highdistance < highone)
				{
					System.out.println(" Relocation move couldn't minimize the highest route ");

				}
				else
				{
				System.out.println("The highest route with ID " +whereishigh + "has been now : "+ highone);
				highdistance = highone;
			}
			actcost = (s.cost);
							 actcost = (actcost/35);
				actcost += (15/60)*allNodes.size();
				System.out.println("Total time to finish all routes : " + actcost);
				System.out.println("**** Applying SwapMove changing ****");
				   SwapMove sm = new SwapMove();
					int swap =0;
				     boolean terminal = false;
				     while(terminal == false)
				     {
					  sm.moveCost = Double.MAX_VALUE;
				     FindBestSwapMove(sm, s);
				     swap+=1;
				     if (sm.moveCost < 0) {

						ApplySwapMove(sm, s);
						SolutionDrawer.drawRoutes(allNodes, s, "swapmove" + Integer.toString(dis));
						}
						else
						{
							terminal = true;
						}
					}
				if(highdistance <= highone)
								{
									System.out.println(" SwapMove couldn't minimize the highest route ");
								}
								else
								{
								System.out.println("The highest route with ID " +whereishigh + "has been now : "+ highone);
								highdistance = highone;
			}

actcost = (s.cost);
				 actcost = (actcost/35);
				actcost += (15/60)*allNodes.size();
					System.out.println("Total time to finish all routes : " + actcost);
			System.out.println("**** Applying TwoOptMove ****");
				TwoOptMove topm = new TwoOptMove();
				int twos = 0;
				boolean twoterminal = false;
				while(twoterminal == false)
				{
					twos+=1;
					topm.moveCost = Double.MAX_VALUE;
					FindBestTwoOptMove(topm,s);
					if(topm.moveCost < 0)
					{
						ApplyTwoOptMove(topm,s);
						SolutionDrawer.drawRoutes(allNodes, s, "twooptmove" + Integer.toString(dis));
					}
					else
					{
						twoterminal = true;
					}

				}

				if(highdistance <= highone)
							{
					System.out.println(" TwoOptMove couldn't minimize the highest route ");
							}
					else
							{
					System.out.println("The highest route with ID " +whereishigh + "has been now : "+ highone);
					highdistance = highone;
			}
			actcost = (s.cost);
							 actcost = (actcost/35);
				actcost += (15/60)*allNodes.size();
					System.out.println("Total time to finish all routes : " + actcost);


  }





    private  double  CalculateLatestRoute(Solution s)
    {
		int totalNodes = 0;
		double highdis = 0;
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
		//   for (Map.Entry<Integer,Double> entry : maptest.entrySet()){
		  //            System.out.println("Key = " + entry.getKey() +      ", Value = " + entry.getValue());
		    //           check+= entry.getValue();
			//	}
		return highdis;
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

            Node candidate = customers.get(j);

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

        private void ApplySwapMove(SwapMove sm, Solution sol) {
	        if (sm.moveCost == Double.MAX_VALUE) {
	            return;
	        }

	        Route firstRoute = sol.routes.get(sm.firstRoutePosition);
	        Route secondRoute = sol.routes.get(sm.secondRoutePosition);

	        if (firstRoute == secondRoute) {
	            if (sm.firstNodePosition == sm.secondNodePosition - 1) {
	                Node A = firstRoute.nodes.get(sm.firstNodePosition);
	                Node B = firstRoute.nodes.get(sm.firstNodePosition + 1);

	                firstRoute.nodes.set(sm.firstNodePosition, B);
	                firstRoute.nodes.set(sm.firstNodePosition + 1, A);

	            } else {
	                Node A = firstRoute.nodes.get(sm.firstNodePosition);
	                Node B = firstRoute.nodes.get(sm.secondNodePosition);

	                firstRoute.nodes.set(sm.firstNodePosition, B);
	                firstRoute.nodes.set(sm.secondNodePosition, A);
	            }
	            firstRoute.cost = firstRoute.cost + sm.moveCost;
	        } else {
	            Node A = firstRoute.nodes.get(sm.firstNodePosition - 1);
	            Node B = firstRoute.nodes.get(sm.firstNodePosition);
	            Node C = firstRoute.nodes.get(sm.firstNodePosition + 1);

	            Node E = secondRoute.nodes.get(sm.secondNodePosition - 1);
	            Node F = secondRoute.nodes.get(sm.secondNodePosition);
	            Node G = secondRoute.nodes.get(sm.secondNodePosition + 1);

	            double costChangeFirstRoute = distanceMatrix[A.ID][F.ID] + distanceMatrix[F.ID][C.ID] - distanceMatrix[A.ID][B.ID] - distanceMatrix[B.ID][C.ID];
	            double costChangeSecondRoute = distanceMatrix[E.ID][B.ID] + distanceMatrix[B.ID][G.ID] - distanceMatrix[E.ID][F.ID] - distanceMatrix[F.ID][G.ID];

	            firstRoute.cost = firstRoute.cost + costChangeFirstRoute;
	            secondRoute.cost = secondRoute.cost + costChangeSecondRoute;

	            firstRoute.load = firstRoute.load + F.demand - B.demand;
	            secondRoute.load = secondRoute.load + B.demand - F.demand;

	            firstRoute.nodes.set(sm.firstNodePosition, F);
	            secondRoute.nodes.set(sm.secondNodePosition, B);

	        }

	        sol.cost = sol.cost + sm.moveCost;

    }


    	private void FindBestSwapMove(SwapMove sm, Solution sol) {
				ArrayList<Route> routes = sol.routes;
				for (int firstRouteIndex = 0; firstRouteIndex < routes.size(); firstRouteIndex++) {
					Route rt1 = routes.get(firstRouteIndex);
					for (int secondRouteIndex = firstRouteIndex; secondRouteIndex < routes.size(); secondRouteIndex++) {
						Route rt2 = routes.get(secondRouteIndex);
						for (int firstNodeIndex = 1; firstNodeIndex < rt1.nodes.size() - 1; firstNodeIndex++) {
							int startOfSecondNodeIndex = 1;
							if (rt1 == rt2) {
								startOfSecondNodeIndex = firstNodeIndex + 1;
							}
							for (int secondNodeIndex = startOfSecondNodeIndex; secondNodeIndex < rt2.nodes.size() - 1; secondNodeIndex++) {
								Node a1 = rt1.nodes.get(firstNodeIndex - 1);
								Node b1 = rt1.nodes.get(firstNodeIndex);
								Node c1 = rt1.nodes.get(firstNodeIndex + 1);

								Node a2 = rt2.nodes.get(secondNodeIndex - 1);
								Node b2 = rt2.nodes.get(secondNodeIndex);
								Node c2 = rt2.nodes.get(secondNodeIndex + 1);

								double moveCost = Double.MAX_VALUE;

								if (rt1 == rt2) // within route
								{
									if (firstNodeIndex == secondNodeIndex - 1) {
										double costRemoved = distanceMatrix[a1.ID][b1.ID] + distanceMatrix[b1.ID][b2.ID] + distanceMatrix[b2.ID][c2.ID];
										double costAdded = distanceMatrix[a1.ID][b2.ID] + distanceMatrix[b2.ID][b1.ID] + distanceMatrix[b1.ID][c2.ID];
										moveCost = costAdded - costRemoved;
		//                                      BuilArcList(arcsDaysCreated, a1.uid, b2.uid, p, b2.uid, b1.uid, b1.uid, c2.uid);
		//                                      BuilArcList(arcsDaysDeleted, a1.uid, b1.uid, p, b1.uid, b2.uid, b2.uid, c2.uid);


									} else {
										double costRemoved1 = distanceMatrix[a1.ID][b1.ID] + distanceMatrix[b1.ID][c1.ID];
										double costAdded1 = distanceMatrix[a1.ID][b2.ID] + distanceMatrix[b2.ID][c1.ID];

										double costRemoved2 = distanceMatrix[a2.ID][b2.ID] + distanceMatrix[b2.ID][c2.ID];
										double costAdded2 = distanceMatrix[a2.ID][b1.ID] + distanceMatrix[b1.ID][c2.ID];

										moveCost = costAdded1 + costAdded2 - (costRemoved1 + costRemoved2);


									}
								} else // between routes
								{
									//capacity constraints
									if (rt1.load - b1.demand + b2.demand > capacity) {
										continue;
									}
									if (rt2.load - b2.demand + b1.demand > capacity) {
										continue;
									}

									double costRemoved1 = distanceMatrix[a1.ID][b1.ID] + distanceMatrix[b1.ID][c1.ID];
									double costAdded1 = distanceMatrix[a1.ID][b2.ID] + distanceMatrix[b2.ID][c1.ID];

									double costRemoved2 = distanceMatrix[a2.ID][b2.ID] + distanceMatrix[b2.ID][c2.ID];
									double costAdded2 = distanceMatrix[a2.ID][b1.ID] + distanceMatrix[b1.ID][c2.ID];

									moveCost = costAdded1 + costAdded2 - (costRemoved1 + costRemoved2);
		//                          BuilArcList(arcsDaysCreated, a1.uid, b2.uid, p, b2.uid, c1.uid, p, a2.uid, b1.uid, p, b1.uid, c2.uid);
		//                          BuilArcList(arcsDaysDeleted, a1.uid, b1.uid, p, b1.uid, c1.uid, p, a2.uid, b2.uid, p, b2.uid, c2.uid);

								}
								StoreBestSwapMove(firstRouteIndex, secondRouteIndex, firstNodeIndex, secondNodeIndex, moveCost, sm);
							}
						}
					}
				}
			}

			private void StoreBestSwapMove(int firstRouteIndex, int secondRouteIndex, int firstNodeIndex, int secondNodeIndex, double moveCost, SwapMove sm) {
				if (moveCost < sm.moveCost) {
					sm.firstRoutePosition = firstRouteIndex;
					sm.firstNodePosition = firstNodeIndex;
					sm.secondRoutePosition = secondRouteIndex;
					sm.secondNodePosition = secondNodeIndex;
					sm.moveCost = moveCost;
				}
		}

private  void findBestRelocationMove(RelocationMove rm, Solution s, double [][] distanceMatrix, int numberOfVehicles)
	{

		double bestMoveCost = Double.MAX_VALUE;


		for (int from = 0; from<numberOfVehicles; from++)
		{

			//to route for
			for (int to = 0; to<numberOfVehicles; to++)
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
								}
								else {
									rm.newLoadFrom = s.routes.get(from).load;
									rm.newLoadTo = s.routes.get(to).load;
								}

								//System.out.println("From route: " + rm.fromRoute + ", To Route: " + rm.toRoute + ", New Load From:" + rm.newLoadFrom + ", New Load To:" + rm.newLoadTo);
							}


						}



					}
				}
				//}
			}
		}
	}




	private static void applyRelocationMove(RelocationMove rm, Solution s, double[][] distanceMatrix)
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


   private void FindBestTwoOptMove(TwoOptMove top, Solution sol) {
        for (int rtInd1 = 0; rtInd1 < sol.routes.size(); rtInd1++) {
            Route rt1 = sol.routes.get(rtInd1);

            for (int rtInd2 = rtInd1; rtInd2 < sol.routes.size(); rtInd2++) {
                Route rt2 = sol.routes.get(rtInd2);

                for (int nodeInd1 = 0; nodeInd1 < rt1.nodes.size() - 1; nodeInd1++) {
                    int start2 = 0;
                    if (rt1 == rt2) {
                        start2 = nodeInd1 + 2;
                    }

                    for (int nodeInd2 = start2; nodeInd2 < rt2.nodes.size() - 1; nodeInd2++)
                    {
                        double moveCost = Double.MAX_VALUE;

                        if (rt1 == rt2) {
                            Node A = rt1.nodes.get(nodeInd1);
                            Node B = rt1.nodes.get(nodeInd1 + 1);
                            Node K = rt2.nodes.get(nodeInd2);
                            Node L = rt2.nodes.get(nodeInd2 + 1);

                            if (nodeInd1 == 0 && nodeInd2 == rt1.nodes.size() - 2) {
                                continue;
                            }

                            double costAdded = distanceMatrix[A.ID][K.ID] + distanceMatrix[B.ID][L.ID];
                            double costRemoved = distanceMatrix[A.ID][B.ID] + distanceMatrix[K.ID][L.ID];

                            moveCost = costAdded - costRemoved;

                        } else {
                            Node A = (rt1.nodes.get(nodeInd1));
                            Node B = (rt1.nodes.get(nodeInd1 + 1));
                            Node K = (rt2.nodes.get(nodeInd2));
                            Node L = (rt2.nodes.get(nodeInd2 + 1));

                            if (nodeInd1 == 0 && nodeInd2 == 0) {
                                continue;
                            }
                            if (nodeInd1 == rt1.nodes.size() - 2 && nodeInd2 == rt2.nodes.size() - 2) {
                                continue;
                            }

                            if (CapacityConstraintsAreViolated(rt1, nodeInd1, rt2, nodeInd2)) {
                                continue;
                            }

                            double costAdded = distanceMatrix[A.ID][L.ID] + distanceMatrix[B.ID][K.ID];
                            double costRemoved = distanceMatrix[A.ID][B.ID] + distanceMatrix[K.ID][L.ID];

                            moveCost = costAdded - costRemoved;
                        }

                        if (moveCost < top.moveCost)
                        {
                            StoreBestTwoOptMove(rtInd1, rtInd2, nodeInd1, nodeInd2, moveCost, top);
                        }
                    }
                }
            }
        }
    }

    private void StoreBestTwoOptMove(int rtInd1, int rtInd2, int nodeInd1, int nodeInd2, double moveCost, TwoOptMove top) {
        top.positionOfFirstRoute = rtInd1;
        top.positionOfSecondRoute = rtInd2;
        top.positionOfFirstNode = nodeInd1;
        top.positionOfSecondNode = nodeInd2;
        top.moveCost = moveCost;
    }

    private void ApplyTwoOptMove(TwoOptMove top, Solution sol)
    {
        Route rt1 = sol.routes.get(top.positionOfFirstRoute);
        Route rt2 = sol.routes.get(top.positionOfSecondRoute);

        if (rt1 == rt2)
        {
            ArrayList modifiedRt = new ArrayList();

            for (int i = 0; i <= top.positionOfFirstNode; i++)
            {
                modifiedRt.add(rt1.nodes.get(i));
            }
            for (int i = top.positionOfSecondNode; i > top.positionOfFirstNode; i--)
            {
                modifiedRt.add(rt1.nodes.get(i));
            }
            for (int i = top.positionOfSecondNode + 1; i < rt1.nodes.size(); i++)
            {
                modifiedRt.add(rt1.nodes.get(i));
            }

            rt1.nodes = modifiedRt;

            rt1.cost += top.moveCost;
            sol.cost += top.moveCost;
        }
        else
        {
            ArrayList modifiedRt1 = new ArrayList();
            ArrayList modifiedRt2 = new ArrayList();

            Node A = (rt1.nodes.get(top.positionOfFirstNode));
            Node B = (rt1.nodes.get(top.positionOfFirstNode + 1));
            Node K = (rt2.nodes.get(top.positionOfSecondNode));
            Node L = (rt2.nodes.get(top.positionOfSecondNode + 1));


            for (int i = 0 ; i <= top.positionOfFirstNode; i++)
            {
                modifiedRt1.add(rt1.nodes.get(i));
            }
             for (int i = top.positionOfSecondNode + 1 ; i < rt2.nodes.size(); i++)
            {
                modifiedRt1.add(rt2.nodes.get(i));
            }

            for (int i = 0 ; i <= top.positionOfSecondNode; i++)
            {
                modifiedRt2.add(rt2.nodes.get(i));
            }
            for (int i = top.positionOfFirstNode + 1 ; i < rt1.nodes.size(); i++)
            {
                modifiedRt2.add(rt1.nodes.get(i));
            }

            double rt1SegmentLoad = 0;
            for (int i = 0 ; i <= top.positionOfFirstNode; i++)
            {
                rt1SegmentLoad += rt1.nodes.get(i).demand;
            }

            double rt2SegmentLoad = 0;
            for (int i = 0 ; i <= top.positionOfSecondNode; i++)
            {
                rt2SegmentLoad += rt2.nodes.get(i).demand;
            }

            double originalRt1Load = rt1.load;

            rt1.load = (int)rt1SegmentLoad + (rt2.load - (int)rt2SegmentLoad);
            rt2.load = (int)rt2SegmentLoad + ((int)originalRt1Load - (int)rt1SegmentLoad);

            rt1.nodes = modifiedRt1;
            rt2.nodes = modifiedRt2;

            rt1.cost = UpdateRouteCost(rt1);
            rt2.cost = UpdateRouteCost(rt2);

            sol.cost += top.moveCost;
        }

    }

        private double UpdateRouteCost(Route rt)
	    {
	        double totCost = 0 ;
	        for (int i = 0 ; i < rt.nodes.size()-1; i++)
	        {
	            Node A = rt.nodes.get(i);
	            Node B = rt.nodes.get(i+1);
	            totCost += distanceMatrix[A.ID][B.ID];
	        }
	        return totCost;
    }

    private boolean CapacityConstraintsAreViolated(Route rt1, int nodeInd1, Route rt2, int nodeInd2)
    {
        double rt1FirstSegmentLoad = 0;
        for (int i = 0 ; i <= nodeInd1; i++)
        {
            rt1FirstSegmentLoad += rt1.nodes.get(i).demand;
        }
        double rt1SecondSegment = rt1.load - rt1FirstSegmentLoad;

        double rt2FirstSegmentLoad = 0;
        for (int i = 0 ; i <= nodeInd2; i++)
        {
            rt2FirstSegmentLoad += rt2.nodes.get(i).demand;
        }
        double rt2SecondSegment = rt2.load - rt2FirstSegmentLoad;

        if (rt1FirstSegmentLoad +  rt2SecondSegment > rt1.capacity)
        {
            return true;
        }

        if (rt2FirstSegmentLoad +  rt1SecondSegment > rt2.capacity)
        {
            return true;
        }

        return false;
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
                totalCost += distanceMatrix[A.ID][B.ID];
            }
        }

        totalCost = (totalCost/35) ;
        totalCost += (15/60) * totalNodes ;


        System.out.println(totalCost);

        return totalCost;

    }


}



