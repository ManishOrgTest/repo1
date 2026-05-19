package com.synopsys.reachability;


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
 * Condition is based on value of request param. so could be false or true. and with always false condition.  
 */
@WebServlet("/SameFunction")
public class DiffFunctionsCondionalParamAndAlwaysFalse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static boolean isAdmin;
	private static boolean alwaysFalse = false;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String paramValue = request.getParameter("evilParam");
		isAdmin = Boolean.parseBoolean(request.getParameter("isAdmin"));

		goToSQL(paramValue);

	}

	private void goToSQL(String param) {

		if (isAdmin && alwaysFalse) {
			return;
		}
		
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
