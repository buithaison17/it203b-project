package services;

public interface UserService {
    public void register();

    public void login();

    public boolean changePassword(String oldPassword, String newPassword);
}
