package org.trhelper.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.trhelper.domain.order.OrderStatus;
import org.trhelper.domain.order.PlacedOrder;
import org.trhelper.domain.order.TransportType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@org.springframework.stereotype.Repository
@EnableJpaRepositories(repositoryBaseClass = PlacedOrderFilterRepositoryImpl.class)
public interface PlacedOrderFilterRepository extends Repository<PlacedOrder, Long> {
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
            Double maxCost,
            OrderStatus status
    );
}
