 package QuanLyKhachSan;

import View.LoginForm;
import javax.swing.SwingUtilities;
import DAO.PhongDAO;

public class QuanLyKhachSan {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm frame = new LoginForm();
            frame.setVisible(true);
        }); 	
        
    }
}