package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProductFoundServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	//Shows welcome message and provides a link to return to the login page
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//Try to access the product
		try
		{
			Class.forName("org.h2.Driver");
	
			Connection conn = DriverManager.getConnection("jdbc:h2:~/test","sa","");
			
			String sql = "Select * from PRODUCTS where id=?";
			
			PreparedStatement pst = conn.prepareStatement(sql);
	
			pst.setString(1, request.getParameter("id"));
	
			ResultSet rs = pst.executeQuery();	
			PrintWriter out = response.getWriter();
			
			//Show the user the product information
			while(rs.next())
			{
				String idPrint = rs.getString(1);
				String name =rs.getString(2);
				String description = rs.getString(3);
				
				out.print("<h3>Product: " + name + " </h3>");
				out.print("<h4>ID: " + idPrint + " </h4>");
				out.print("<p>Description: " + description + " </p>");
			}	
			out.print("<a href='index.html'>Return to product search</a>");
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

}
