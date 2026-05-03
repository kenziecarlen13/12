package org.week.lany.data;

public class Ticket {
    private int id;
    private String sectionName;
    private Double price;
    private Integer stock;

    public Ticket(String sectionName, Double price) {
        this.sectionName = sectionName;
        this.price = price;
    }

    public Ticket(int id, String sectionName, Double price) {
        this.id = id;
        this.sectionName = sectionName;
        this.price = price;
    }

    public Ticket(int id, String sectionName, Double price, Integer stock) {
        this.id = id;
        this.sectionName = sectionName;
        this.price = price;
        this.stock = stock;
    }

    public Ticket(String sectionName, Double price, Integer stock) {
        this.sectionName = sectionName;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}

