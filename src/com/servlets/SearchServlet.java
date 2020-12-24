package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet 
{
	public static final String regex = "^[0-9]{8}$";
	
	private static final long serialVersionUID = 1L;
     
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String id = request.getParameter("id");
		
		RequestDispatcher rd = null;
		
		//Validate the Id form (must be 8 [0-9] digits)
		if(!validID(id))
		{
			//If it doesn't work let the user know the format
			rd=request.getRequestDispatcher("index.html");
			PrintWriter out = response.getWriter();
			rd.include(request, response);
			out.println("<h4><span style='color:red'>That ID " + id + " does not match the 8 digit numeric format.</h4>");
		}
		else
		{
			//Attempt to search database
			try
			{
				Class.forName("org.h2.Driver");

				Connection conn = DriverManager.getConnection("jdbc:h2:~/test","sa","");

				String sql = "Select * from PRODUCTS where id=?";
				
				PreparedStatement pst = conn.prepareStatement(sql);

				pst.setString(1, id);

				ResultSet rs = pst.executeQuery();	
				
				//If the id matches one in the database then go to the productFoundServlet
				if(rs.next())
				{
					rd=request.getRequestDispatcher("ProductFoundServlet");
					rd.forward(request, response);
				}
				//Otherwise let the user know that the product does not exist
				else
				{
					rd=request.getRequestDispatcher("index.html");
					PrintWriter out = response.getWriter();
					rd.include(request, response);
					out.println("<h4><span style='color:red'>That product does not exist in our catalog.</h4>");
				}
				conn.close();
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
	
	//Checking that the id is 8 numbers
	private static boolean validID(String idTest)
	{
		Pattern pattern = Pattern.compile(regex);
		
		Matcher matcher = pattern.matcher(idTest);

		if (matcher.matches())
			return true;
		else
			return false;
	}
}
