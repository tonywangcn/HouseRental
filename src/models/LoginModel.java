/*
* LoginModel.java
* Programmer: Yongtao Wang
* v 1.01
* Model logic for Login
*/

package models;

import java.sql.*;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;

import javax.crypto.SecretKeyFactory;
import java.util.Base64;  

import dao.DBConnect;


public class LoginModel extends DBConnect {
	private String tableName = "ywang536_users"; 
	private Boolean admin;
	private int userId;
	private String userName;

	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Boolean isAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public void initTable() {

			String query = String.format(	"CREATE TABLE IF NOT EXISTS %s (" + 
											"id INTEGER NOT NULL AUTO_INCREMENT, "+
											"username VARCHAR(50) NOT NULL, "+
											"password VARCHAR(50) NOT NULL, "+
											"admin TINYINT(4) NOT NULL, "+
											"UNIQUE KEY username (username)," +
											" PRIMARY KEY (`id`) )",tableName );
			try { 
				PreparedStatement stmt = connection.prepareStatement(query);
				stmt.executeUpdate();
	
			} catch(Exception e) {
				e.printStackTrace();
			}

	}
	// register new user with username and password
	public Boolean register(String username, String password) {
		String query = String.format( "INSERT INTO %s(username, password, admin) VALUES(?,?,0)",tableName );
		try { 
			PreparedStatement stmt = connection.prepareStatement(query);
			// generate a random salt to encrypt password
			SecureRandom random = new SecureRandom();
			byte[] salt = new byte[16];
			random.nextBytes(salt);
			String hash = encryptPassword(password, salt.toString());
			stmt.setString(1, username);
			stmt.setString(2, hash);
			stmt.executeUpdate();
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// hash password with salt and PBKDF2WithHmacSHA1 algorithm
	public static String encryptPassword(String password, String salt) {
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt.getBytes());
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 128);
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = factory.generateSecret(spec).getEncoded();
			return String.format("%s.%s", salt,Base64.getEncoder().encodeToString(hash));
		} catch (Exception e) {
			throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);  
		}
	}

	// compare password with hashed result
	public static Boolean checkPassword(String password, String hash) {
		String salt = hash.split("\\.",2)[0];
		return encryptPassword(password, salt).equals(hash);
	}

	public Boolean getCredentials(String username, String password){
           
           String query =  String.format("SELECT * FROM %s WHERE username = ?;", tableName ) ;
            try(PreparedStatement stmt = connection.prepareStatement(query)) {
               stmt.setString(1, username);
               ResultSet rs = stmt.executeQuery();
                if(rs.next()) { 
					if (checkPassword(password,rs.getString("password"))) {
						setAdmin(rs.getBoolean("admin"));
						setUserId(rs.getInt("id"));
						setUserName(rs.getString("username"));
						return true;
					}
               	}
             }catch (SQLException e) {
            	e.printStackTrace();   
             }
	       return false;
    }

}//end class
