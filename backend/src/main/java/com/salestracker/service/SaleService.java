package com.salestracker.service;

import com.salestracker.model.Sale;
import com.salestracker.model.UserType;
import com.salestracker.repository.SaleRepository;
import com.salestracker.repository.UserTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final UserTypeRepository userTypeRepository;

    public SaleService(SaleRepository saleRepository, UserTypeRepository userTypeRepository) {
        this.saleRepository = saleRepository;
        this.userTypeRepository = userTypeRepository;
    }

    // --- Sale methods ---

    public List<Sale> getAll() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getById(Long id) {
        return saleRepository.findById(id);
    }

    public Sale create(Sale sale) {
        // If a userType with an id is provided, resolve it from the DB before saving
        if (sale.getUserType() != null && sale.getUserType().getId() != null) {
            UserType userType = userTypeRepository.findById(sale.getUserType().getId())
                    .orElseThrow(() -> new RuntimeException("UserType not found: " + sale.getUserType().getId()));
            sale.setUserType(userType);
        }
        return saleRepository.save(sale);
    }

    public Sale update(Long id, Sale updated) {
        return saleRepository.findById(id).map(sale -> {
            sale.setCustomerName(updated.getCustomerName());
            sale.setProduct(updated.getProduct());
            sale.setAmount(updated.getAmount());
            sale.setSaleDate(updated.getSaleDate());
            sale.setStatus(updated.getStatus());
            // Update the userType relationship if provided
            if (updated.getUserType() != null && updated.getUserType().getId() != null) {
                UserType userType = userTypeRepository.findById(updated.getUserType().getId())
                        .orElseThrow(() -> new RuntimeException("UserType not found: " + updated.getUserType().getId()));
                sale.setUserType(userType);
            } else {
                sale.setUserType(null);
            }
            return saleRepository.save(sale);
        }).orElseThrow(() -> new RuntimeException("Sale not found: " + id));
    }

    public void delete(Long id) {
        saleRepository.deleteById(id);
    }

    // --- UserType methods ---

    public List<UserType> getAllUserTypes() {
        return userTypeRepository.findAll();
    }

    public Optional<UserType> getUserTypeById(Long id) {
        return userTypeRepository.findById(id);
    }
}
