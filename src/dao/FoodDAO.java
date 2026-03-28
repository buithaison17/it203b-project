package dao;

import models.entity.Food;

import java.util.List;

public interface FoodDAO {
    public boolean saveFood(String name, String description, double price, int stock);

    public boolean deleteFood(int foodId);

    public boolean updateFood(int foodId, String name, String description, double price);

    public boolean updateStock(int foodId, int newStock);

    public Food findById(int foodId);

    public List<Food> findAll(int currentPage);

    public int getTotalPage();
}
