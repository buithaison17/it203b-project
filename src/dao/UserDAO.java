package dao;

import entity.User;

import java.util.List;

public interface UserDAO {
    public boolean saveUser(String fullName, String email, String password);

    public User findByEmail(String email);

    public List<User> findAll();
}
