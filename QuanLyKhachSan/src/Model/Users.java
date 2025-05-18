package Model;

public class Users {
    private int id;
    private String username;
    private String email;
    private String passwordHash;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Users(int id, String username, String email, String passwordHash) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.passwordHash = passwordHash;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
}
