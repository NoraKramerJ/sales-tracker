package com.salestracker.model;

import com.salestracker.enums.SaleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sales")
@Schema(description = "A sales record")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Auto-generated identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Name of the customer", example = "Acme Corp")
    private String customerName;

    @Schema(description = "Product sold", example = "Widget Pro")
    private String product;

    @Schema(description = "Sale amount", example = "1499.99")
    private Double amount;

    @Schema(description = "Date the sale was made", example = "2026-07-16")
    private LocalDate saleDate;

    @Schema(description = "Sale status", example = "Open", allowableValues = {"Open", "Closed", "Pending"})
    private String status; // e.g. "Open", "Closed", "Pending"

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_type")
    @Schema(description = "Type of sale", example = "INDIVIDUAL")
    private SaleType saleType; // BULK or INDIVIDUAL

    @Column(name = "phone_number")
    @Schema(description = "Contact number entered per sale", example = "555-123-4567")
    private String phoneNumber;

    @Column(name = "company_website")
    @Schema(description = "BULK only: company webpage", example = "https://acmecorp.com")
    private String companyWebsite;

    @Column(name = "tax_id")
    @Schema(description = "BULK only: company tax ID", example = "12-3456789")
    private String taxId;

    public Sale() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDate getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public SaleType getSaleType() { return saleType; }
    public void setSaleType(SaleType saleType) { this.saleType = saleType; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getCompanyWebsite() { return companyWebsite; }
    public void setCompanyWebsite(String companyWebsite) { this.companyWebsite = companyWebsite; }

    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
}
