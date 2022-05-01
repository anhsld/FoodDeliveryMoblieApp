package com.example.quanlydoan.ui.order;

import java.io.Serializable;

public class Order implements Serializable {
    String id, status, total;
    long createDate;

    public Order() {
    }

    public Order(String id, String status, String total, long createDate) {
        this.id = id;
        this.status = status;
        this.total = total;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
