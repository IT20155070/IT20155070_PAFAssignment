package model;

import java.sql.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class BillingModel {
	
	// Creating the db connection
	public Connection connect()
	{
		Connection con = null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

			// Provide the correct details: DBServer/DBName, username, password
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/electrogrid", "root", "");
		}
		catch (Exception e)
		{e.printStackTrace();}
		return con;
	} 
	
	// Create method to insert bill details
	public String insertbillingdata(String account_no, String from_d, String to_d, int current_r, String status)
	{
		String output = "";
		try
		{
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for inserting."; 
			}
			
			// create a prepared statement
			String query = " insert into billing (`ID`,`Account_No`,`Name`,`Address`,`From_Date`,`Previous_Reading`,`To_Date`,`Current_Reading`,`Units`,`Current_amount`,`Previous_amount`,`Total_amount`,`Status`)" + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			double p_amount = this.getPreviousAmount(account_no, status);
			String querynew = "update billing set status = 'Cancel'  where Account_No = ?";
			PreparedStatement preparedStmt1 = con.prepareStatement(querynew);
			preparedStmt1 .setString(1, account_no);
			
			preparedStmt1.execute();
			
			String name = this.getuserdetailsname(account_no);
			String address = this.getuserdetailsaddress(account_no);
			
			int previous_r = this.getpreviousreading(account_no, status);
			
			int units = this.calculateUnits(previous_r,current_r);
			double c_amount = this.calculateCurrentAmount(units);
			
			double t_amount = this.calculateTotalAmount(c_amount,p_amount);
			
			
			
			// binding values
			preparedStmt.setInt(1, 0);
			preparedStmt.setString(2, account_no);
			preparedStmt.setString(3, name);
			preparedStmt.setString(4, address);
			preparedStmt.setString(5, from_d);
			preparedStmt.setInt(6, previous_r);
			preparedStmt.setString(7, to_d);
			preparedStmt.setInt(8, current_r);
			preparedStmt.setInt(9, units);
			preparedStmt.setDouble(10,  c_amount);
			preparedStmt.setDouble(11, p_amount);
			preparedStmt.setDouble(12,  t_amount);
			preparedStmt.setString(13, status);

			// execute the statement
			preparedStmt.execute();
			con.close();
			
			String newBill = readBilingDetails();
			output = "{\"status\":\"success\", \"data\": \"Insert Successful" + newBill + "\"}"; 
		}
		catch (Exception e)
		{
			output = "{\"status\":\"error\", \"data\":\"Error while inserting the Bill.\"}";
			System.err.println(e.getMessage()); 
		}
		return output;
	}

	
	public String insertbilling(String account_no, String from_d, String to_d, int current_r, String status)
	{
		String output = "";
		try
		{
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for inserting."; 
			}
			
			// create a prepared statement
			String query = " insert into billing (`Account_No`,`Name`,`Address`,`From_Date`,`To_Date`,`Current_Reading`,`Status`" + "values (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			
			
			String name = this.getuserdetailsname(account_no);
			String address = this.getuserdetailsaddress(account_no);
			
			
			
			// binding values
			preparedStmt.setInt(1, 0);
			preparedStmt.setString(2, account_no);
			preparedStmt.setString(3, name);
			preparedStmt.setString(4, address);
			preparedStmt.setString(5, from_d);
			preparedStmt.setString(6, to_d);
			preparedStmt.setInt(7, current_r);
			preparedStmt.setString(8, status);

			// execute the statement
			preparedStmt.execute();
			con.close();
			String newBill = readBilingDetails();
			 output = "{\"status\":\"success\", \"data\": \"" + newBill + "\"}";
			 
		}
		catch (Exception e)
		{
			output = "{\"status\":\"error\", \"data\":\"Error while inserting the item.\"}";
			System.err.println(e.getMessage()); 
		}
		return output;
	}


	// Create method to get previous amount
	public double getPreviousAmount(String account_no, String status) {
		 
		double p_amount=0;
		 
		try {
			//db connectivity
			Connection con = connect();
			
			//create a prepared statement
			String getQuery = "select Total_amount\n"
								+ "from billing\n"
								+ "where Account_No = ? and Status='Pending'; ";
			
			PreparedStatement pstmt = con.prepareStatement(getQuery);
			pstmt.setString(1, account_no);

			double Total_a= 0;
			
			ResultSet rs = pstmt.executeQuery();
			
			//iterate through the rows in the result set
			while (rs.next()) {
				
				
				Total_a = rs.getDouble("Total_amount");
				
			}
			con.close();
			
			 
			p_amount = Total_a;
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return p_amount; //return previous amount
	}

	// Create method to get previous reading
	public int getpreviousreading(String account_no, String status) {
		 
		int previous_r=0;
		 
		try {
			//db connectivity
			Connection con = connect();
			
			//create a prepared statement
			String getQuery = "select Current_Reading\n"
					+ "from billing\n"
					+ "where Account_No = ? and Status='Cancel'  ; ";
			
			PreparedStatement pstmt = con.prepareStatement(getQuery);
			pstmt.setString(1, account_no);

			int Current_R= 0;
			
			ResultSet rs = pstmt.executeQuery();
			
			//iterate through the rows in the result set
			while (rs.next()) {
				
				Current_R = rs.getInt("Current_Reading");
				
			}
			con.close();
			
			previous_r = Current_R;
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return previous_r; //return previous reading
	}

	// Create method to get user address
	public String getuserdetailsaddress(String account_no) {
		 
		 String address="";
		 
		try {
			
			Connection con = connect();
			
			//create a prepared statement
			String getQuery = "select u.Address\n"
							+ "from user u\n"
							+ "where u.accountNo = ?; ";
			
			PreparedStatement pstmt = con.prepareStatement(getQuery);
			pstmt.setString(1, account_no);

			String getaddress = "";
			
			ResultSet rs = pstmt.executeQuery();
			
			//iterate through the rows in the result set
			while (rs.next()) {
				
				getaddress = rs.getString("Address");
				
			}
			con.close();

			address = getaddress;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return address; //return address
	}

	// Create method to get user name
	public String getuserdetailsname(String account_no) {
		 
		String name="";
		  
		try {
			
			Connection con = connect();
			
			String getQuery = "select u.Name \n"
							+ "from user u\n"
							+ "where u.accountNo = ?; ";
			
			PreparedStatement pstmt = con.prepareStatement(getQuery);
			pstmt.setString(1, account_no);
			
			String getname ="";
			
			ResultSet rs = pstmt.executeQuery();
			
			//iterate through the rows in the result set
			while (rs.next()) {
				
				getname = rs.getString("Name");
	
			}
			con.close();
			
			name = getname;

			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return name; //return name
	}

	// Create method to calculate units
	public int calculateUnits(int previous_r, int current_r) {
		
		 int units = 0;
		 
		 //calculation
		 units = current_r - previous_r;
		
		 return units; //return no of units
	}

	//create method to calculate current amount
	public double calculateCurrentAmount(int units) {
		
		double c_amount = 0;
		
		if(units<=60) {
			c_amount = (double) (units * 7.85);
		}else if(units>60 && units<=90){
			c_amount = (double) ((double) (60 * 7.85) + (units - 60) * 10.00);
		}else if(units>90 && units<=120){
			c_amount = (double) ((double) (60 * 7.85) + (30 * 10.00) + (units - 90) * 27.75);
		}else if(units>120 && units<=180){
			c_amount = (double) ((double) (60 * 7.85) + (30 * 10.00) + (30 * 27.75) + (units - 120) * 32.75);
		}else {
			c_amount = (double) ((double) (60 * 7.85) + (30 * 10.00) + (30 * 27.75) + (60 * 32.75) + (units - 180) * 45.00);
		}
		
		return c_amount; //return current amount
	}

	//create method to calculate total amount
	public double calculateTotalAmount(double c_amount, double p_amount) {
		
		double t_amount = 0;
		
		t_amount = c_amount + p_amount;
		
		return t_amount; //return total amount
	}

	// Create method to all bill details
	public String readBilingDetails() {
		
		String output = "";
		
		try {
			
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for reading."; 
			}
			
			// Prepare the html table to be displayed
			output = "<table border='1'><tr><th>Account No</th><th>Name</th><th>Address</th><th>From Date</th><th>Previous Reading</th><th>To Date</th><th>Current Reading</th><th>Units</th><th>Current Amount</th><th>Previous Amount</th><th>Total Amount</th><th>Status</th><th>Update</th><th>Remove</th></tr>";
			
			String query = "select * from billing";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			 // iterate through the rows in the result set
			 while (rs.next())
			 {
				 String ID = Integer.toString(rs.getInt("ID"));
				 String Account_No = rs.getString("Account_No");
				 String Name = rs.getString("Name");
				 String Address = rs.getString("Address");
				 Date From_Date = rs.getDate("From_Date");
				 String Previous_Reading =  Integer.toString(rs.getInt("Previous_Reading"));
				 Date To_Date = rs.getDate("To_Date");
				 String Current_Reading = Integer.toString(rs.getInt("Current_Reading"));
				 String Units = Integer.toString(rs.getInt("Units"));
				 String Current_amount = Double.toString(rs.getDouble("Current_amount"));
				 String Previous_amount = Double.toString(rs.getDouble("Previous_amount"));
				 String Total_amount = Double.toString(rs.getDouble("Total_amount"));
				 String Status = rs.getString("Status");
				 
				// Add into the html table
				
				 output += "<tr><td><input id='hidItemIDUpdate' name='hidItemIDUpdate' type='hidden' data-billid='" + ID + "'>" + Account_No + "</td>";  
				 output += "<td>" + Name + "</td>";
				 output += "<td>" + Address + "</td>";
				 output += "<td>" + From_Date + "</td>";
				 output += "<td>" + Previous_Reading + "</td>";
				 output += "<td>" + To_Date + "</td>";
				 output += "<td>" + Current_Reading + "</td>";
				 output += "<td>" + Units + "</td>";
				 output += "<td>" + Current_amount + "</td>";
				 output += "<td>" + Previous_amount + "</td>";
				 output += "<td>" + Total_amount + "</td>";
				 output += "<td>" + Status + "</td>";
				 
				// buttons
				// buttons
				 output += "<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary' data-billid='"+ ID +"'></td>"
				+ "<td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-accountno='"+ Account_No + "'></td></tr>"; 
			 }
			 con.close();
			 // Complete the html table
			 output += "</table>";
			
		}catch(Exception e) {
			
			output = "Error while reading the bill details of users";
			System.err.println(e.getMessage());
		}
		
		return output;
	}
	
	//create method to read bill details using account no
	public String getuserBilingDetails(String account_no) {
		
		String output = "";
		
		try {
			
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for reading."; 
			}
			
			// Prepare the html table to be displayed
			output = "<table border='1' style=\"font-family: Arial, Helvetica, sans-serif; border-collapse: collapse; width: 100%; radius: 10px\">"
					+ "<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\">Bill ID</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Account No</th>" 
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Name</th>" 
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Address</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>From Date</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Previous Meter Reading</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>To date</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Current Meter Reading</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>No of Units Consumed</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Charge for electricity consumed</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Total amount according to the previous amount</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Total Amount</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Status (Pending / Done)</th></tr>";
			
			String query = "select * from billing where Account_No='"+account_no+"'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			 // iterate through the rows in the result set
			 while (rs.next())
			 {
				 String ID= Integer.toString(rs.getInt("ID"));
				 String Account_No = rs.getString("Account_No");
				 String Name = rs.getString("Name");
				 String Address = rs.getString("Address");
				 Date From_Date = rs.getDate("From_Date");
				 int Previous_Reading = rs.getInt("Previous_Reading");
				 Date To_Date = rs.getDate("To_Date");
				 int Current_Reading = rs.getInt("Current_Reading");
				 int Units = rs.getInt("Units");
				 Double Current_amount = rs.getDouble("Current_amount");
				 Double Previous_amount = rs.getDouble("Previous_amount");
				 Double Total_amount = rs.getDouble("Total_amount");
				 String Status = rs.getString("Status");
				 
				 // Add a row into the html table
				 output += "<tr style=\"border: 1px solid #ddd; padding: 8px;\"><td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + ID + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Account_No + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Name + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Address + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + From_Date + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Previous_Reading + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + To_Date + "</td>"; 
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Current_Reading + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Units + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Current_amount + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Previous_amount + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Total_amount + "</td>";
				 // buttons
				 
			 }
			 con.close();
			 // Complete the html table
			 output += "</table>";
			
		}catch(Exception e) {
			
			output = "Error while reading the bill details of users";
			System.err.println(e.getMessage());
		}
		
		return output;
		
	}
	
	// Create method to read bill details using bill id
	public String getuserBilingDetailsbyid(String id) {
		
		String output = "";
		
		try {
			
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for reading."; 
			}
			
			// Prepare the html table to be displayed
			output = "<table border='1' style=\"font-family: Arial, Helvetica, sans-serif; border-collapse: collapse; width: 100%; radius: 10px\">"
					+ "<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\">Bill ID</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Account No</th>" 
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Name</th>" 
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Address</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>From Date</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Previous Meter Reading</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>To date</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Current Meter Reading</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>No of Units Consumed</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Charge for electricity consumed</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Total amount according to the previous amount</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Total Amount</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Status (Pending / Done)</th></tr>";
			
			String query = "select * from billing where ID='"+id+"'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			 // iterate through the rows in the result set
			 while (rs.next())
			 {
				 String ID= Integer.toString(rs.getInt("ID"));
				 String Account_No = rs.getString("Account_No");
				 String Name = rs.getString("Name");
				 String Address = rs.getString("Address");
				 Date From_Date = rs.getDate("From_Date");
				 int Previous_Reading = rs.getInt("Previous_Reading");
				 Date To_Date = rs.getDate("To_Date");
				 int Current_Reading = rs.getInt("Current_Reading");
				 int Units = rs.getInt("Units");
				 Double Current_amount = rs.getDouble("Current_amount");
				 Double Previous_amount = rs.getDouble("Previous_amount");
				 Double Total_amount = rs.getDouble("Total_amount");
				 String Status = rs.getString("Status");
				 
				 // Add a row into the html table
				 output += "<tr style=\"border: 1px solid #ddd; padding: 8px;\"><td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + ID + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Account_No + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Name + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Address + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + From_Date + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Previous_Reading + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + To_Date + "</td>"; 
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Current_Reading + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Units + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Current_amount + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Previous_amount + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Total_amount + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Status + "</td>";
				 // buttons
				 
			 }
			 con.close();
			 // Complete the html table
			 output += "</table>";
			
		}catch(Exception e) {
			
			output = "Error while reading the bill details of users";
			System.err.println(e.getMessage());
		}
		
		return output;
		
	}
	
	// Create method to update bill details
	public String updateBillDetails(String id,String account_no, String from_Date, String to_Date, String current_Reading, String status ) {
		
		String output = "";
		
		try {
			
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for updating."; 
			}
			
			
			
			// create a prepared statement
			String query = "UPDATE billing SET Account_No=?,Name=?,Address=?,From_Date=?,Previous_reading=?,To_Date=?,Current_Reading=?,Units=?,Current_amount=?,Previous_amount=?,Total_amount=?,Status=? where ID=?  ";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			String querynew = "update billing set status = 'Cancel'  where Account_No = ?";
			PreparedStatement preparedStmt1 = con.prepareStatement(querynew);
			preparedStmt1 .setString(1, account_no);
			
			double p_amount = this.getPreviousAmount(account_no, status);
			
			String name = this.getuserdetailsname(account_no);
			String address = this.getuserdetailsaddress(account_no);
			
			int previous_r = this.getpreviousreading(account_no, status);
			
			int units = this.calculateUnits(previous_r,Integer.parseInt(current_Reading));
			double c_amount = this.calculateCurrentAmount(units);
			
			double t_amount = this.calculateTotalAmount(c_amount,p_amount);
			
			// binding values
			preparedStmt.setString(1, account_no); 
			preparedStmt.setString(2, name); 
			preparedStmt.setString(3, address); 
			preparedStmt.setString(4, from_Date);
			preparedStmt.setInt(5, previous_r);
			preparedStmt.setString(6, to_Date);
			preparedStmt.setInt(7, Integer.parseInt(current_Reading));
			preparedStmt.setInt(8, units);
			preparedStmt.setDouble(9,  c_amount);
			preparedStmt.setDouble(10, p_amount);
			preparedStmt.setDouble(11,  t_amount);
			preparedStmt.setString(12, status);
			preparedStmt.setString(13, id); 
			
			
			// execute the statement
			preparedStmt.execute();
			con.close();
			String newBill = readBilingDetails();
			 output = "{\"status\":\"success\", \"data\": \"" + newBill + "\"}";
			   
		}catch(Exception e) {
			
			output = "{\"status\":\"error\", \"data\":\"Error while updating the item.\"}";
			System.err.println(e.getMessage());
		}
		
		return output;
	}
	
public String updateBill(String id,String account_no, String from_Date, String to_Date, String current_Reading, String status ) {
		
		String output = "";
		
		try {
			
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for updating."; 
			}
			
			
			
			// create a prepared statement
			String query = "UPDATE billing SET Account_No=?,Name=?,Address=?,From_Date=?,Previous_reading=?,To_Date=?,Current_Reading=?,Units=?,Current_amount=?,Previous_amount=?,Total_amount=?,Status=? where ID=?  ";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			
			
			String name = this.getuserdetailsname(account_no);
			String address = this.getuserdetailsaddress(account_no);
			
			
			
			// binding values
			preparedStmt.setString(1, account_no); 
			preparedStmt.setString(2, name); 
			preparedStmt.setString(3, address); 
			preparedStmt.setString(4, from_Date);
			preparedStmt.setString(5, to_Date);
			preparedStmt.setInt(6, Integer.parseInt(current_Reading));
			preparedStmt.setString(7, status);
			preparedStmt.setInt(8, Integer.parseInt(id));

			
			
			// execute the statement
			preparedStmt.execute();
			con.close();
			String newBill = readBilingDetails();
			 output = "{\"status\":\"success\", \"data\": \"" + newBill + "\"}";
			 
			   
		}catch(Exception e) {
			
			output = "{\"status\":\"error\", \"data\":\"Error while updating the item.\"}";
			System.err.println(e.getMessage());
		}
		
		return output;
	}
	
	// Create method to delete bill details
	public String deletebill(String Account_No){
		
		String output = "";
		try
		{
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for deleting."; 
			}
			// create a prepared statement
			String query = "delete from billing where Account_No=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			// binding values
			preparedStmt.setString(1, Account_No);
			
			// execute the statement
			preparedStmt.execute();
			con.close();
			String newBill = readBilingDetails();
			output = "{\"status\":\"success\", \"data\": \"" + newBill + "\"}";
			 
			
		}
		catch (Exception e)
		{
			output = "{\"status\":\"error\", \"data\":\"Error while deleting the item.\"}";
			System.err.println(e.getMessage());		}
		
		return output;
		
	}
	
 
}
