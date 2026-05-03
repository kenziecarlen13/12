package org.week.keshi.data;

public class TicketTier {
    private int id;
    private String tierName;
    private Double price;
    private Integer availability;

    public TicketTier(String tierName, Double price) {
        this.tierName = tierName;
        this.price = price;
    }

    public TicketTier(int id, String tierName, Double price) {
        this.id = id;
        this.tierName = tierName;
        this.price = price;
    }

    public TicketTier(int id, String tierName, Double price, Integer availability) {
        this.id = id;
        this.tierName = tierName;
        this.price = price;
        this.availability = availability;
    }

    public TicketTier(String tierName, Double price, Integer availability) {
        this.tierName = tierName;
        this.price = price;
        this.availability = availability;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }
}

