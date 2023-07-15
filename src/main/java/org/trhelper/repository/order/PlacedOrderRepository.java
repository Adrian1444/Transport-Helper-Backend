package org.trhelper.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.trhelper.domain.order.OrderStatus;
import org.trhelper.domain.order.PlacedOrder;
import org.trhelper.domain.order.TransportType;
import org.trhelper.domain.user.Client;
import org.trhelper.domain.user.Driver;

import java.util.List;

@Repository
@Transactional
public interface PlacedOrderRepository extends JpaRepository<PlacedOrder,Long>{
    @Query("from PlacedOrder placedOrder where placedOrder.placedBy=?1")
    List<PlacedOrder> findOrdersPlacedBy(Client client);

    @Query("from PlacedOrder p where p.acceptedBy=?1")
    List<PlacedOrder> findOrdersAcceptedBy(Driver driver);

    @Modifying
    @Query("update PlacedOrder p set p.status=?1 where p.id = ?2")
    void updateOrderStatus(OrderStatus status, Long id);

    PlacedOrder findPlacedOrderById(Long id);

    @Modifying
    @Query("update PlacedOrder p set p.acceptedBy=?2,p.durationToPickupLocation=?3,p.durationToDestination=?4,p.pickupTime=?5,p.arrivalToDestinationTime=?6,p.status='ACCEPTED' where p.id = ?1")
    void confirmOrder(Long id,Driver acceptedBy, String durationToPickupLocation, String durationToDestination, String pickupLocationArrivalDateString, String destinationArrivalDateString);
}
