/*
* AdminModel.java
* Programmer: Yongtao Wang
* v 1.01
* Model logic for Admin
*/
package models;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dao.DBConnect;

public class AdminModel {
    private String tableName = "ywang536_rentals"; 
    private String usersTableName = "ywang536_users";

	// Declare DB objects
	DBConnect conn = null;
	Statement stmt = null;

	public AdminModel() {

		conn = new DBConnect();
	}

    public ResultSet getAllRegularUsers() {
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM " + usersTableName + "WHERE admin = 0";
            PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
			rs = stmt.executeQuery();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void promoteRegularUserToAdmin(int userId) {
		try {
			String sql = "UPDATE " + usersTableName + " SET admin = 1 where id = ? AND admin = 0";
			PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
			stmt.setInt(1, userId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}        
    }

    public String askClientToPay(int userId) {
		try {
			String sql = "UPDATE " + tableName + " SET isPayed = true where userId = ? AND isPayed = false";
			PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
			stmt.setInt(1, userId);
			if (stmt.executeUpdate() == 0) {
				return "Client failed to pay, please check userid and payment status!";
			}
			return "Client payed successfully!";

		} catch (SQLException e) {
			e.printStackTrace();
			return "Something wrong with database conection, please try again!";
		}
	}

    public String delRental(int rentalId) {
        try {
            String sql = "DELETE FROM " + tableName + " where id=?";
			PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
			stmt.setInt(1, rentalId);
			if (stmt.executeUpdate() == 0) {
				return "Failed to delete rental record!";
			}
			return "Delete rental record successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
			return "Something wrong with database conection, please try again!";
        }
    }

	public String getUnpayedUserIds() {
		String result = "";
		
        try {
			ResultSet rs = null;
            String sql = "SELECT userid FROM " + tableName + " WHERE isPayed = FALSE";
            PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()) {
				if (result.length() == 0) {
					result = rs.getString("userid");
				} else {
					result = result + "," + rs.getString("userid");
				}
			}
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
	}

	public String checkUserPaymentStatus(int userid) {
		try {
            String sql = "SELECT * FROM " + tableName + " where userId=? AND checkInAt is not NULL ORDER BY id DESC limit 1";
			PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
			stmt.setInt(1, userid);
			ResultSet rs = stmt.executeQuery();
			if (rs.next() == false) {
				return "User has never checked in!";
			} else {
				do {
					if (rs.getBoolean("isPayed") ) {
						return "User "+ rs.getString("userId") +" already payed the fee!";
					}
					return "User "+ rs.getString("userId") +" haven't pay the fee yet!";
					
				  } while (rs.next());
			}
        } catch (SQLException e) {
            e.printStackTrace();
			return "Something wrong with database conection, please try again!";
        }
	}

	public String getAllRentalRecords() {
		String result = "\nid userid isPayed totalDay";
		try {
            String sql = "SELECT * FROM " + tableName;
			PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String tmp = rs.getString("id") + "   " + rs.getString("userId") + "      " + rs.getString("isPayed") + "      " + rs.getString("totalday");
				result = result + "\n" + tmp;
			}
        } catch (SQLException e) {
            e.printStackTrace();
			return "Something wrong with database conection, please try again!";
        }
		return result;
	}
}
