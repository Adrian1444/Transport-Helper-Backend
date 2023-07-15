package org.trhelper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.trhelper.domain.order.*;
import org.trhelper.domain.order.dto.*;
import org.trhelper.service.user.implementation.PlacedOrderServiceImpl;
import org.trhelper.service.utils.Pair;
import org.trhelper.service.utils.graphs.Edge;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path="/order")
public class PlacedOrderController {

    @Autowired
    private PlacedOrderServiceImpl placedOrderService;

    @PostMapping("/client/add")
    public ResponseEntity<?> add(@RequestBody GetPlacedOrderDTO placedOrder){
        try{
            placedOrderService.add(placedOrder);
            return ResponseEntity.ok().body(placedOrder);
        }catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/client/find/placedBy/{username}")
    public ResponseEntity<?> getAllOrdersPlacedByUser(@PathVariable String username) {
        try{
            List<SendPlacedOrderDTO> orders=placedOrderService.findOrdersPlacedBy(username);
            return ResponseEntity.ok().body(orders);
        }catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/driver/find/acceptedBy/{username}")
    public ResponseEntity<?> getAllOrdersAcceptedByUser(@PathVariable String username) {
        try{
            List<SendPlacedOrderDTO> orders=placedOrderService.findOrdersAcceptedBy(username);
            return ResponseEntity.ok().body(orders);
        }catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/update/status/{status}/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> makeStatusDeleted(@PathVariable OrderStatus status, @PathVariable Long id) {
        try{
            placedOrderService.updateOrderStatus(status,id);
            return ResponseEntity.ok().body("Order status updated successfully!");
        }catch(Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/driver/find/filter")
    public ResponseEntity<?> filter(@RequestBody FilterPlacedOrderDTO filterPlacedOrderDTO){
        try{
            List<PlacedOrder> placedOrders = placedOrderService.filterOrders(
                    filterPlacedOrderDTO.getOrigin(),
                    filterPlacedOrderDTO.getDestination(),
                    filterPlacedOrderDTO.getTransportTypeList(),
                    filterPlacedOrderDTO.getMinWeight(),
                    filterPlacedOrderDTO.getMaxWeight(),
                    filterPlacedOrderDTO.getMinNumberOfItems(),
                    filterPlacedOrderDTO.getMaxNumberOfItems(),
                    filterPlacedOrderDTO.getPickupDate(),
                    filterPlacedOrderDTO.getMinCost(),
                    filterPlacedOrderDTO.getMaxCost()
            );



            List<SendPlacedOrderDTO> placedOrderDTOList=new ArrayList<>();
            for(PlacedOrder order:placedOrders){
                String acceptedByUsername;
                if(order.getAcceptedBy()==null)
                    acceptedByUsername=null;
                else
                    acceptedByUsername=order.getAcceptedBy().getUsername();
                placedOrderDTOList.add(new SendPlacedOrderDTO(
                        order.getId(),
                        order.getOrigin(),
                        order.getDestination(),
                        order.getTransportType(),
                        order.getWeight(),
                        order.getNumberOfItems(),
                        order.getPickupDate(),
                        order.getAdditionalInformation(),
                        order.getPlacedBy().getUsername(),
                        acceptedByUsername,
                        order.getStatus(),
                        order.getTimestamp(),
                        order.getCost(),
                        order.getPickupTime(),
                        order.getArrivalToDestinationTime()
                ));
            }
            return ResponseEntity.ok().body(placedOrderDTOList);

        }catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/driver/update/confirm", method = RequestMethod.POST)
    public ResponseEntity<?> confirmOrder(@RequestBody ConfirmOrderDTO confirmOrderDTO) {
        try{
            placedOrderService.confirmOrder(confirmOrderDTO.getId(), confirmOrderDTO.getAcceptedByUsername(), confirmOrderDTO.getStartingDate(), confirmOrderDTO.getStartingTime(), confirmOrderDTO.getDurationToPickupLocation(), confirmOrderDTO.getDurationToDestination());
            return ResponseEntity.ok().body("Order confirmed successfully!");
        }catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/driver/find/orders/locations/{acceptedBy}")
    public ResponseEntity<?> getOrdersLocations(@PathVariable String acceptedBy) {
        try{
            List<PlacedOrderLocation> locations=placedOrderService.findOrderLocations(acceptedBy);
            return ResponseEntity.ok().body(locations);
        }catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/driver/find/orders/graph/{acceptedBy}")
    public ResponseEntity<?> getOrdersGraph(@PathVariable String acceptedBy) {
        try{
            List<Edge> orders=placedOrderService.generatePlacedOrderGraph(acceptedBy);
            return ResponseEntity.ok().body(orders);
        }catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/driver/find/orders/graph/shortestpath")
    public ResponseEntity<?> findShortestPath(@RequestBody LocationsShortestPathRequestDTO locationsShortestPathRequestDTO){
        try{
            List<String> orders=placedOrderService.findShortestPath(locationsShortestPathRequestDTO.getAcceptedBy(), locationsShortestPathRequestDTO.getSource(), locationsShortestPathRequestDTO.getDestination(), locationsShortestPathRequestDTO.getIntermediateNodes());
            return ResponseEntity.ok().body(orders);
        }catch(Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/driver/find/orders/graph/allnodes/shortestpath/{acceptedBy}/{startingNode}")
    public ResponseEntity<?> findShortestPathThroughAllNodes(@PathVariable String acceptedBy,@PathVariable String startingNode){
        try{
            List<String> orders=placedOrderService.findShortestPathThroughAllNodes(acceptedBy,startingNode);
            return ResponseEntity.ok().body(orders);
        }catch(Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/driver/find/orders/graph/allnodes/shortestpath/constraints/{acceptedBy}/{startingNode}")
    public ResponseEntity<?> findShortestPathThroughAllNodesWithConstraints(@PathVariable String acceptedBy,@PathVariable String startingNode){
        try{
            List<String> orders=placedOrderService.findShortestPathThroughAllNodesWithOriginDestinationConstraints(acceptedBy,startingNode);
            return ResponseEntity.ok().body(orders);
        }catch(Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }


}
