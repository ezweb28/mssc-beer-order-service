package skim.sfg.beer.order.service.sm.actions;

import skim.sfg.beer.order.service.config.JmsConfig;
import skim.sfg.beer.order.service.domain.BeerOrder;
import skim.sfg.beer.order.service.domain.BeerOrderEventEnum;
import skim.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import skim.sfg.beer.order.service.repositories.BeerOrderRepository;
import skim.sfg.beer.order.service.services.BeerOrderManagerImpl;
import skim.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import skim.sfg.brewery.model.events.AllocateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AllocateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final JmsTemplate jmsTemplate;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {
        String beerOrderId = (String) context.getMessage().getHeaders().get(BeerOrderManagerImpl.ORDER_ID_HEADER);
        Optional<BeerOrder> beerOrderOptional = beerOrderRepository.findById(UUID.fromString(beerOrderId));
        beerOrderOptional.ifPresentOrElse(beerOrder -> {
            jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_QUEUE,
                    AllocateOrderRequest.builder()
                    .beerOrderDto(beerOrderMapper.beerOrderToDto(beerOrder))
                    .build());

            log.debug("Sent Allocation Request for order id: " + beerOrderId);
        }, () -> log.error("Order Not Found. Id: " + beerOrderId));


    }
}
