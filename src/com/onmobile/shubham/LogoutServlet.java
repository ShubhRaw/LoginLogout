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

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	Logger logger= LogManager.getLogger(LogoutServlet.class);
    Logger tlogger=LogManager.getLogger("tran."+LogoutServlet.class.getName());
    
    public LogoutServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession(false);
		PrintWriter out=response.getWriter();
		
		if(session==null)
		{
			out.print("<script>alert(\"Please login First\");</script>");  
			request.getRequestDispatcher("login.html").include(request, response);
			
		}
		else {
        
        logger.debug("Invalidating the session for "+session.getAttribute("email"));
        tlogger.debug(" ",new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()),session.getAttribute("email"),"Logout",response.getStatus(),"SUCCESS",session.getAttribute("email")+" logged out");
        logger.debug(session.getAttribute("email") +" logged out");
        session.invalidate();
        
        
		logger.debug("Redirecting to "+request.getContextPath() + "/login.html");
		response.sendRedirect(request.getContextPath() + "/login.html");	        
		}

	}
}
