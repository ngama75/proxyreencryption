package uvsq.m2secrets.proxyremailserver.servlet;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uvsq.m2secrets.proxyreencryption.entities.ProxyKey;
import uvsq.m2secrets.proxyremailserver.dao.MainDao;

@WebServlet(name="ProxyKeyServlet", urlPatterns={"/proxykeys"})
public class ProxyKeyServlet extends HttpServlet {
	  private static final long serialVersionUID = 1L;
	
	  @EJB MainDao maindao;
	   protected void doGet(
		        HttpServletRequest request, HttpServletResponse response)
		            throws ServletException, IOException {
		   ServletOutputStream out = response.getOutputStream();
		   out.println("<h1>List of ProxyKeys</h1>");
		   out.println("<pre>");
		   List<ProxyKey> l = maindao.getAllProxyKeys();
		   for (ProxyKey u:l) {
			   out.println(u.toString());
		   }
		   out.println("</pre>");
	   }

}
