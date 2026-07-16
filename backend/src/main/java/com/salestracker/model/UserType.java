package com.salestracker.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_types")
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name", nullable = false, unique = true)
    private String typeName;  // e.g. "Inside Sales", "Field Sales", "Account Manager"

    @Column(name = "phone_number")
    private String phoneNumber;

    // One UserType can have MANY Sales records
    @OneToMany(mappedBy = "userType", fetch = FetchType.LAZY)
    private List<Sale> sales;

    public UserType() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public List<Sale> getSales() { return sales; }
    public void setSales(List<Sale> sales) { this.sales = sales; }
}
