package com.synopsys.reachability;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author aphadke
 *The taint is within an object. 
 */
@WebServlet("/SameFunction")
public class DifferentFunctionsObject extends HttpServlet {
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

		TaintObj obj = new TaintObj(param);

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://local/", "userName", "password");
			 Statement st = conn.createStatement()) {
			ResultSet res = st.executeQuery("SELECT * FROM User where userId='" + obj.getParam1() + "'");
			res.close();
		} catch (Exception e) {
			// do nothing
		}

	}

}




class TaintObj {

	private final String param1;

	public TaintObj(String param1) {
		this.param1 = param1;
	}

	public String getParam1() {
		return param1;
	}
}
