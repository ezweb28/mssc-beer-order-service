package skim.sfg.beer.order.service.sm.actions;

import skim.sfg.beer.order.service.domain.BeerOrderEventEnum;
import skim.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import skim.sfg.beer.order.service.services.BeerOrderManagerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ValidationFailureAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {
        String beerOrderId = (String) context.getMessage().getHeaders().get(BeerOrderManagerImpl.ORDER_ID_HEADER);
        log.error("Validation Failed, Beer Order ID: " + beerOrderId);

    }
}
