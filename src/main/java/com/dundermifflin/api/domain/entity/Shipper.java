package com.dundermifflin.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "shippers")
public class Shipper {

    @Id
    @Column(name = "shipper_id")
    private Integer shipperId;

    @Column(name = "company_name", nullable = false, length = 200)
    private String companyName;

    @Column(name = "phone", length = 50)
    private String phone;

    public Integer getShipperId() {
        return shipperId;
    }

    public void setShipperId(Integer shipperId) {
        this.shipperId = shipperId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
