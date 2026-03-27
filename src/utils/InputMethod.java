package utils;

import dao.UserDAOImpl;
import services.AdminServiceImpl;

import java.util.Scanner;

public class InputMethod {
    private static final Scanner sc = new Scanner(System.in);

    // Nhập chuỗi bình thường
    public static String getString(String title) {
        while (true) {
            System.out.print(title);
            String input = sc.nextLine();
            if (input.isBlank()) {
                System.out.println("Không được bỏ trống");
            } else if (input.length() < 6) {
                System.out.println("Tối thiểu 6 kí tự");
            } else {
                return input;
            }
        }
    }

    // Nhập chuỗi có thể bỏ qua dùng khi chỉnh sửa
    public static String getString(String title, String oldValue) {
        while (true) {
            System.out.print(title);
            String input = sc.nextLine();
            if (oldValue != null && input.isBlank()) {
                return oldValue;
            }
            if (input.isBlank()) {
                System.out.println("Không được bỏ trống");
            } else if (input.length() < 6) {
                System.out.println("Tối thiểu 6 kí tự");
            } else {
                return input;
            }
        }
    }

    // Nhập chuỗi tối thiểu 3 ký tự
    public static String getStringAtLeastThreeCharacters(String title) {
        while (true) {
            System.out.print(title);
            String input = sc.nextLine();
            if (input.isBlank()) {
                System.out.println("Không được bỏ trống");
            } else if (input.length() < 3) {
                System.out.println("Tối thiểu 3 ký tự");
            } else {
                return input;
            }
        }
    }

    public static String getStringAtLeastThreeCharacters(String title, String oldValue) {
        while (true) {
            System.out.print(title);
            String input = sc.nextLine();
            if (oldValue != null && input.isBlank()) {
                return oldValue;
            }
            if (input.isBlank()) {
                System.out.println("Không được bỏ trống");
            } else if (input.length() < 3) {
                System.out.println("Tối thiểu 3 ký tự");
            } else {
                return input;
            }
        }
    }

    // Nhập số thực
    public static double getDoubleInteger(String title) {
        while (true) {
            System.out.print(title);
            try {
                double input = Double.parseDouble(sc.nextLine());
                if (input < 0) {
                    System.out.println("Nhập số lớn hơn 0");
                } else {
                    return input;
                }
            } catch (NumberFormatException e) {
                System.out.println("Nhập không hợp lệ");
            }
        }
    }

    // Nhập số thực có thể bỏ qua
    public static double getDoubleInteger(String title, double oldValue) {
        while (true) {
            System.out.print(title);
            String input = sc.nextLine();
            if (oldValue >= 0 && input.isBlank()) {
                return oldValue;
            }
            try {
                double number = Double.parseDouble(input);
                if (number < 0) {
                    System.out.println("Nhập số lớn hơn 0");
                } else {
                    return number;
                }
            } catch (NumberFormatException e) {
                System.out.println("Nhập không hợp lệ");
            }
        }
    }

    // Nhập số nguyên
    public static int getIntegerPositive(String title) {
        while (true) {
            System.out.print(title);
            try {
                int number = Integer.parseInt(sc.nextLine());
                if (number < 0) {
                    System.out.println("Nhập số lớn hơn 0");
                } else {
                    return number;
                }
            } catch (NumberFormatException e) {
                System.out.println("Nhập không hợp lệ");
            }
        }
    }

    public static int getIntegerPositive(String title, int oldValue) {
        while (true) {
            System.out.print(title);
            String input = sc.nextLine();
            if (oldValue >= 0 && input.isBlank()) {
                return oldValue;
            }
            try {
                int number = Integer.parseInt(input);
                if (number < 0) {
                    System.out.println("Nhập số lớn hơn 0");
                } else {
                    return number;
                }
            } catch (NumberFormatException e) {
                System.out.println("Nhập không hợp lệ");
            }
        }
    }
}
