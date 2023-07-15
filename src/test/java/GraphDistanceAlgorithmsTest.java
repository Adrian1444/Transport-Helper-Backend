import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trhelper.domain.order.PlacedOrderLocation;
import org.trhelper.domain.order.PlacedOrderLocationType;
import org.trhelper.service.utils.graphs.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GraphDistanceAlgorithmsTest {
    private List<Edge> graph;
    private List<PlacedOrderLocation> locations;


    @BeforeEach
    void setUp() {
        PlacedOrderLocation location1 = new PlacedOrderLocation("0", 46.5446253, 24.561196, 0L, PlacedOrderLocationType.ORIGIN, "Targu Mures");
        PlacedOrderLocation location2 = new PlacedOrderLocation("1", 46.4501899, 24.2306699, 1L, PlacedOrderLocationType.DESTINATION, "Iernut");
        PlacedOrderLocation location3 = new PlacedOrderLocation("2", 46.4833006, 24.0950842, 2L, PlacedOrderLocationType.ORIGIN, "Ludus");
        PlacedOrderLocation location4 = new PlacedOrderLocation("3", 46.5685214, 23.7853643, 0L, PlacedOrderLocationType.DESTINATION, "Turda");
        PlacedOrderLocation location5 = new PlacedOrderLocation("4", 46.769379, 23.5899542, 1L, PlacedOrderLocationType.ORIGIN, "Cluj Napoca");
        PlacedOrderLocation location6 = new PlacedOrderLocation("5", 46.8629674, 23.0204274, 2L, PlacedOrderLocationType.DESTINATION, "Huedin");
        PlacedOrderLocation location7 = new PlacedOrderLocation("6", 46.6057231, 24.6431138, 3L, PlacedOrderLocationType.ORIGIN, "Ernei");
        PlacedOrderLocation location8 = new PlacedOrderLocation("7", 46.7796886, 24.7000238, 3L, PlacedOrderLocationType.DESTINATION, "Reghin");

        locations=new ArrayList<>(Arrays.asList(location1, location2, location3, location4, location5, location6, location7, location8));
        graph= ConstructOrderDistancesGraph.constructGraph(locations);
    }

    @Test
    void testDijkstraAlgorithmForLocations() {
        List<String> path=ShortestPathThroughOtherNodes.findShortestPathThroughNodes(locations,graph,
                "0","3",new ArrayList<>());
        //A path exists?
        assertNotNull(path);
        //Does the path contain the source and the destination?
        assertTrue(path.contains("0"));
        assertTrue(path.contains("3"));
        //Does the path from Tg.Mures to Turda contain Iernut and Ludus?
        assertTrue(path.contains("1"));
        assertTrue(path.contains("2"));
        //Are Iernut and Ludus on the correct positions?
        assertEquals("1", path.get(1));
        assertEquals("2", path.get(2));
        //Does the path have only Tg.Mures,Turda,Iernut and Ludus?
        assertEquals(path.size(),4);
    }

    @Test
    void testDijkstraAlgorithmForLocationsWithIntermediateNode() {
        List<String> path=ShortestPathThroughOtherNodes.findShortestPathThroughNodes(locations,graph,"0","3",new ArrayList<String>(List.of("6")));
        //A path exists?
        assertNotNull(path);
        //Is the intermediate node on the correct position?
        assertEquals("6", path.get(1));
    }

    @Test
    void testKruskalForLocations(){
        List<Edge> edges= KruskalAlgorithm.applyKruskalAlgorithm(locations);
        //Edges exist?
        assertNotNull(edges);
        //Does the new graph contain n-1 nodes?
        assertEquals(edges.size(),locations.size()-1);
        //Test the existence of some edges
        boolean found1=false;
        boolean found2=false;
        for(Edge edge:edges){
            if(GraphUtils.edgeExists(edges,0,6))
                found1=true;
            else if(GraphUtils.edgeExists(edges,1,2));
                found2=true;
        }
        assertTrue(found1);
        assertTrue(found2);
    }

}
