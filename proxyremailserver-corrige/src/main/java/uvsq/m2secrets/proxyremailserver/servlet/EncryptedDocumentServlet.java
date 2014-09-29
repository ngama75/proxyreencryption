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

import uvsq.m2secrets.proxyreencryption.entities.EncryptedDocument;
import uvsq.m2secrets.proxyremailserver.dao.MainDao;

@WebServlet(name="EncryptedDocumentServlet", urlPatterns={"/edocs"})
public class EncryptedDocumentServlet extends HttpServlet {
	  private static final long serialVersionUID = 1L;
	
	  @EJB MainDao maindao;
	   protected void doGet(
		        HttpServletRequest request, HttpServletResponse response)
		            throws ServletException, IOException {
		   ServletOutputStream out = response.getOutputStream();
		   out.println("<h1>List of docs</h1>");
		   out.println("<pre>");
		   List<EncryptedDocument> l = maindao.getAllEncryptedDocuments();
		   for (EncryptedDocument d:l) {
			   out.println(d.toString());
		   }
		   out.println("</pre>");
	   }

}
