package com.salestracker.controller;

import com.salestracker.model.Sale;
import com.salestracker.model.UserType;
import com.salestracker.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    // --- Sale endpoints ---

    @GetMapping("/api/sales")
    public List<Sale> getAll() {
        return saleService.getAll();
    }

    @GetMapping("/api/sales/{id}")
    public ResponseEntity<Sale> getById(@PathVariable Long id) {
        return saleService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/sales")
    public Sale create(@RequestBody Sale sale) {
        return saleService.create(sale);
    }

    @PutMapping("/api/sales/{id}")
    public ResponseEntity<Sale> update(@PathVariable Long id, @RequestBody Sale sale) {
        try {
            return ResponseEntity.ok(saleService.update(id, sale));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/sales/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- UserType endpoints ---

    @GetMapping("/api/user-types")
    public List<UserType> getAllUserTypes() {
        return saleService.getAllUserTypes();
    }

    @GetMapping("/api/user-types/{id}")
    public ResponseEntity<UserType> getUserTypeById(@PathVariable Long id) {
        return saleService.getUserTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
