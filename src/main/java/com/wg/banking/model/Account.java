package com.wg.banking.model;

import java.util.Date;

public class Account {
    private String accountNo;
    private double balance;
    private String ownerId;
    private String branchId;
    private Date createdAt;
    private Date updatedAt;

    // Default constructor
    public Account() {}

    // Parameterized constructor
    public Account(String accountNo, double balance, String ownerId, String branchId, Date createdAt, Date updatedAt) {
        this.accountNo = accountNo;
        this.balance = balance;
        this.ownerId = ownerId;
        this.branchId = branchId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNo='" + accountNo + '\'' +
                ", balance=" + balance +
                ", ownerId='" + ownerId + '\'' +
                ", branchId='" + branchId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
