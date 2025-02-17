package skim.sfg.beer.order.service.sm.actions;

import skim.sfg.beer.order.service.config.JmsConfig;
import skim.sfg.beer.order.service.domain.BeerOrder;
import skim.sfg.beer.order.service.domain.BeerOrderEventEnum;
import skim.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import skim.sfg.beer.order.service.repositories.BeerOrderRepository;
import skim.sfg.beer.order.service.services.BeerOrderManagerImpl;
import skim.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import skim.sfg.brewery.model.events.ValidateOrderRequest;
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
public class ValidateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {
        Optional<String> beerOrderIdOpt = Optional.ofNullable((String) context.getMessage().getHeaders().get(BeerOrderManagerImpl.ORDER_ID_HEADER));
        if(beerOrderIdOpt.isPresent()) {
            String beerOrderId = beerOrderIdOpt.get();
            Optional<BeerOrder> beerOrderOptional = beerOrderRepository.findById(UUID.fromString(beerOrderId));
            beerOrderOptional.ifPresentOrElse(beerOrder -> {
                jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_QUEUE, ValidateOrderRequest.builder()
                        .beerOrder(beerOrderMapper.beerOrderToDto(beerOrder))
                        .build());

                log.debug("Sent Validation request to queue for order id " + beerOrderId);
            }, () -> log.debug("Order Not Found. Id: " + beerOrderId));
        } else {
            log.debug("No beer order id was sent.");
        }
    }
}
