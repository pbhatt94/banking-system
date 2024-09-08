package com.wg.banking.model;

import java.util.Date;

public class Branch {
    private String branchId;          
    private String branchName;
    private String branchAddress;
    private String branchManagerId;       
    private Date createdAt;
    private Date updatedAt;
    
    public Branch() {}

    public Branch(String branchId, String branchName, String branchAddress, String branchManagerId, Date createdAt, Date updatedAt) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.branchAddress = branchAddress;
        this.branchManagerId = branchManagerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getBranchManagerId() {
        return branchManagerId;
    }

    public void setBranchManagerId(String branchManager) {
        this.branchManagerId = branchManager;
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
        return "Branch{" +
                "branchId='" + branchId + '\'' +
                ", branchName='" + branchName + '\'' +
                ", branchAddress='" + branchAddress + '\'' +
                ", branchManager=" + branchManagerId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
