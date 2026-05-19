package com.synopsys.reachability;

// with 2 intermediate functions

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author aphadke
 *
 */
@WebServlet("/SameFunction")
public class DifferentFunctionsIntermediate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public DifferentFunctionsIntermediate() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String paramValue = request.getParameter("evilParam");

		intermediate1(paramValue);
		

	}

	private void intermediate1(String param) {
		
		intermediate2(param);
		
	}
	
private void intermediate2(String param) {
		
	goToSQL(param);
		
	}

	private void goToSQL(String param) {

		try {

			Connection conn = DriverManager.getConnection("jdbc:mysql://local/", "userName", "password");

			Statement st = conn.createStatement();

			ResultSet res = st.executeQuery("SELECT * FROM  User where userId='" + param + "'");

			res.close();
			
		} catch (Exception e) {
			// TODO do nothing
		}

	}

}
