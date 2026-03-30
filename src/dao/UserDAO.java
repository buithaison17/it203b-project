package dao;

import models.dto.UserDTO;
import models.entity.User;

import java.util.List;

public interface UserDAO {
    public boolean saveUser(String fullName, String email, String password);

    public User findByEmail(String email);

    public List<UserDTO> findAll(int currentPage);

    public boolean changePassword(int id, String newPassword);

    public boolean deleteUser(int id);

    public UserDTO findById(int id);

    public boolean grantRole(int id, String role);

    public int getTotalPage();

    public boolean updateBalance(int userId, double amount);
}
