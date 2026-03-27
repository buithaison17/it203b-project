package utils;

public class AcceptChoice {
    public static boolean accpect(String title) {
        while (true) {
            System.out.println(title);
            System.out.println("1. Xác nhận");
            System.out.println("2. Không");
            int choice = InputMethod.getIntegerPositive("Lựa chọn: ");
            switch (choice) {
                case 1:
                    return true;
                case 2:
                    return false;
                default:

                    System.out.printf("%s%s%s\n", Config.RED, "Chức năng không hợp lệ", Config.RESET);
            }
        }
    }
}
