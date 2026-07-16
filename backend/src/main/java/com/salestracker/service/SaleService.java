package com.salestracker.service;

import com.salestracker.model.Sale;
import com.salestracker.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public List<Sale> getAll() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getById(Long id) {
        return saleRepository.findById(id);
    }

    public Sale create(Sale sale) {
        return saleRepository.save(sale);
    }

    public Sale update(Long id, Sale updated) {
        return saleRepository.findById(id).map(sale -> {
            sale.setCustomerName(updated.getCustomerName());
            sale.setProduct(updated.getProduct());
            sale.setAmount(updated.getAmount());
            sale.setSaleDate(updated.getSaleDate());
            sale.setStatus(updated.getStatus());
            return saleRepository.save(sale);
        }).orElseThrow(() -> new RuntimeException("Sale not found: " + id));
    }

    public void delete(Long id) {
        saleRepository.deleteById(id);
    }
}
