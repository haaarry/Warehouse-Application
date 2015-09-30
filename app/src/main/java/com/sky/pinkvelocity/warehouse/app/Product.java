package com.sky.pinkvelocity.warehouse.app;

/**
 * Created by hac10 on 30/09/15.
 */


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Product{

    private int id;
    private String productCode;
    private int productTypeId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private int stockLevel = -100;
    private int reorderLevel;
    private List<Supplier> suppliers = new ArrayList<Supplier>();
    private boolean discontinued = false;
    private String shelfLocation = null;
    private String rowLocation = null;

    public Product(int id, int tid, String productCode, String name, String description, BigDecimal price, String imageUrl){
        this.id = id;
        this.productTypeId = tid;
        this.productCode = productCode;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addProductSupplier(Supplier supplier){
        suppliers.add(supplier);
    }

    public List<Supplier> getProductSuppliers() {
        return suppliers;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public int adjustStockLevel(int numOfItemsBought) {
        this.stockLevel -= numOfItemsBought;
        return stockLevel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTid() {
        return productTypeId;
    }

    public void setTid(int tid) {
        this.productTypeId = tid;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public boolean isDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(boolean discontinued) {
        this.discontinued = discontinued;
    }

    @Override
    public String toString() {
        return "ProductId: " + id
                + ", name "
                + name
                + ", imageUrl "
                + imageUrl
                + ", shelf "
                + shelfLocation
                + ", row "
                + rowLocation;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public void setShelfLocation(String shelfLocation) {
        this.shelfLocation = shelfLocation;
    }

    public String getRowLocation() {
        return rowLocation;
    }

    public void setRowLocation(String rowLocation) {
        this.rowLocation = rowLocation;
    }
}




