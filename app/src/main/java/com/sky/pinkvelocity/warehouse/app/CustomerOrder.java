package com.sky.pinkvelocity.warehouse.app;

/**
 * Created by hac10 on 30/09/15.
 */
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Jo on 28/09/2015.
 */

public class CustomerOrder implements Order {
    private static long orderNumberCounter = 0;
    private final long orderNumber = setOrderNumber();
    private DateTime dateOrderPlaced;
    private Map<Product, Integer> productsOrdered;
    private Map<Product, Map<Date, Integer>> productsDispatched;
    private Map<Product, Map<Date, Integer>> productsDelivered;
    private BigDecimal totalPrice;
    private Customer customer;

    public CustomerOrder(Map<Product, Integer> productsOrdered, BigDecimal totalPrice, Customer customer) {
        this.dateOrderPlaced  = new DateTime();
        this.productsOrdered = productsOrdered;
        this.productsDispatched = null;
        this.productsDelivered = null;
        this.totalPrice = totalPrice;
        this.customer = customer;
    }

    //sets the order number and increments the orderNumberCounter
    private long setOrderNumber() {
        orderNumberCounter += 1;
        return orderNumberCounter;
    }

//    private BigDecimal getTotalPrice(Map<Product, Integer> productsOrdered){
//        BigDecimal totalPrice = BigDecimal.ZERO;
//        for(Map.Entry<Product, Integer> entry : productsOrdered.entrySet()){
//            int quantity = entry.getValue();
//            totalPrice.add(entry.getKey().getPrice().multiply(new BigDecimal(quantity)));
//        }
//        return totalPrice;
//    }

    @Override
    public void orderProducts(List<Product> productsToOrder, Integer quantity) {
        //not required
    }


    @Override
    public Map<Product, Integer> getProductsOrdered() {
        return productsOrdered;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public DateTime getDateOrderPlaced() {
        return dateOrderPlaced;
    }

    @Override
    public void setProductDispatched(Product product, Integer quantity) {

    }

    @Override
    public Map<Product, Map<Date, Integer>> getDispatchedProducts() {
        return productsDispatched;
    }

    @Override
    public void productDelivered(Product product, Integer quantity) {

    }

    @Override
    public Map<Product, Map<Date, Integer>> getDeliveredProducts() {
        return productsDelivered;
    }

    public static long getOrderNumberCounter(){
        return orderNumberCounter;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public Map<Product, Map<Date, Integer>> getProductsDelivered() {
        return productsDelivered;
    }

    public void setProductsDelivered(Map<Product, Map<Date, Integer>> productsDelivered) {
        this.productsDelivered = productsDelivered;
    }

    public BigDecimal getTotalPrice(BigDecimal totalPrice) {
        return totalPrice;
    }
}