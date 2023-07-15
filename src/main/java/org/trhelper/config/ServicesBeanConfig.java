package org.trhelper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.trhelper.service.user.ClientService;
import org.trhelper.service.user.PlacedOrderService;
import org.trhelper.service.user.UserCommonService;
import org.trhelper.service.user.implementation.ClientServiceImpl;
import org.trhelper.service.user.DriverService;
import org.trhelper.service.user.implementation.DriverServiceImpl;
import org.trhelper.service.user.implementation.PlacedOrderServiceImpl;
import org.trhelper.service.user.implementation.UserCommonServiceImpl;
import org.trhelper.service.utils.graphs.BFSAlgorithmForUserRelations;
import org.trhelper.service.utils.graphs.KosarajuSCCofUsers;


@Configuration
public class ServicesBeanConfig {

    @Bean
    public ClientService clientService() {
        return new ClientServiceImpl();
    }

    @Bean
    public DriverService driverService() {
        return new DriverServiceImpl();
    }

    @Bean
    public UserCommonService userCommonService() {
        return new UserCommonServiceImpl();
    }

    @Bean
    public PlacedOrderService placedOrderService() {return new PlacedOrderServiceImpl();}


}
