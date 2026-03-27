package services;

import dao.ComputerDAOImpl;
import enums.ComputerStatus;
import exceptions.InvalidCategoryException;
import exceptions.InvalidComputerStatusException;
import models.entity.Computer;
import utils.AcceptChoice;
import utils.Config;
import utils.InputMethod;
import utils.Validate;

public class ComputerServiceImpl implements ComputerService {
    private static ComputerServiceImpl instance;

    private ComputerServiceImpl() {
    }

    public static ComputerServiceImpl getInstance() {
        if (instance == null) {
            instance = new ComputerServiceImpl();
        }
        return instance;
    }

    private final ComputerDAOImpl computerDAO = ComputerDAOImpl.getInstance();

    @Override
    public void addComputer() {
        String name = InputMethod.getStringAtLeastThreeCharacters("Nhập tên máy: ");
        String configuration = InputMethod.getString("Nhập cấu hình máy: ");
        double pricePerHours = InputMethod.getDoubleInteger("Nhập số tiền thuê: ");

        int categoryId;
        while (true) {
            categoryId = InputMethod.getIntegerPositive("Nhập ID khu vực: ");
            try {
                Validate.validateCategory(categoryId);
                break;
            } catch (InvalidCategoryException e) {
                System.out.printf("%s%s%S\n", Config.RED, e.getMessage(), Config.RESET);
            }
        }

        String status;
        while (true) {
            status = InputMethod.getString("Nhập trạng thái (available/ unavailable): ");
            try {
                Validate.validateComputerStats(status);
                break;
            } catch (InvalidComputerStatusException e) {
                System.out.printf("%s%s%S\n", Config.RED, e.getMessage(), Config.RESET);
            }
        }

        boolean result = computerDAO.saveComputer(name, configuration, categoryId, pricePerHours, status);

        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Thêm máy thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Thêm máy thất bại", Config.RESET);
        }
    }

    @Override
    public void findAll() {

    }

    @Override
    public void updateComputer() {
        int id = InputMethod.getIntegerPositive("Nhập ID máy: ");
        Computer computer = computerDAO.findComputerById(id);
        if (computer == null) {
            System.out.printf("%s%s%s\n", Config.RED, "Máy không tồn tại", Config.RESET);
            return;
        }
        String newName = InputMethod.getString("Nhập tên máy mới (Enter để bỏ qua): ", computer.getName());
        String newConfiguration = InputMethod.getString("Nhập cấu hình máy mới (Enter để bỏ qua): ", computer.getConfiguration());
        double newPricePerHours = InputMethod.getDoubleInteger("Nhập số tiền thuê mới (Enter để bỏ qua): ", computer.getPricePerHours());
        int newCategoryId;
        while (true) {
            newCategoryId = InputMethod.getIntegerPositive("Nhập ID khu vực mới (Enter để bỏ qua): ", computer.getCategory().getId());
            try {
                Validate.validateCategory(newCategoryId);
                break;
            } catch (InvalidCategoryException e) {
                System.out.printf("%s%s%s\n", Config.RED, e.getMessage(), Config.RESET);
            }
        }
        String newStatus;
        while (true) {
            newStatus = InputMethod.getString("Nhập trạng thái mới (available/ unavailable) (Enter để bỏ qua): ", computer.getStatus().toString());
            try {
                Validate.validateComputerStats(newStatus);
                break;
            } catch (InvalidComputerStatusException e) {
                System.out.printf("%s%s%s\n", Config.RED, e.getMessage(), Config.RESET);
            }
        }
        if (!AcceptChoice.accpect("Xác nhận cập nhật máy")) {
            System.out.printf("%s%s%s\n", Config.YELLOW, "Không có thay đổi", Config.RESET);
            return;
        }
        boolean result = computerDAO.updateComputer(id, newName, newConfiguration, newCategoryId, newPricePerHours, newStatus);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Cập nhật máy thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Cập nhật máy thất bại", Config.RESET);
        }
    }

    @Override
    public void deleteComputer() {
        int id = InputMethod.getIntegerPositive("Nhập ID máy: ");
        if (!AcceptChoice.accpect("Xác nhận xóa máy: ")) {
            System.out.printf("%s%s%s\n", Config.YELLOW, "Không xóa máy", Config.RESET);
            return;
        }
        boolean result = computerDAO.deleteComputer(id);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Xóa máy thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Xóa máy thất bại", Config.RESET);
        }
    }
}