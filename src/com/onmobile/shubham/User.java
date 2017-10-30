package com.onmobile.shubham;

import javax.persistence.Table;

@Table(name="userdata")
public class User {
	
	private String email;	
	private String name;
	private String password;
	private String course;
	private String address;
	private String photo;
	private int counter;
	private long la;

	public User(){}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public long getLa() {
		return la;
	}

	public void setLa(long la) {
		this.la = la;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", name=" + name + ", password=" + password + ", course=" + course
				+ ", address=" + address + ", photo=" + photo + ", counter=" + counter + ", la=" + la + "]";
	}
	
	

}
