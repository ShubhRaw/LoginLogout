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

@WebServlet("/UpdateServlet")
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Logger logger= LogManager.getLogger(UpdateServlet.class);
	Logger tLogger=LogManager.getLogger("tran."+UpdateServlet.class.getName());
	Tlog tlog=new Tlog();
	public UpdateServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		doProcess(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		HttpSession session=request.getSession(false);

		if(session==null)
		{
			out.print("<script>alert(\"Please login First\");</script>");  
			request.getRequestDispatcher("login.html").include(request, response);
		}

		else {
			logger.debug("Reading values from the request for Updation");
			String name=request.getParameter("name");
			String email=request.getParameter("email");
			String course=request.getParameter("courselist");
			String address=request.getParameter("address");

			User u=new User();
			u.setEmail(email);
			u.setName(name);
			u.setCourse(course);
			u.setAddress(address);

			logger.debug("New User Object: "+ u.toString());
			UserDAOJdbcImpl udi=new UserDAOJdbcImpl();
			try {
				udi.update(u);
			} catch (Exception e) {
				//tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()),email,"Updating Data",response.getStatus(),e.getMessage());
				tlog.setEmail(email);
				tlog.setAction("Updating Data");
				tlog.setCondition("FAILURE");
				tlog.setStatus(response.getStatus());
				tlog.setMessage(e.getMessage());
			}

			logger.debug("DB updated for " +name+ " with values "+u.toString());
			//tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()),email,"Update DB",response.getStatus(),"No Error");
			tlog.setEmail(email);
			tlog.setAction("Updating DB");
			tlog.setCondition("SUCCESS");
			tlog.setStatus(response.getStatus());
			tlog.setMessage("DB Updated");

			/*logger.debug("Invalidating the session for "+email);

			session.invalidate();
			logger.debug(email+" logged out");
			//tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()),email,"Logout",response.getStatus(),"No Error");
			tlog.setEmail(email);
			tlog.setAction("Logout");
			tlog.setCondition("SUCCESS");
			tlog.setStatus(response.getStatus());
			tlog.setMessage(" ");

			request.getRequestDispatcher("login.html").include(request, response);
			out.println("<h4 style=\"color:red;\"> Please Login Again!</h4>");*/
			session.setAttribute("name", u.getName());
			session.setAttribute("email", u.getEmail());
			session.setAttribute("address", u.getAddress());
			session.setAttribute("course", u.getCourse());
			session.setAttribute("photo", u.getPhoto());
			
			response.sendRedirect("profile.jsp");

		}

		tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()), tlog.getEmail(), tlog.getAction(), tlog.getStatus(), tlog.getCondition(), tlog.getMessage());


	}

}
