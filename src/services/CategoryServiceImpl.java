package services;

import dao.CategoryDaoImpl;
import models.entity.Category;
import utils.AcceptChoice;
import utils.Config;
import utils.InputMethod;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private static CategoryServiceImpl instance;

    private CategoryServiceImpl() {
    }

    public static CategoryServiceImpl getInstance() {
        if (instance == null) {
            instance = new CategoryServiceImpl();
        }
        return instance;
    }

    private final CategoryDaoImpl categoryDao = CategoryDaoImpl.getInstance();

    @Override
    public void addCategory() {
        String name = InputMethod.getStringAtLeastThreeCharacters("Nhập tên khu vực: ");
        String description = InputMethod.getString("Nhập mô tả: ");
        boolean result = categoryDao.saveCategory(name, description);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Thêm khu vực thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Thêm khu vực thất bại", Config.RESET);
        }
    }

    @Override
    public void findAll() {
        int totalPage = categoryDao.getTotalPage();
        if (totalPage == 0) {
            System.out.printf("%s%s%s\n", Config.YELLOW, "Không có khu vực", Config.RESET);
            return;
        }
        int choice;
        int currentPage = 1;
        do {
            List<Category> categories = categoryDao.findAll(currentPage);
            System.out.println("|----------------------------------------------------------------------------------------|");
            System.out.println("|                                        DANH SÁCH KHU VỰC                               |");
            System.out.println("|----------------------------------------------------------------------------------------|");
            System.out.printf("| %-8s | %-20s | %-30s | %-19s |\n", "ID", "Tên", "Mô tả", "Ngày tạo", "", "");
            System.out.println("|----------------------------------------------------------------------------------------|");
            categories.forEach(System.out::println);
            System.out.println("|----------------------------------------------------------------------------------------|");
            choice = InputMethod.getIntegerPositive("[1] Prev, [2] Next, [3].Exit");
            switch (choice) {
                case 1:
                    currentPage = currentPage > 1 ? currentPage - 1 : currentPage;
                    break;
                case 2:
                    currentPage = currentPage < totalPage ? currentPage + 1 : currentPage;
                    break;
                case 3:
                    break;
                default:
                    System.out.printf("%s%s%s\n", Config.RED, "Lựa chọn không hợp lệ", Config.RESET);
                    break;
            }
        } while (choice != 3);
    }

    @Override
    public void updateCategory() {
        int id = InputMethod.getIntegerPositive("Nhập ID khu vực: ");
        Category category = categoryDao.findById(id);
        // Kiểm tra ID
        if (category == null) {
            System.out.printf("%s%s%s\n", Config.RED, "Khu vực không tồn tại", Config.RESET);
            return;
        }
        // Nhập thông tin mới
        String name = InputMethod.getStringAtLeastThreeCharacters("Nhập tên khu vực mới (Enter để bỏ qua): ", category.getName());
        String description = InputMethod.getString("Nhập mô tả mới (Enter để bỏ qua): ", category.getDescription());
        if (!AcceptChoice.accpect("Xác nhận cập nhật danh mục")) {
            System.out.printf("%s%s%s\n", Config.YELLOW, "Không có thay đổi", Config.RESET);
            return;
        }
        boolean result = categoryDao.updateCategory(id, name, description);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Cập nhật khu vực thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Cập nhật khu vực thất bại", Config.RESET);
        }
    }

    @Override
    public void deleteCategory() {
        int id = InputMethod.getIntegerPositive("Nhập ID: ");
        Category category = categoryDao.findById(id);
        if (category == null) {
            System.out.printf("%s%s%s\n", Config.RED, "Khu vực không tồn tại", Config.RESET);
        }
        if (!AcceptChoice.accpect("Xác nhận xóa khu vực")) {
            System.out.printf("%s%s%s\n", Config.YELLOW, "Không có thay đổi", Config.RESET);
            return;
        }
        boolean result = categoryDao.deleteCategory(id);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Xóa khu vực thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Xóa khu vực thất bại", Config.RESET);
        }

    }
}
