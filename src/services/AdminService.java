package services;

import models.dto.UserDTO;
import models.entity.User;

import java.util.List;

public interface AdminService {

    public void findAll();

    public void saveUser();

    public void updateUser();

    public void deleteUser();

    public void grantRole();

    public void generateUserReport();
}
