package services;

public interface UserService {
    public boolean register(String fullName, String email, String password);

    public boolean login(String email, String password);
}
