package services;

import dao.UserDAOImpl;
import entity.User;

import java.util.List;

public class AdminServiceImpl implements AdminService {
    private static AdminServiceImpl instance;
    private final UserDAOImpl userDAO = UserDAOImpl.getInstance();

    private AdminServiceImpl() {
    }

    public static AdminServiceImpl getInstance() {
        if (instance == null) instance = new AdminServiceImpl();
        return instance;
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public void saveUser(String fullName, String email, String password) {

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void grantRole(User user, String role) {

    }

    @Override
    public String generateUserReport() {
        return "";
    }
}
