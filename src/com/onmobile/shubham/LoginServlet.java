package com.onmobile.shubham;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
       
	Logger logger= LogManager.getLogger(LoginServlet.class);
    Logger tLogger=LogManager.getLogger("tran."+LoginServlet.class.getName());
    Tlog tlog=new Tlog();
    
    public LoginServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HeadPara hp=new HeadPara();
		hp.gettingPara(request);
		hp.gettingHead(request);
		
		PrintWriter out = response.getWriter();
		String email= request.getParameter("email");
		String password= request.getParameter("password");
		logger.debug("req param incoming Email = "+email+" and Password = "+password+"");
		UserDAO udi=new UserDAOJdbcImpl();
		logger.debug("Checking if "+email+" exists in DB");
		
		boolean emailFlag = udi.checkemail(email);
		if(emailFlag)
		{
			logger.debug(email +"not in DB. Redirecting to "+request.getContextPath() + "/signup.html");
//			tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()),email,"Login",response.getStatus(),"Email not in DB");
			tlog.setEmail(email);
			tlog.setAction("Login");
			tlog.setCondition("SUCCESS");
			tlog.setStatus(response.getStatus());
			tlog.setMessage("Email not in DB");
			request.getRequestDispatcher("signup.html").include(request, response);
			out.println("<script>alert(\"Email not found in database! You have to register first!\");</script>");
		}
		else
		{
		User user=null;
		logger.debug(email+" present in DB, checking if its locked for inserting wrong password more than 3 times");
		try {
			user = udi.select(email);
			if(user != null)
				logger.debug("Got user from DB : "+user.toString());
			else
				logger.debug("Got user from DB = null");
			
		} catch (Exception e) {
			//tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()),email,"Updating Lock",response.getStatus(),e.getMessage());
			tlog.setEmail(email);
			tlog.setAction("Updating Lock");
			tlog.setCondition("FAILURE");
			tlog.setStatus(response.getStatus());
			tlog.setMessage(e.getMessage());
		}
		
			
			int l=user.getCounter();
			if(l<3)
			{
				logger.debug("Not Locked, Checking if Password is correct");
				if(password.equals(user.getPassword()))
				{
					logger.debug("Password Matched");
					HttpSession session=request.getSession();
					Date d=new Date();
					logger.debug("Updating Lock to 0 since succesful attempt");
					try {
						udi.updatelock(user.getEmail(), 0, d.getTime());
					} catch (Exception e) {
						//tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()),email,"Updating Lock",response.getStatus(),e.getMessage());
						tlog.setEmail(email);
						tlog.setAction("Updating Lock");
						tlog.setCondition("FAILURE");
						tlog.setStatus(response.getStatus());
						tlog.setMessage(e.getMessage());
					}
					session.setAttribute("name", user.getName());
					session.setAttribute("email", user.getEmail());
					session.setAttribute("address", user.getAddress());
					session.setAttribute("course", user.getCourse());
					session.setAttribute("photo", user.getPhoto());

					logger.debug(email+" Logged in");
					
					//tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()),email,"Login",response.getStatus(),"No Error");
					tlog.setEmail(email);
					tlog.setAction("Login");
					tlog.setCondition("SUCCESS");
					tlog.setStatus(response.getStatus());
					tlog.setMessage(email+" Logged in");
					
					response.sendRedirect("profile.jsp");
				}
				else
				{
					logger.debug(email+" entered wrong password");
					int c;
					Date d2=new Date();
					c=user.getCounter()+1;
					logger.debug("Incrementing Lock for " + email +" now"+(3-user.getCounter())+" chances left");
					try {
						udi.updatelock(user.getEmail(), c, d2.getTime());
					} catch (Exception e) {
						//tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()),email,"Updating Lock",response.getStatus(),e.getMessage());
						tlog.setEmail(email);
						tlog.setAction("Updating Lock");
						tlog.setCondition("FAILURE");
						tlog.setStatus(response.getStatus());
						tlog.setMessage(e.getMessage());
					}
					
					//tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()),email,"Login",response.getStatus(),"Wrong Password");
					tlog.setEmail(email);
					tlog.setAction("Login");
					tlog.setCondition("FAILURE");
					tlog.setStatus(response.getStatus());
					tlog.setMessage("Wrong Password");
					request.getRequestDispatcher("login.html").include(request, response);
					out.println("<h4 style=\"color:red;\"> Please Enter Correct Details! Chances Remaining: "+(3-user.getCounter())+"</h4>");
				}
			}
				else
				{
					long timeleft;
					Date d3=new Date();
					timeleft=(d3.getTime()-user.getLa())/(60*1000) % 60;
				
					if(timeleft < 30)
					{
						logger.debug(email+ " is in locked state for inserting wrong password more than 3 times");
						request.getRequestDispatcher("login.html").include(request, response);
						//tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()),email,"Login",response.getStatus(),"Locked State");
						tlog.setEmail(email);
						tlog.setAction("Login");
						tlog.setCondition("FAILURE");
						tlog.setStatus(response.getStatus());
						tlog.setMessage("Locked State");
						
						out.println("<script>alert(\"You are locked! Time Remaining: " +(30-timeleft)+" mins\");</script>");
					}
					else
					{
						try {
							udi.updatelock(user.getEmail(), 0, d3.getTime());
						} catch (Exception e) {
							//tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()),email,"Updating Lock",response.getStatus(),e.getMessage());
							tlog.setEmail(email);
							tlog.setAction("Updating Lock");
							tlog.setCondition("FAILURE");
							tlog.setStatus(response.getStatus());
							tlog.setMessage(e.getMessage());
						}
						logger.debug(email+" is unlocked now since 30 mins are over since last unsuccessful attempt");
						//tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()),email,"Login",response.getStatus(),"Unlocked");
						tlog.setEmail(email);
						tlog.setAction("Login");
						tlog.setCondition("SUCCESS");
						tlog.setStatus(response.getStatus());
						tlog.setMessage("Unlocked");
						request.getRequestDispatcher("login.html").include(request, response);
						out.println("<script>alert(\"You are unlocked! Login Again\");</script>");
					}
				
				}
		}
		
		tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()), tlog.getEmail(), tlog.getAction(), tlog.getStatus(), tlog.getCondition(), tlog.getMessage());
	}
}



