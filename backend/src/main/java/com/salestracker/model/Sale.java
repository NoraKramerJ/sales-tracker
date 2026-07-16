package com.salestracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String product;
    private Double amount;
    private LocalDate saleDate;
    private String status; // e.g. "Open", "Closed", "Pending"

    // Many Sales belong to One UserType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_type_id")  // matches the FK column in the sales table
    private UserType userType;

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

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }
}
