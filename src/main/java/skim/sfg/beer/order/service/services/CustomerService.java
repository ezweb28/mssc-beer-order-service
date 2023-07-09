package skim.sfg.beer.order.service.services;

import org.springframework.data.domain.Pageable;
import skim.sfg.brewery.model.CustomerPagedList;

public interface CustomerService {

    CustomerPagedList listCustomers(Pageable pageable);
}
