package skim.sfg.beer.order.service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import skim.sfg.beer.order.service.domain.Customer;
import skim.sfg.beer.order.service.repositories.CustomerRepository;
import skim.sfg.beer.order.service.web.mappers.CustomerMapper;
import skim.sfg.brewery.model.CustomerPagedList;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerPagedList listCustomers(Pageable pageable) {

        Page<Customer> customerPage = customerRepository.findAll(pageable);

        return new CustomerPagedList(customerPage
                        .stream()
                        .map(customerMapper::customerToDto)
                        .collect(Collectors.toList()),
                    PageRequest.of(customerPage.getPageable().getPageNumber(),
                        customerPage.getPageable().getPageSize()),
                        customerPage.getTotalElements());
    }
}
