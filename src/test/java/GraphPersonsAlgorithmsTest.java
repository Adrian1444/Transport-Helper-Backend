import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trhelper.domain.user.Driver;
import org.trhelper.domain.user.FollowingRelation;
import org.trhelper.service.utils.graphs.BFSAlgorithmForUserRelations;
import org.trhelper.service.utils.graphs.ConstructOrderDistancesGraph;
import org.trhelper.service.utils.graphs.GraphUtils;
import org.trhelper.service.utils.graphs.KosarajuSCCofUsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphPersonsAlgorithmsTest {

    private List<FollowingRelation> relations;
    private Driver driver1;
    private Driver driver2;
    private Driver driver3;
    private Driver driver4;
    private Driver driver5;
    private Driver driver6;

    @BeforeEach
    void setUp() {
        driver1=new Driver("username1","a","Adrian","Lungu","a@a.com","0734537674","Ro","",0L);
        driver2=new Driver("username2","a","Mihai","Adrianovic","a@a.com","0734537674","Ro","",1L);
        driver3=new Driver("username3","a","Adrian","Mateo","a@a.com","0734537674","Ro","",2L);
        driver4=new Driver("username4","a","Marcel","Ioan","a@a.com","0734537674","Ro","",3L);
        driver5=new Driver("username5","a","Adrian","Enescu","a@a.com","0734537674","Ro","",4L);
        driver6=new Driver("username6","a","Ioan","Rusu","a@a.com","0734537674","Ro","",5L);

        FollowingRelation relation1=new FollowingRelation(driver1,driver2);
        FollowingRelation relation2=new FollowingRelation(driver2,driver3);
        FollowingRelation relation3=new FollowingRelation(driver3,driver1);
        FollowingRelation relation4=new FollowingRelation(driver4,driver5);
        FollowingRelation relation5=new FollowingRelation(driver1,driver6);

        relations=new ArrayList<>(Arrays.asList(relation1,relation2,relation3,relation4,relation5));

    }

    @Test
    void testBFSforSearchedPersons(){
        BFSAlgorithmForUserRelations bfsAlgorithmForUserRelations=new BFSAlgorithmForUserRelations();
        List<Driver> drivers=bfsAlgorithmForUserRelations.bfsTraversal(relations,driver1,"Adrian");
        assertEquals(3, drivers.size());
        assertTrue(drivers.contains(driver1));
        assertTrue(drivers.contains(driver2));
        assertTrue(drivers.contains(driver3));
        drivers=bfsAlgorithmForUserRelations.bfsTraversal(relations,driver4,"Adrian");
        assertEquals(1, drivers.size());
        assertTrue(drivers.contains(driver5));
    }

    @Test
    void testKosarajuSCCofUsers(){
        KosarajuSCCofUsers kosarajuSCCofUsers=new KosarajuSCCofUsers();
        Set<Driver> drivers=kosarajuSCCofUsers.findSCCs(relations,driver1);
        assertEquals(3, drivers.size());
        assertTrue(drivers.contains(driver1));
        assertTrue(drivers.contains(driver2));
        assertTrue(drivers.contains(driver3));
        drivers=kosarajuSCCofUsers.findSCCs(relations,driver4);
        drivers.remove(driver4);
        assertEquals(0, drivers.size());
    }

}
