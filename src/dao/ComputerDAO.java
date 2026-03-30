package dao;

import models.dto.ComputerDTO;
import models.entity.Computer;
import enums.ComputerStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ComputerDAO {
    public boolean saveComputer(String name, String configuration, int categoryId, double price, String statusdAt);

    public Computer findComputerById(int id);

    public boolean updateComputer(int id, String name, String configuration, int categoryId, double price, String status);

    public boolean deleteComputer(int id);

    public List<Computer> findAll(int currentPage);

    public int getTotalPage();

    public List<ComputerDTO> getListComputerCanBook(int currentPage, LocalDateTime startTime, LocalDateTime endTime);
}
