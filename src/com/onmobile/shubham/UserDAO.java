package com.onmobile.shubham;

public interface UserDAO {
	void insert(User u) throws Exception;
	void update(User u) throws Exception;
	User select(String email) throws Exception;
	boolean checkemail(String email);
	void updatelock(String email, int counter, long la) throws Exception;
	
}
