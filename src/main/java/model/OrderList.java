package model;

public class OrderList {
    private Order[] orders;
    private int total;
    private int totalToday;

    public boolean ordersNotNull(){
        return orders !=null;
    }
}
