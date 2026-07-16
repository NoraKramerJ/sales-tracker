package com.salestracker.service;

import com.salestracker.model.Sale;
import com.salestracker.enums.SaleType;
import com.salestracker.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    // Sales over this amount are always recorded as "Individual".
    private static final double AMOUNT_LIMIT = 400;

    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    // Enforce the business rule: amount over the limit forces sale type to Individual.
    private void applyTypeRule(Sale sale) {
        if (sale.getAmount() != null && sale.getAmount() > AMOUNT_LIMIT) {
            sale.setSaleType(SaleType.BULK);
        }
    }

    public List<Sale> getAll() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getById(Long id) {
        return saleRepository.findById(id);
    }

    public Sale create(Sale sale) {
        applyTypeRule(sale);
        return saleRepository.save(sale);
    }

    public Sale update(Long id, Sale updated) {
        return saleRepository.findById(id).map(sale -> {
            sale.setCustomerName(updated.getCustomerName());
            sale.setProduct(updated.getProduct());
            sale.setAmount(updated.getAmount());
            sale.setSaleDate(updated.getSaleDate());
            sale.setStatus(updated.getStatus());
            sale.setSaleType(updated.getSaleType());
            sale.setPhoneNumber(updated.getPhoneNumber());
            applyTypeRule(sale);
            return saleRepository.save(sale);
        }).orElseThrow(() -> new RuntimeException("Sale not found: " + id));
    }

    public void delete(Long id) {
        saleRepository.deleteById(id);
    }
}
