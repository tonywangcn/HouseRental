/*
* CLientModel.java
* Programmer: Yongtao Wang
* v 1.01
* Model logic for Client
*/

package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.DBConnect;

public class ClientModel extends DBConnect {
	// table name
	private String tableName = "ywang536_rentals"; 

	private int id;
	private int userId;
	private String checkInAt;
	private String checkOutAt;
	private Boolean isPayed;
	private int totalDay;
	
	// Declare DB objects
	DBConnect conn = null;
	Statement stmt = null;

	public ClientModel() {

		conn = new DBConnect();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCheckInAt() {
		return this.checkInAt;
	}

	public void setCheckInAt(String checkInAt) {
		this.checkInAt = checkInAt;
	}

	public String getCheckOutAt() {
		return this.checkOutAt;
	}

	public void setCheckOutAt(String checkOutAt) {
		this.checkOutAt = checkOutAt;
	}

	public Boolean isIsPayed() {
		return this.isPayed;
	}

	public Boolean getIsPayed() {
		return this.isPayed;
	}

	public void setIsPayed(Boolean isPayed) {
		this.isPayed = isPayed;
	}

	public int getTotalDay() {
		return this.totalDay;
	}

	public void setTotalDay(int totalDay) {
		this.totalDay = totalDay;
	}


	public void initTable() {
		String query = String.format(	"CREATE TABLE IF NOT EXISTS %s (id INTEGER NOT NULL AUTO_INCREMENT, "+
										"userId INT(12) NOT NULL,"+
										"checkInAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "+
										"checkOutAt TIMESTAMP DEFAULT '0000-00-00 00:00:00'," +
										"isPayed BOOLEAN DEFAULT false," +
										"totalDay INT(12) DEFAULT 0,"+
										"PRIMARY KEY (`id`))",tableName );
		try { 
			PreparedStatement stmt = conn.getConnection().prepareStatement(query);
			stmt.executeUpdate();

		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public Boolean hasUser(int userId) {
		try {
			String sql = "SELECT COUNT(1) from " + tableName + " where id=? ;";
			PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;

	}


	public Boolean hasUnCheckIn(int userId) {
		try {
			String sql = "SELECT COUNT(*) from " + tableName + " where userId=? AND checkInAt IS NOT NULL AND checkOutAt = '0000-00-00 00:00:00' ;";
			PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}


	public String checkIn(int userId) {
		if (hasUnCheckIn(userId)){
			return "Please check out first!";
		}
		try {
			System.out.println(String.format("User %d is checking in!", userId));
			String sql  = "INSERT INTO " + tableName + "(userId ) values(?) ";
			PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
			stmt.setInt(1, userId);
			stmt.executeUpdate();
			return "";
		} catch (SQLException e) {
			e.printStackTrace();
			return "Something wrong happend";
		}
	}


	public int calculateTotal(int userId) {
		try {
			String sql = "SELECT DATEDIFF( now(), checkInAt)/86400 as days from " + tableName+ " where userId = ?";
			PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String checkOut(int userId) {
		try {
			int total = calculateTotal(userId);
			// minimum day to pay is 1
			total = total >= 1 ? total: 1;

			System.out.println(String.format("User %d is checking out, total days is %d!", userId, total ));
			String sql = "UPDATE " + tableName + " SET checkOutAt = CURRENT_TIMESTAMP, totalDay = ? where isPayed = true AND userId = ? and checkOutAt = '0000-00-00 00:00:00' ";
			PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
			stmt.setInt(1, total);
			stmt.setInt(2, userId);
			int updated = stmt.executeUpdate();
			if (updated != 0) {
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Please check in or pay the fee first, then ask admin to check it for you!";
	}

	public List<ClientModel> getRentalHistory(int userId) {
		List<ClientModel> results = new ArrayList<>();
		try {
			String sql = "SELECT * FROM " + tableName + " where userId=?";
			PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
			stmt.setInt(1, userId);
			ResultSet rs =  stmt.executeQuery();
			while (rs.next()) {
				ClientModel client = new ClientModel();
				client.setId(rs.getInt(1));
				client.setUserId(rs.getInt(2));
				client.setCheckInAt( rs.getString(3) );
				client.setCheckOutAt( rs.getString(4));
				client.setIsPayed( rs.getBoolean(5));
				client.setTotalDay( rs.getInt(6));
				results.add(client);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;	
	}

	public List<ClientModel> getUnpayedRentals() {
        List<ClientModel> results = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + tableName + " where isPayed = false";
            PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ClientModel client = new ClientModel();
				client.setId(rs.getInt(1));
				client.setUserId(rs.getInt(2));
				client.setCheckInAt( rs.getString(3) );
				client.setCheckOutAt( rs.getString(4));
				client.setIsPayed( rs.getBoolean(5));
				client.setTotalDay( rs.getInt(6));
				results.add(client);
			}
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
		System.out.println(results);
        return results;
    }

    public List<ClientModel> getAllRentals() {
        List<ClientModel> results = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + tableName;
            PreparedStatement stmt = conn.getConnection().prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ClientModel client = new ClientModel();
				client.setId(rs.getInt(1));
				client.setUserId(rs.getInt(2));
				client.setCheckInAt( rs.getString(3) );
				client.setCheckOutAt( rs.getString(4));
				client.setIsPayed( rs.getBoolean(5));
				client.setTotalDay( rs.getInt(6));
				results.add(client);
			}
        } catch (SQLException e) {
            e.printStackTrace();
        }
		System.out.println(results);
        return results;
    }
}