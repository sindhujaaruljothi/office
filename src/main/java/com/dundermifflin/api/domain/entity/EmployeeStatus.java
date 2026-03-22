package com.dundermifflin.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_statuses")
public class EmployeeStatus {

    @Id
    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "status_code", nullable = false, length = 20)
    private String statusCode;

    @Column(name = "status_name", nullable = false, length = 50)
    private String statusName;

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
