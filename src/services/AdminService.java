package services;

import entity.User;

import java.util.List;

public interface AdminService {

    public List<User> findAll();

    public void saveUser(String fullName, String email, String password);

    public void updateUser(User user);

    public void deleteUser(User user);

    public void grantRole(User user, String role);

    public String generateUserReport();
}
