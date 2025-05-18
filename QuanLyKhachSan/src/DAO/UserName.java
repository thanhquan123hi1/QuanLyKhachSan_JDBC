package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.DichVu;
import  Model.Users;
import Util.DatabaseConnection;

public class UserName {
	
	
	
  public List<Users> getAllUsername() {
	  List<Users> U = new ArrayList();
	  try (Connection conn = DatabaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery("SELECT * FROM Users")) {
	            while (rs.next()) {
	            	int ID = rs.getInt("ID");
	                String Username = rs.getString("Username");
	                String Email = rs.getString("Email");
	                String Password = rs.getString("PasswordHash");
	                U.add(new Users(ID,Username,Email,Password));
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException("Lỗi khi lấy danh sách dịch vụ: " + e.getMessage());
	        }
	        return U;
  }
  
  public boolean checkLogin(String username, String password) {
	    List<Users> users = getAllUsername();

	    return users.stream()
	                .anyMatch(u -> u.getUsername().equals(username) && u.getPasswordHash().equals(password));
	}
  
  public boolean createUser(String username, String email, String password) {
      String sql = "INSERT INTO Users (Username, Email, PasswordHash) VALUES (?, ?, ?)";

      try (Connection conn = DatabaseConnection.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {

          stmt.setString(1, username);
          stmt.setString(2, email);
          stmt.setString(3, password); // Có thể hash ở đây nếu cần

          int rowsInserted = stmt.executeUpdate();
          return rowsInserted > 0;

      } catch (Exception e) {
          e.printStackTrace();
          throw new RuntimeException("Lỗi khi tạo tài khoản: " + e.getMessage());
      }
  }

  
  
}
