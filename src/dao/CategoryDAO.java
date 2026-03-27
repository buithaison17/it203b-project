package dao;

import models.entity.Category;

import java.util.List;

public interface CategoryDAO {
    public boolean saveCategory(String name, String description);

    public boolean updateCategory(int id, String name, String description);

    public boolean deleteCategory(int id);

    public List<Category> findAll();

    public Category findById(int id);
}
