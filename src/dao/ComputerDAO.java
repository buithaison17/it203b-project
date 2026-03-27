package dao;

import models.entity.Computer;
import enums.ComputerStatus;

import java.util.List;

public interface ComputerDAO {
    public boolean saveComputer(String name, String configuration, int categoryId, double price, ComputerStatus statusdAt);

    public Computer findComputerById(int id);

    public boolean updateComputer(int id, String name, String configuration, int categoryId, double price, ComputerStatus status);

    public boolean deleteComputer(int id);

    public List<Computer> findAll();
}
