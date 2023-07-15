import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trhelper.domain.order.PlacedOrder;
import org.trhelper.domain.order.PlacedOrderLocation;
import org.trhelper.domain.order.PlacedOrderLocationType;
import org.trhelper.service.utils.graphs.ConstructOrderDistancesGraph;
import org.trhelper.service.utils.graphs.Edge;
import org.trhelper.service.utils.graphs.GeneticAlgorithmShortestPath;
import org.trhelper.service.utils.graphs.TspProblem2opt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class HeuristicAlgorithmsTest {
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
    void test2OptAlgorithmForTSP(){
        String startingNode="2";
        List<String> path=TspProblem2opt.findShortestPath(graph, locations,startingNode);
        //Is there a path found?
        assertNotNull(path);
        //Are all the nodes present in the result?
        assertEquals(8, path.size());
        //Are all elements distinct?
        assertEquals(path,path.stream().distinct().collect(Collectors.toList()));
    }

    @Test
    void testGeneticAlgorithmForTSPwithPickupAndDelivery(){
        String startingNode="2";
        List<PlacedOrderLocation> locationList= GeneticAlgorithmShortestPath.findShortestPath(locations,graph, locations.get(Integer.parseInt(startingNode)));
        //Is there a path found?
        assertNotNull(locationList);
        //Are all the nodes present in the result?
        assertEquals(8, locationList.size());
        //Are all elements distinct?
        assertEquals(locationList,locationList.stream().distinct().collect(Collectors.toList()));
        //Is the pickup and delivery restriction respected?
        for(int i=0;i<locationList.size()-1;i++){
            if(locationList.get(i).getType()==PlacedOrderLocationType.ORIGIN){
                boolean found=false;
                Long orderId=locationList.get(i).getOrderId();
                for(int j=i+1;j<locationList.size();j++){
                    if(locationList.get(j).getOrderId().equals(orderId)
                            && locationList.get(j).getType()==PlacedOrderLocationType.DESTINATION){
                        found=true;
                        break;
                    }
                }
                assertTrue(found);
            }

        }
    }
}
