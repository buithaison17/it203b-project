package dao;

import models.dto.FoodDTO;
import models.dto.OrderDTO;
import models.entity.Order;

import java.util.List;
import java.util.Map;

public interface OrderDAO {
    public boolean placeOrder(Map<Integer, Integer> orderItems, double totalPrice);

    public int getTotalPage();

    public List<OrderDTO> viewListOrder(int currentPage);

    public Order findById(int id);

    public boolean updateStatusOrder(int orderId, String status);

    public int getTotalPageOfUser();

    public List<OrderDTO> viewListOrderOfUser(int currentPage);

    public int getTotalPageOfReport();

    public List<FoodDTO> viewListBestFood(int currentPage);

    public double getRevenue();
}
