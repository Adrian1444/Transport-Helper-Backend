package org.trhelper.service.user;

import org.springframework.stereotype.Service;
import org.trhelper.domain.order.OrderStatus;
import org.trhelper.domain.order.PlacedOrder;
import org.trhelper.domain.order.TransportType;
import org.trhelper.domain.order.dto.SendPlacedOrderDTO;
import org.trhelper.domain.order.dto.GetPlacedOrderDTO;

import java.util.List;

@Service
public interface PlacedOrderService {
   void add(GetPlacedOrderDTO placedOrder) throws Exception;
   List<SendPlacedOrderDTO> findOrdersPlacedBy(String username) throws Exception;
   List<SendPlacedOrderDTO> findOrdersAcceptedBy(String username) throws Exception;

   void updateOrderStatus(OrderStatus status, Long id);
   List<PlacedOrder> filterOrders(
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
   );
   void confirmOrder(Long id, String acceptedByUsername, String startingDate, String startingTime, String durationToPickupLocation, String durationToDestination) throws Exception;


}
