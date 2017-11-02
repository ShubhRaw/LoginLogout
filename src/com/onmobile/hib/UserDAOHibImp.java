package com.onmobile.hib;
//production comment
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
//live prod
import com.onmobile.shubham.User;
import com.onmobile.shubham.UserDAO;

public class UserDAOHibImp implements UserDAO {

	Session getSes()
	{
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session s= sessionFactory.openSession();
		return s;
	}


	@Override
	public void insert(User u) throws Exception{
		try {		
			Session s= this.getSes();
			s.beginTransaction();
			s.save(u);
			s.getTransaction().commit();
			s.close();
		}catch(Exception e){ 
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public void update(User u) throws Exception{
		try {
			User u2=new User();
			Session s= this.getSes();
			s.beginTransaction();
			u2=(User)s.get(User.class, u.getEmail());

			u2.setName(u.getName());
			u2.setCourse(u.getCourse());
			u2.setAddress(u.getAddress());
			s.update(u2);
			s.getTransaction().commit();
			s.close();
		}catch(Exception e){ 
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public User select(String email) throws Exception{
		try {
			User u=new User();
			Session s= this.getSes();
			s.beginTransaction();
			u=(User)s.get(User.class, email);
			s.close();
			return u;
		}catch(Exception e){ 
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public boolean checkemail(String email) {
		Session s= this.getSes();
		s.beginTransaction();
		return (User)s.get(User.class, email)==null;
	}

	@Override
	public void updatelock(String email, int counter, long la) throws Exception{
		try {
			User u=new User();
			Session s= this.getSes();
			s.beginTransaction();
			u=(User)s.get(User.class, email);

			u.setCounter(counter);
			u.setLa(la);
			s.update(u);
			s.getTransaction().commit();
			s.close();
		}catch(Exception e){ 
			throw new Exception(e.getMessage());
		}
	}

}
