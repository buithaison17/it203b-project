package services;

import dao.ComputerDAOImpl;
import models.entity.Computer;
import utils.AcceptChoice;
import utils.Config;
import utils.InputMethod;
import utils.Validate;

import java.util.List;

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
            if (Validate.validateCategory(categoryId)) {
                System.out.printf("%s%s%s\n", Config.RED, "Khu vực không hợp lệ", Config.RESET);
            } else {
                break;
            }
        }

        String status;
        while (true) {
            status = InputMethod.getString("Nhập trạng thái (available/ unavailable): ");
            if (!Validate.validateComputerStats(status)) {
                System.out.printf("%s%s%s\n", Config.RED, "Trạng thái máy không hợp lệ", Config.RESET);
            } else {
                break;
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
        int totalPage = computerDAO.getTotalPage();
        if (totalPage == 0) {
            System.out.printf("%s%s%s\n", Config.RED, "Danh sách máy trống", Config.RESET);
        }
        int choice;
        int currentPage = 1;
        do {
            List<Computer> computers = computerDAO.findAll(currentPage);
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------|");
            System.out.println("|                                                           DANH SÁCH MÁY                                                              |");
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------|");
            System.out.printf("| %-8s | %-20s | %-30s | %-10s | %-12s | %-15s | %-19s |\n",
                    "ID", "Tên", "Cấu hình", "Khu vực", "Giá thuê/h", "Trạng thái", "Ngày tạo");
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------|");
            computers.forEach(System.out::println);
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------|");
            choice = InputMethod.getIntegerPositive("[1] Prev, [2] Next, [3].Exit");
        } while (choice != 3);
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
            if (Validate.validateCategory(newCategoryId)) {
                System.out.printf("%s%s%s\n", Config.RED, "Khu vực không hợp lệ", Config.RESET);
            } else {
                break;
            }
        }
        String newStatus;
        while (true) {
            newStatus = InputMethod.getString("Nhập trạng thái mới (available/ unavailable) (Enter để bỏ qua): ", computer.getStatus().toString());
            if (!Validate.validateComputerStats(newStatus)) {
                System.out.printf("%s%s%s\n", Config.RED, "Trạng thái máy không hợp lệ", Config.RESET);
            } else {
                break;
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