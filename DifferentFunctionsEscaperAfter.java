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

import org.apache.commons.lang.StringEscapeUtils;

/**
 * @author aphadke
 * StringEscapeUtils from apache commons used to escape the SQL tainted param AFTER the SQL query 
 */
@WebServlet("/SameFunction")
public class DifferentFunctionsEscaperAfter extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String paramValue = request.getParameter("evilParam");

		goToSQL(paramValue);

	}

	private void goToSQL(String param) {

		
		try {

			Connection conn = DriverManager.getConnection("jdbc:mysql://local/", "userName", "password");

			Statement st = conn.createStatement();

			ResultSet res = st.executeQuery("SELECT * FROM  User where userId='" + param + "'");
			res.close();
			
			param = StringEscapeUtils.escapeSql(param);

		} catch (Exception e) {
			// TODO do nothing
		}

	}

}
