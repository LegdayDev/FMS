package domain;

public class Users {
    private String username;
    private String password;
    private String role; // admin or player
    private String address;

    public Users() {
    }

    public Users(String username, String password, String role, String address) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getAddress() {
        return address;
    }
}
