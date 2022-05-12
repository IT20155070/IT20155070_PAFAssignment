package model;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


@WebServlet("/BillingAPI")
public class BillingAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	BillingModel BillObj = new BillingModel();
       
    
    public BillingAPI() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String output = BillObj.insertbillingdata(request.getParameter("Account_No"), 
				 request.getParameter("From_Date"), 
				request.getParameter("To_Date"), 
				Integer.parseInt(request.getParameter("Current_Reading")),
		        request.getParameter("Status")); 
				response.getWriter().write(output);
	}

	private Map getParasMap(HttpServletRequest request) {
		
		Map<String, String> map = new HashMap<String, String>();
		try
		 {
		 Scanner scanner = new Scanner(request.getInputStream(), "UTF-8");
		 String queryString = scanner.hasNext() ?
		 scanner.useDelimiter("\\A").next() : "";
		 scanner.close();
		 String[] params = queryString.split("&");
		 for (String param : params)
		 { 
			 String[] p = param.split("=");
			 map.put(p[0], p[1]);
			 }
			 }
			catch (Exception e)
			 {
			 }
			return map;
		 
	}

	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map paras = getParasMap(request); 
		
		String output = BillObj.updateBillDetails(paras.get("hidBillIDSave").toString(), 
				paras.get("Account_No").toString(), 
				paras.get("From_Date").toString(), 
				paras.get("To_Date").toString(), 
				paras.get("Current_Reading").toString(),
				paras.get("Status").toString()); 
		
		response.getWriter().write(output); 
	}

	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map paras = getParasMap(request);
		
		String output = BillObj.deletebill(paras.get("Account_No").toString());
		
		response.getWriter().write(output); 

	}


	
}
