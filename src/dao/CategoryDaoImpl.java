package dao;

import models.entity.Category;
import utils.Config;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    private static CategoryDaoImpl instance;

    private CategoryDaoImpl() {
    }

    public static CategoryDaoImpl getInstance() {
        if (instance == null) {
            instance = new CategoryDaoImpl();
        }
        return instance;
    }

    @Override
    public boolean saveCategory(String name, String description) {
        String saveCategorySql = "insert into categories (name, description) values (?, ?)";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmSaveCategory = connection.prepareStatement(saveCategorySql)) {
            stmSaveCategory.setString(1, name);
            stmSaveCategory.setString(2, description);
            return stmSaveCategory.executeUpdate() > 0;
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Lỗi khi thêm khu vực", Config.RESET);
        }
        return false;
    }

    @Override
    public boolean updateCategory(int id, String name, String description) {
        String updateCategorySql = "update categories set name = ?, description = ? where id = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmUpdateCategory = connection.prepareStatement(updateCategorySql)) {
            stmUpdateCategory.setString(1, name);
            stmUpdateCategory.setString(2, description);
            stmUpdateCategory.setInt(3, id);
            return stmUpdateCategory.executeUpdate() > 0;
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Lỗi khi cập nhật khu vực", Config.RESET);
        }
        return false;
    }

    @Override
    public boolean deleteCategory(int id) {
        String deleteCategorySql = "delete from categories where id = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmDeleteCategory = connection.prepareStatement(deleteCategorySql)) {
            stmDeleteCategory.setInt(1, id);
            return stmDeleteCategory.executeUpdate() > 0;
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Lỗi khi xóa khu vực", Config.RESET);
        }
        return false;
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String findAllCategoriesSql = "select id, name, description from categories";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmFindAllCategories = connection.prepareStatement(findAllCategoriesSql)) {
            ResultSet resultSet = stmFindAllCategories.executeQuery();
            while (resultSet.next()) {
                categories.add(mapToCategory(resultSet));
            }
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Lỗi khi lấy danh sách khu vực", Config.RESET);
        }
        return categories;
    }

    private Category mapToCategory(ResultSet resultSet) throws SQLException {
        return new Category(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description")
        );
    }

    @Override
    public Category findById(int id) {
        Category category = null;
        String findByIdSql = "select id, name, description from categories where id = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(findByIdSql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                category = mapToCategory(resultSet);
            }
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Lỗi khi tìm khu vực", Config.RESET);
        }
        return category;
    }
}
