package org.trhelper.service.utils.graphs;

import org.trhelper.domain.order.PlacedOrderLocation;
import org.trhelper.domain.order.PlacedOrderLocationType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class GeneticAlgorithmShortestPath {
    private static final int POPULATION_SIZE = 200;
    private static final double MUTATION_RATE = 0.1;
    private static final int GENERATIONS = 400;

    public static List<PlacedOrderLocation> findShortestPath(List<PlacedOrderLocation> locations, List<Edge> edges,PlacedOrderLocation startingNode) {
        Map<String, List<Edge>> adjacencyList = GraphUtils.createAdjacencyList(locations, edges);
        List<Route> population = initializePopulation(locations, POPULATION_SIZE,adjacencyList,startingNode);

        Optional<Route> bestRoute = Optional.empty();


        for (int generation = 0; generation < GENERATIONS; generation++) {
            if(generation%25==0)
                System.out.println(population.stream().min(Comparator.comparing(Route::getTotalDistance)).get().getTotalDistance());
            List<Route> newPopulation = new ArrayList<>();

            for (int i = 0; i < POPULATION_SIZE; i++) {
                Route parent1 = selectParent(population);
                Route parent2 = selectParent(population);

                Route child = crossover(parent1, parent2, adjacencyList,startingNode);

                if (new Random().nextDouble() < MUTATION_RATE) {
                    child = mutate(child, adjacencyList);
                }

                newPopulation.add(child);
            }
            Optional<Route> optRoute = population.stream()
                    .filter(solution -> solution.checkStartingNodeAndPickupDeliveryRestriction(startingNode, locations.size()))
                    .min(Comparator.comparing(Route::getTotalDistance));
            optRoute.ifPresent(newPopulation::add);

            population = newPopulation;
        }
        bestRoute = population.stream().min(Comparator.comparing(Route::getTotalDistance));

        List<PlacedOrderLocation> locationList=new ArrayList<>();
        if(bestRoute.isEmpty())
            return locationList;

        for(int i=0;i<bestRoute.get().locations.size();i++)
        {
            PlacedOrderLocation location1=bestRoute.get().locations.get(i);
            boolean foundCorrespondent=false;
            for (int j=0;j<bestRoute.get().locations.size();j++){
                PlacedOrderLocation location2=bestRoute.get().locations.get(j);
                if(i!=j && location2.getOrderId().equals(location1.getOrderId()))
                    foundCorrespondent=true;
            }
            if(foundCorrespondent)
                locationList.add(location1);
        }

        for(PlacedOrderLocation location:locationList)
            System.out.println(location.getOrderId()+" "+location.getName());
        return locationList;
    }




    private static List<Route> initializePopulation(List<PlacedOrderLocation> locations, int populationSize, Map<String, List<Edge>> adjacencyList,PlacedOrderLocation startingNode) {
        List<PlacedOrderLocation> origins = locations.stream()
            .filter(loc -> loc.getType() == PlacedOrderLocationType.ORIGIN)
            .collect(Collectors.toList());

        return IntStream.range(0, populationSize)
            .parallel()
            .mapToObj(i -> {
                List<PlacedOrderLocation> shuffledOrigins = new ArrayList<>(origins);
                Collections.shuffle(shuffledOrigins);

                List<PlacedOrderLocation> routeLocations = new ArrayList<>();
                for (PlacedOrderLocation origin : shuffledOrigins) {
                    routeLocations.add(origin);
                    PlacedOrderLocation destination = locations.stream()
                            .filter(loc -> loc.getType() == PlacedOrderLocationType.DESTINATION && loc.getOrderId().equals(origin.getOrderId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("No matching destination found for origin " + origin.getId()));
                    routeLocations.add(destination);
                }
                return createValidRoute(routeLocations, adjacencyList);
            })
            .collect(Collectors.toList());
    }



    private static Route createValidRoute(List<PlacedOrderLocation> locations, Map<String, List<Edge>> adjacencyList) {
        List<PlacedOrderLocation> validLocations = new ArrayList<>();
        validLocations.add(locations.get(0));
        for (int i = 1; i < locations.size(); i++) {
            PlacedOrderLocation currentLocation = locations.get(i);
            validLocations.add(currentLocation);
        }

        return new Route(validLocations);
    }

    private static Route crossover(Route parent1, Route parent2, Map<String, List<Edge>> adjacencyList,PlacedOrderLocation startingNode) {
        List<PlacedOrderLocation> childLocations = new ArrayList<>();
        childLocations.add(startingNode);

        Set<String> visitedIds = new HashSet<>();
        visitedIds.add(startingNode.getId());

        int startIndex = new Random().nextInt(parent1.getLocations().size());
        int endIndex = startIndex + new Random().nextInt(parent1.getLocations().size() - startIndex);

        // Take a random subsequence from parent1
        for (int i = startIndex; i <= endIndex; i++) {
            PlacedOrderLocation location = parent1.getLocations().get(i);
            childLocations.add(location);
            visitedIds.add(location.getId());
        }

        // Fill the remaining nodes from parent2
        for (PlacedOrderLocation location : parent2.getLocations()) {
            if (!visitedIds.contains(location.getId())) {
                childLocations.add(location);
                visitedIds.add(location.getId());
            }
        }

        // Create a valid route by moving nodes if necessary
        for (int i = 0; i < childLocations.size(); i++) {
            PlacedOrderLocation current = childLocations.get(i);
            if (current.getType() == PlacedOrderLocationType.DESTINATION) {
                int originIndex = childLocations.indexOf(childLocations.stream().filter(loc -> Objects.equals(loc.getOrderId(), current.getOrderId()) && loc.getType() == PlacedOrderLocationType.ORIGIN).findFirst().orElse(null));
                if (originIndex > i) {
                    PlacedOrderLocation origin = childLocations.get(originIndex);
                    childLocations.remove(originIndex);
                    childLocations.add(i, origin);
                    i--;
                }
            }
        }

        return new Route(childLocations);
    }



    private static Route mutate(Route route, Map<String, List<Edge>> adjacencyList) {
        List<PlacedOrderLocation> locations = route.getLocations();
        List<PlacedOrderLocation> mutatedLocations = new ArrayList<>(locations);

        int index1 = new Random().nextInt(locations.size() / 2) * 2;
        int index2 = new Random().nextInt(locations.size() / 2) * 2;

        PlacedOrderLocation loc1 = mutatedLocations.get(index1);
        PlacedOrderLocation loc2 = mutatedLocations.get(index2);

        mutatedLocations.set(index1, loc2);
        mutatedLocations.set(index1 + 1, mutatedLocations.get(index2 + 1));

        mutatedLocations.set(index2, loc1);
        mutatedLocations.set(index2 + 1, locations.get(index1 + 1));

        //if (isRouteValid(new Route(mutatedLocations), adjacencyList)) {
            return new Route(mutatedLocations);
        //} else {
       //     return route;
        //}
    }

    /*
    private static boolean isValid(PlacedOrderLocation loc1, PlacedOrderLocation loc2, Map<String, List<Edge>> adjacencyList) {
        List<Edge> edgesFromLoc1 = adjacencyList.get(loc1.getId());
        if (edgesFromLoc1 != null) {
            return edgesFromLoc1.stream().anyMatch(edge -> edge.getTo().getId().equals(loc2.getId()));
        }
        return false;
    }
    */
    private static Route selectParent(List<Route> population) {
        int tournamentSize = 5;
        List<Route> tournament = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < tournamentSize; i++) {
            tournament.add(population.get(random.nextInt(population.size())));
        }

        return tournament.stream().min(Comparator.comparing(Route::getTotalDistance)).orElse(null);
    }



    public static class Route {
        private List<PlacedOrderLocation> locations;
        private double totalDistance;

        public Route(List<PlacedOrderLocation> locations) {
            this.locations = locations;
            this.totalDistance = calculateTotalDistance(locations);
        }

        public List<PlacedOrderLocation> getLocations() {
            return locations;
        }

        public double getTotalDistance() {
            return totalDistance;
        }

        private double calculateTotalDistance(List<PlacedOrderLocation> locations) {
            double distance = 0;
            for (int i = 0; i < locations.size() - 1; i++) {
                PlacedOrderLocation from = locations.get(i);
                PlacedOrderLocation to = locations.get(i + 1);
                distance += getDistance(from, to);
            }

            return distance;
        }

        private double getDistance(PlacedOrderLocation from, PlacedOrderLocation to) {
            return Math.sqrt(Math.pow(from.getLatitude() - to.getLatitude(), 2) + Math.pow(from.getLongitude() - to.getLongitude(), 2));
        }

        public boolean checkStartingNodeAndPickupDeliveryRestriction(PlacedOrderLocation startingNode,int nrLocations){
            if(locations.get(0)!=startingNode)
                return false;
            if(locations.size()!=nrLocations)
                return false;
            if(locations.stream().distinct().count() !=nrLocations)
                return false;

            for(int i=0;i<locations.size()-1;i++){
                if(locations.get(i).getType()==PlacedOrderLocationType.ORIGIN){
                    boolean found=false;
                    Long orderId=locations.get(i).getOrderId();
                    for(int j=i+1;j<locations.size();j++){
                        if(locations.get(j).getOrderId().equals(orderId)
                                && locations.get(j).getType()==PlacedOrderLocationType.DESTINATION){
                            found=true;
                            break;
                        }
                    }
                    if(!found)
                        return false;
                }
            }
            return true;
        }


    }
}





