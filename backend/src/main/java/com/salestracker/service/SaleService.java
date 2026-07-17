package com.salestracker.service;

import com.salestracker.model.Sale;
import com.salestracker.enums.SaleType;
import com.salestracker.repository.SaleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class SaleService {

    // Business rules:
    //   amount < 30    -> rejected
    //   30 <= amount <= 450  -> INDIVIDUAL
    //   amount > 450   -> BULK
    private static final double  MIN_AMOUNT     = 30;
    private static final double  BULK_THRESHOLD = 450;
    // Accepts: 5551234567 | 555-123-4567 | (555) 123-4567 | (555)123-4567
    private static final Pattern US_PHONE = Pattern.compile(
        "^\\(?[2-9]\\d{2}\\)?[\\s\\-]?[2-9]\\d{2}[\\s\\-]?\\d{4}$"
    );
    // Accepts http:// or https:// URLs with a valid domain
    private static final Pattern URL = Pattern.compile(
        "^https?://[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]*)?$"
    );

    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    private void validateAmount(Double amount) {
        if (amount == null || amount < MIN_AMOUNT) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Sale amount must be at least $" + (int) MIN_AMOUNT + "."
            );
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Phone number is required."
            );
        }
        if (!US_PHONE.matcher(phone.trim()).matches()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid US phone number. Accepted formats: 5551234567, 555-123-4567, (555) 123-4567."
            );
        }
    }

    private void applyTypeRule(Sale sale) {
        if (sale.getAmount() <= BULK_THRESHOLD) {
            sale.setSaleType(SaleType.INDIVIDUAL);
            // clear bulk-only fields when downgraded to individual
            sale.setCompanyWebsite(null);
            sale.setTaxId(null);
        } else {
            sale.setSaleType(SaleType.BULK);
        }
    }

    private void validateBulkFields(Sale sale) {
        if (SaleType.BULK.equals(sale.getSaleType())) {
            if (sale.getCompanyWebsite() == null || sale.getCompanyWebsite().isBlank()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Company website is required for Bulk sales."
                );
            }
            if (!URL.matcher(sale.getCompanyWebsite().trim()).matches()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Company website must be a valid URL starting with http:// or https://."
                );
            }
            if (sale.getTaxId() == null || sale.getTaxId().isBlank()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Tax ID is required for Bulk sales."
                );
            }
        }
    }

    public List<Sale> getAll() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getById(Long id) {
        return saleRepository.findById(id);
    }

    public Sale create(Sale sale) {
        validateAmount(sale.getAmount());
        validatePhone(sale.getPhoneNumber());
        applyTypeRule(sale);          // sets saleType; clears bulk fields if INDIVIDUAL
        validateBulkFields(sale);     // requires website+taxId when BULK
        return saleRepository.save(sale);
    }

    public Sale update(Long id, Sale updated) {
        validateAmount(updated.getAmount());
        validatePhone(updated.getPhoneNumber());
        return saleRepository.findById(id).map(sale -> {
            sale.setCustomerName(updated.getCustomerName());
            sale.setProduct(updated.getProduct());
            sale.setAmount(updated.getAmount());
            sale.setSaleDate(updated.getSaleDate());
            sale.setStatus(updated.getStatus());
            sale.setPhoneNumber(updated.getPhoneNumber());
            sale.setCompanyWebsite(updated.getCompanyWebsite());
            sale.setTaxId(updated.getTaxId());
            applyTypeRule(sale);      // sets saleType; clears bulk fields if now INDIVIDUAL
            validateBulkFields(sale); // requires website+taxId when BULK
            return saleRepository.save(sale);
        }).orElseThrow(() -> new RuntimeException("Sale not found: " + id));
    }

    public Sale patch(Long id, Sale updates) {
        return saleRepository.findById(id).map(sale -> {
            if (updates.getCustomerName() != null) sale.setCustomerName(updates.getCustomerName());
            if (updates.getProduct() != null)      sale.setProduct(updates.getProduct());
            if (updates.getAmount() != null) {
                validateAmount(updates.getAmount());
                sale.setAmount(updates.getAmount());
            }
            if (updates.getSaleDate() != null)     sale.setSaleDate(updates.getSaleDate());
            if (updates.getStatus() != null)       sale.setStatus(updates.getStatus());
            if (updates.getPhoneNumber() != null) {
                validatePhone(updates.getPhoneNumber());
                sale.setPhoneNumber(updates.getPhoneNumber());
            }
            if (updates.getCompanyWebsite() != null) sale.setCompanyWebsite(updates.getCompanyWebsite());
            if (updates.getTaxId() != null)          sale.setTaxId(updates.getTaxId());
            applyTypeRule(sale);
            validateBulkFields(sale);
            return saleRepository.save(sale);
        }).orElseThrow(() -> new RuntimeException("Sale not found: " + id));
    }

    public void delete(Long id) {
        saleRepository.deleteById(id);
    }
}
