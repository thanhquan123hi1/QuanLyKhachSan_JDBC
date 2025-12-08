package Controller;


import View.Dashboard;
import View.LoginForm;

import javax.swing.JOptionPane;

import DAO.UserName;

public class User {

	public void KiemTraDangNhap(String username, String Password) {
		 UserName U = new UserName();
		 if(U.checkLogin(username, Password)) {
			 JOptionPane.showMessageDialog(null, "Đăng nhập thành công!");
			 Dashboard d = new Dashboard();
			 d.setVisible(true);
		 }
		 else {
		        // Sai tài khoản hoặc mật khẩu
		        JOptionPane.showMessageDialog(null, "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		    }
		
	}
	
	public void createTK (String username,String Email ,String Password )
	{
		
		 UserName U = new UserName();
		 if(U.createUser(username, Email, Password)) {
			 JOptionPane.showMessageDialog(null, "Tạo Tài Khoản Thành Công");
			 LoginForm d = new LoginForm();
			 d.setVisible(true);
		 }
		 else {
		        // Sai tài khoản hoặc mật khẩu
		        JOptionPane.showMessageDialog(null, "Lỗi", "Lỗi", JOptionPane.ERROR_MESSAGE);
		    }
	}
	
}
