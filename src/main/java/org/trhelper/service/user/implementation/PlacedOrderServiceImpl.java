package org.trhelper.service.user.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trhelper.domain.order.*;
import org.trhelper.domain.order.dto.SendPlacedOrderDTO;
import org.trhelper.domain.order.dto.GetPlacedOrderDTO;
import org.trhelper.domain.user.Client;
import org.trhelper.domain.user.Driver;
import org.trhelper.repository.order.PlacedOrderFilterRepository;
import org.trhelper.repository.order.PlacedOrderRepository;
import org.trhelper.service.user.PlacedOrderService;
import org.trhelper.service.utils.Pair;
import org.trhelper.service.utils.TimeFormatterUtils;
import org.trhelper.service.utils.graphs.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlacedOrderServiceImpl implements PlacedOrderService {

    @Autowired
    private PlacedOrderRepository placedOrderRepository;

    @Autowired
    private PlacedOrderFilterRepository placedOrderFilterRepository;

    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private DriverServiceImpl driverService;

    public void add(GetPlacedOrderDTO placedOrderDto) throws Exception {
        Client placedBy=clientService.findByUsername(placedOrderDto.getPlacedByUsername());
        PlacedOrder placedOrder=new PlacedOrder(placedOrderDto.getOrigin(),
                placedOrderDto.getDestination(), placedOrderDto.getTransportType(),
                placedOrderDto.getWeight(), placedOrderDto.getNumberOfItems(),
                placedOrderDto.getPickupDate(),placedOrderDto.getAdditionalInformation(),
                placedBy,
                null,
                OrderStatus.PENDING,
                TimeFormatterUtils.getCurrentTime("yyyy-MM-dd HH:mm"),
                placedOrderDto.getCost(),
                null,
                null,
                null,
                null,
                placedOrderDto.getLatitudeOrigin(),
                placedOrderDto.getLongitudeOrigin(),
                placedOrderDto.getLatitudeDestination(),
                placedOrderDto.getLongitudeDestination());
        placedOrderRepository.save(placedOrder);
    }

    public List<SendPlacedOrderDTO> findOrdersPlacedBy(String username) throws Exception{
        Client client= clientService.findByUsername(username);
        List<PlacedOrder> orders=placedOrderRepository.findOrdersPlacedBy(client);
        List<SendPlacedOrderDTO> placedOrderDtoList=new ArrayList<>();
        for(PlacedOrder order:orders){

            String acceptedByUsername;
            if(order.getAcceptedBy()==null)
                acceptedByUsername=null;
            else
                acceptedByUsername=order.getAcceptedBy().getUsername();

            placedOrderDtoList.add(new SendPlacedOrderDTO(order.getId(),order.getOrigin(),
                    order.getDestination(), order.getTransportType(),
                    order.getWeight(), order.getNumberOfItems(),
                    order.getPickupDate(),order.getAdditionalInformation(),
                    order.getPlacedBy().getUsername(),
                    acceptedByUsername,
                    order.getStatus(),
                    order.getTimestamp(),
                    order.getCost(),
                    order.getPickupTime(),
                    order.getArrivalToDestinationTime()
                    ));
        }
        return placedOrderDtoList;
    }

    public List<SendPlacedOrderDTO> findOrdersAcceptedBy(String username) throws Exception{
        Driver driver= driverService.findByUsername(username);
        List<PlacedOrder> orders=placedOrderRepository.findOrdersAcceptedBy(driver);
        List<SendPlacedOrderDTO> placedOrderDtoList=new ArrayList<>();
        for(PlacedOrder order:orders){

            String acceptedByUsername;
            if(order.getAcceptedBy()==null)
                acceptedByUsername=null;
            else
                acceptedByUsername=order.getAcceptedBy().getUsername();

            placedOrderDtoList.add(new SendPlacedOrderDTO(order.getId(),order.getOrigin(),
                    order.getDestination(), order.getTransportType(),
                    order.getWeight(), order.getNumberOfItems(),
                    order.getPickupDate(),order.getAdditionalInformation(),
                    order.getPlacedBy().getUsername(),
                    acceptedByUsername,
                    order.getStatus(),
                    order.getTimestamp(),
                    order.getCost(),
                    order.getPickupTime(),
                    order.getArrivalToDestinationTime()
            ));
        }
        return placedOrderDtoList;
    }

    public void updateOrderStatus(OrderStatus status, Long id){
        placedOrderRepository.updateOrderStatus(status,id);
    }

    public List<PlacedOrder> filterOrders(
            String origin,
            String destination,
            List<TransportType> transportTypeList,
            Double minWeight,
            Double maxWeight,
            Integer minNumberOfItems,
            Integer maxNumberOfItems,
            String pickupDate,
            Double minCost,
            Double maxCost
    ){
        return placedOrderFilterRepository.filterOrders(origin,
                destination,
                transportTypeList,
                minWeight,
                maxWeight,
                minNumberOfItems,
                maxNumberOfItems,
                pickupDate,
                minCost,
                maxCost,
                OrderStatus.PENDING);
    }


    public void confirmOrder(Long id, String acceptedByUsername, String startingDate, String startingTime, String durationToPickupLocation, String durationToDestination) throws Exception {
        String timestamp=startingDate+" "+startingTime;
        LocalDateTime pickupLocationArrivalDate = TimeFormatterUtils.addDurationStringToLocalDateTime(timestamp, durationToPickupLocation);
        String pickupLocationArrivalDateString = TimeFormatterUtils.formatResult(pickupLocationArrivalDate);
        LocalDateTime destinationArrivalDate = TimeFormatterUtils.addDurationStringToLocalDateTime(timestamp, durationToDestination);
        String destinationArrivalDateString = TimeFormatterUtils.formatResult(destinationArrivalDate);

        placedOrderRepository.confirmOrder(id,driverService.findByUsername(acceptedByUsername),durationToPickupLocation,durationToDestination,pickupLocationArrivalDateString,destinationArrivalDateString);

    }

    public List<PlacedOrderLocation> findOrderLocations(String acceptedBy) throws Exception {
        List<PlacedOrder> orders = placedOrderRepository.findOrdersAcceptedBy(driverService.findByUsername(acceptedBy))
                .stream()
                .filter(order -> order.getStatus().equals(OrderStatus.ACCEPTED))
                .collect(Collectors.toList());
        Long id = 0L;
        List<PlacedOrderLocation> locations = new ArrayList<>();
        for (PlacedOrder order : orders) {
            PlacedOrderLocation locationOrigin = new PlacedOrderLocation(String.valueOf(id),Double.parseDouble(order.getLatitudeOrigin()), Double.parseDouble(order.getLongitudeOrigin()), order.getId(), PlacedOrderLocationType.ORIGIN,order.getOrigin(),order.getCost());
            locations.add(locationOrigin);
            id++;
            PlacedOrderLocation locationDestination=new PlacedOrderLocation(String.valueOf(id),Double.parseDouble(order.getLatitudeDestination()), Double.parseDouble(order.getLongitudeDestination()), order.getId(), PlacedOrderLocationType.DESTINATION,order.getDestination(), order.getCost());
            locations.add(locationDestination);
            id++;
        }
        return locations;
    }

    public List<Edge> generatePlacedOrderGraph(String acceptedBy) throws Exception {
        List<PlacedOrderLocation> locations=findOrderLocations(acceptedBy);
        List<Edge> graph= ConstructOrderDistancesGraph.constructGraph(locations);
        return graph;
    }

    public List<String> findShortestPath(String acceptedBy, String source,String destination,List<String> intermediateNodes) throws Exception {
        List<PlacedOrderLocation> locations=findOrderLocations(acceptedBy);
        List<Edge> graph= ConstructOrderDistancesGraph.constructGraph(locations);
        return ShortestPathThroughOtherNodes.findShortestPathThroughNodes(locations,graph,source,destination,intermediateNodes);
    }

    public List<String> findShortestPathThroughAllNodes(String acceptedBy,String startingNode) throws Exception {
        List<PlacedOrderLocation> locations=findOrderLocations(acceptedBy);
        List<Edge> graph= ConstructOrderDistancesGraph.constructGraph(locations);
        return TspProblem2opt.findShortestPath(graph, locations,startingNode);

    }

    public List<String> findShortestPathThroughAllNodesWithOriginDestinationConstraints(String acceptedBy,String startingNode) throws Exception {
        List<PlacedOrderLocation> locations=findOrderLocations(acceptedBy);
        List<Edge> graph= ConstructOrderDistancesGraph.constructGraph(locations);
        List<PlacedOrderLocation> locationList=GeneticAlgorithmShortestPath.findShortestPath(locations,graph, locations.get(Integer.parseInt(startingNode)));
        //return NearestNeighbor.findShortestPath(graph, locations,"5");
        List<String> locationIds=new ArrayList<>();
        for(PlacedOrderLocation location:locationList)
            locationIds.add(location.getId());
        return locationIds;
    }
}
