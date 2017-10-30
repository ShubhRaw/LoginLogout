package com.onmobile.shubham;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/SignupServlet")
@MultipartConfig
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Logger logger= LogManager.getLogger(SignupServlet.class);
	Logger tLogger=LogManager.getLogger("tran."+SignupServlet.class.getName());
	Tlog tlog=new Tlog();

	public SignupServlet() {
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

		PrintWriter out=response.getWriter();

		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String password=request.getParameter("password");
		String course=request.getParameter("courselist");
		String address=request.getParameter("address");
		String photo=null; 
		logger.debug("Read all the values from request");

		UserDAO udi=new UserDAOJdbcImpl();
		logger.debug("Checking if "+email+" already exists in DB");
		boolean emailFlag=udi.checkemail(email);
		if(!emailFlag)
		{
			logger.debug(email+" exists in DB. Redirecting to "+request.getContextPath() + "/login.html");
			out.print("<script>alert(\"Email exists in DB, please login\");</script>");  
			request.getRequestDispatcher("login.html").include(request, response);
		}
		else
		{
			try {
				logger.debug(email+" is not in DB");
				Part filePart = request.getPart("pic");

				if(filePart==null) {
					photo=null;
				}
				else {

					photo=email+".jpg";

					FileInputStream is = (FileInputStream) filePart.getInputStream();
					FileOutputStream ot = new FileOutputStream("D:\\uploads\\"+photo);

					int c;
					while ((c = is.read()) != -1) {
						ot.write(c);
					}
					ot.close();
				}

			}catch (Exception e) { }

			User u=new User();
			u.setName(name);
			u.setEmail(email);
			u.setPassword(password);
			u.setCourse(course);
			u.setAddress(address);
			u.setPhoto(photo);
			logger.debug("New User Object: "+ u.toString());
			logger.debug("Inserting this user obj in DB");
			try {
				udi.insert(u);
			} catch (Exception e) {
				//tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()),email,"Inserting Data",response.getStatus(),e.getMessage());
				tlog.setEmail(email);
				tlog.setAction("Inserting Data");
				tlog.setCondition("FAILURE");
				tlog.setStatus(response.getStatus());
				tlog.setMessage(e.getMessage());
			}

			logger.debug(name+" "+email+" "+course+" "+address+" "+photo+" inserted in DB");
			//tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()),email,"Signup",response.getStatus(),"No Error");
			tlog.setEmail(email);
			tlog.setAction("Signup");
			tlog.setCondition("SUCCESS");
			tlog.setStatus(response.getStatus());
			tlog.setMessage(email+" signed up");
			logger.debug("Redirecting to "+request.getContextPath() + "/login.html");
			response.sendRedirect("login.html");
		}

		tLogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()), tlog.getEmail(), tlog.getAction(), tlog.getStatus(), tlog.getCondition(), tlog.getMessage());
	}
}
