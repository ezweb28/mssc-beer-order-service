package skim.sfg.beer.order.service.web.mappers;

import org.mapstruct.Mapper;
import skim.sfg.beer.order.service.domain.Customer;
import skim.sfg.brewery.model.CustomerDto;

@Mapper(uses = {DateMapper.class})
public interface CustomerMapper {
    CustomerDto customerToDto(Customer customer);

    Customer dtoToCustomer(CustomerDto dto);
}
