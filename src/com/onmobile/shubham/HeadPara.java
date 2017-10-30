package com.onmobile.shubham;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HeadPara {

	Logger logger= LogManager.getLogger(HeadPara.class);

	void gettingPara(HttpServletRequest request)
	{
		Enumeration<String> pnames=request.getParameterNames();
		String pname;
		while(pnames.hasMoreElements()) {
			pname=pnames.nextElement();
			String[] pvalue = request.getParameterValues(pname);
			logger.debug("Req Parameter : "+pname+" = "+pvalue[0]);
		}
	}

	void gettingHead(HttpServletRequest request)
	{
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			logger.debug("Req Header : " + headerName+ " = " + headerValue);
		}
	}
}
