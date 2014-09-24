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

import uvsq.m2secrets.proxyreencryption.entities.PrivKey;
import uvsq.m2secrets.proxyreencryption.entities.PubKey;
import uvsq.m2secrets.proxyreencryption.entities.User;
import uvsq.m2secrets.proxyremailserver.dao.MainDao;

@WebServlet(name="UserServlet", urlPatterns={"/users"})
public class UserServlet extends HttpServlet {
	  private static final long serialVersionUID = 1L;
	
	  @EJB MainDao maindao;
	   
	  protected void doGet(
		        HttpServletRequest request, HttpServletResponse response)
		            throws ServletException, IOException {
		  PrivKey pri = PrivKey.generate();
		  User alice = new User();
		  alice.setName("toto");
		  alice.setPubKey(new PubKey(pri));
		  maindao.insertOrUpdate(alice);
		   ServletOutputStream out = response.getOutputStream();
		   out.println("<h1>List of users</h1>");
		   out.println("<pre>");
		   List<User> l = maindao.getAllUsers();
		   for (User u:l) {
			   out.println(u.toString());
		   }
		   out.println("</pre>");
	   }

}
