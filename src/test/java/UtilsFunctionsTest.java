import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trhelper.domain.order.PlacedOrderLocation;
import org.trhelper.domain.order.PlacedOrderLocationType;
import org.trhelper.service.user.implementation.ClientServiceImpl;
import org.trhelper.service.utils.graphs.Edge;
import org.trhelper.service.utils.graphs.GraphUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsFunctionsTest {

    @Test
    void testEdgeExists(){
        PlacedOrderLocation location1 = new PlacedOrderLocation("0", 46.5446253, 24.561196, 0L, PlacedOrderLocationType.ORIGIN, "Targu Mures");
        PlacedOrderLocation location2 = new PlacedOrderLocation("1", 46.769379, 23.5899542, 1L, PlacedOrderLocationType.ORIGIN, "Cluj Napoca");
        PlacedOrderLocation location3 = new PlacedOrderLocation("2", 46.8629674, 23.0204274, 2L, PlacedOrderLocationType.DESTINATION, "Huedin");
        Edge edge1=new Edge(location1,location2,70);
        Edge edge2=new Edge(location2,location3,50);
        List<Edge> edges=new ArrayList<>();
        edges.add(edge1);
        edges.add(edge2);
        assertTrue(GraphUtils.edgeExists(edges,0,1));
        assertTrue(GraphUtils.edgeExists(edges,1,0));
        assertFalse(GraphUtils.edgeExists(edges,0,2));
    }

    @Test
    void testDistanceFunction(){
        PlacedOrderLocation location1 = new PlacedOrderLocation("0", 46.5446253, 24.561196, 0L, PlacedOrderLocationType.ORIGIN, "Targu Mures");
        PlacedOrderLocation location2 = new PlacedOrderLocation("4", 46.769379, 23.5899542, 1L, PlacedOrderLocationType.ORIGIN, "Cluj Napoca");
        double distance=GraphUtils.distance(location1,location2);
        assertTrue(distance>=78);
        assertTrue(distance<=79);
    }


}
