package skim.sfg.beer.order.service.services.listeners;

import skim.sfg.beer.order.service.config.JmsConfig;
import skim.sfg.beer.order.service.services.BeerOrderManager;
import skim.sfg.brewery.model.events.AllocateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderAllocationResultListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_RESPONSE_QUEUE)
    public void listen(AllocateOrderResult result) {
        if(!result.getAllocationError() && !result.getPendingInventory()) {
            // allocated normally
            beerOrderManager.beerOrderAllocationPassed(result.getBeerOrderDto());
        } else if(!result.getAllocationError() && result.getPendingInventory()) {
            // pending inventory
            beerOrderManager.beerOrderAllocationPendingInventory(result.getBeerOrderDto());
        } else if(result.getAllocationError()) {
            // allocation error
            beerOrderManager.beerOrderAllocationFailed(result.getBeerOrderDto());
        }

    }
}
