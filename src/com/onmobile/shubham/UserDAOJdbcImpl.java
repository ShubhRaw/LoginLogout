package com.onmobile.shubham;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDAOJdbcImpl implements UserDAO {

	Logger logger=LogManager.getLogger(UserDAOJdbcImpl.class);
	public Connection getCon() {

		Connection con=null;	
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection(  
					"jdbc:mysql://localhost:3306/userdetails","root","onmobile");
		} catch (Exception e) { } 

		return con;
	}


	@Override
	public void insert(User u) throws Exception{
		try{  

			Connection con=this.getCon();
			String sql = "INSERT INTO userdata (name,email,password,course,address,photo,counter) values (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, u.getName());
			statement.setString(2, u.getEmail());
			statement.setString(3, u.getPassword());
			statement.setString(4, u.getCourse());
			statement.setString(5, u.getAddress());
			statement.setString(6, u.getPhoto());
			statement.setInt(7, 0);
			statement.executeUpdate();
			logger.debug("Executed SQL query: "+statement.toString());

			con.close();  
		}catch(Exception e){ 
			throw new Exception(e.getMessage());
		}

	}

	@Override
	public void update(User u) throws Exception{
		try{  

			Connection con=this.getCon();

			String sql = "UPDATE userdata SET name=?, address=?, course=? WHERE email=?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, u.getName());
			statement.setString(3, u.getCourse());
			statement.setString(2, u.getAddress());
			statement.setString(4, u.getEmail());
			statement.executeUpdate();
			logger.debug("Executed SQL query: "+statement.toString());

			con.close();  
		}catch(Exception e){ 
			throw new Exception(e.getMessage());
		}  

	}

	@Override
	public void updatelock(String email, int counter, long la) throws Exception{
		try{  
			Connection con=this.getCon();

			PreparedStatement statement = con.prepareStatement("UPDATE userdata SET counter=?, la=? WHERE email=?");
			statement.setInt(1, counter);
			statement.setLong(2, la);
			statement.setString(3, email);
			statement.executeUpdate();
			logger.debug("Executed SQL query: "+statement.toString());

			con.close();  
		}catch(Exception e){ 
			throw new Exception(e.getMessage());
		}  

	}

	@Override
	public User select(String email) throws Exception{
		User u=null;
		try {

			Connection con=this.getCon();

			PreparedStatement statement=con.prepareStatement("select * from userdata where email= ?");
			statement.setString(1, email);
			ResultSet rs=statement.executeQuery(); 
			logger.debug("Executed SQL query: "+statement.toString());

			rs.next();

			u=new User();
			u.setName(rs.getString("name"));
			u.setEmail(rs.getString("email"));
			u.setPassword(rs.getString("password"));
			u.setCourse(rs.getString("course"));
			u.setAddress(rs.getString("address"));
			u.setPhoto(rs.getString("photo"));
			u.setCounter(rs.getInt("counter"));
			u.setLa(rs.getLong("la"));

		} catch (Exception e) {	
			throw new Exception(e.getMessage());

		}

		return u;
	}

	@Override
	public boolean checkemail(String email) {
		try{  
			Connection con=this.getCon();

			String sql = "SELECT * from userdata";
			PreparedStatement statement = con.prepareStatement(sql);
			ResultSet rs=statement.executeQuery(); 
			logger.debug("Executed SQL query: "+statement.toString());

			while(rs.next())
			{
				if(email.equals(rs.getString("email")))
					return false;
			}

			con.close();  
		}catch(Exception e){ }  

		return true;
	}


}
