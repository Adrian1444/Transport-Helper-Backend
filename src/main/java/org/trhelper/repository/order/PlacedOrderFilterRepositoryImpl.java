package org.trhelper.repository.order;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.trhelper.domain.order.OrderStatus;
import org.trhelper.domain.order.PlacedOrder;
import org.trhelper.domain.order.TransportType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class PlacedOrderFilterRepositoryImpl extends SimpleJpaRepository<PlacedOrder, Long> implements PlacedOrderFilterRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public PlacedOrderFilterRepositoryImpl(EntityManager entityManager) {
        super(PlacedOrder.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public List<PlacedOrder> filterOrders(String origin, String destination, List<TransportType> transportTypeList, Double minWeight, Double maxWeight, Integer minNumberOfItems, Integer maxNumberOfItems, String pickupDate, Double minCost, Double maxCost, OrderStatus status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PlacedOrder> query = cb.createQuery(PlacedOrder.class);
        Root<PlacedOrder> root = query.from(PlacedOrder.class);

        List<Predicate> predicates = new ArrayList<>();
        if (origin != null) {
            predicates.add(cb.like(root.get("origin"), "%" + origin + "%"));
        }
        if(status!=null){
            predicates.add(cb.equal(root.get("status"), status));
        }
        if (destination != null) {
            predicates.add(cb.like(root.get("destination"), "%" + destination + "%"));
        }
        if (transportTypeList != null && transportTypeList.size()!=0) {
            predicates.add(root.get("transportType").in(transportTypeList));
        }
        if(minWeight!=null) {
            predicates.add(cb.ge(root.get("weight"), minWeight));
        }
        if(maxWeight!=null) {
            predicates.add(cb.le(root.get("weight"), maxWeight));
        }
        if(minNumberOfItems!=null) {
            predicates.add(cb.ge(root.get("numberOfItems"), minNumberOfItems));
        }
        if(maxNumberOfItems!=null) {
            predicates.add(cb.le(root.get("numberOfItems"), maxNumberOfItems));
        }
        if (pickupDate != null) {
            predicates.add(cb.equal(root.get("pickupDate"), pickupDate));
        }
        if(minCost!=null) {
            predicates.add(cb.ge(root.get("cost"), minCost));
        }
        if(maxCost!=null) {
            predicates.add(cb.le(root.get("cost"), maxCost));
        }
        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();

    }
}