package com;

import java.sql.Date;

//For REST Service
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//For JSON
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//For XML
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import model.BillingModel;

@Path("/Billing") //url path
public class BillingService {

	
	BillingModel billing = new BillingModel();
	
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String insertbillingdata(@FormParam("Account_No") String Account_No,
									@FormParam("From_Date") String From_Date,
									@FormParam("To_Date") String To_Date,
									@FormParam("Current_Reading") int Current_Reading,
									@FormParam("Status") String Status)
	{
		
		if(Account_No.isEmpty()||From_Date.isEmpty()|To_Date.isEmpty()||Status.isEmpty())
		{
			 return "Fields can't be empty";
		}
		else if(!From_Date.matches("^(?:[0-9][0-9])?[0-9][0-9]-[0-3][0-9]-[0-3][0-9]$"))
		{
			return "From Date not be in correct format. Reenter From Date..";
		}
		else if(!To_Date.matches("^(?:[0-9][0-9])?[0-9][0-9]-[0-3][0-9]-[0-3][0-9]$"))
		{
			return "To Date not be in correct format. Reenter To Date..";
		}
		 
		
		String output = billing.insertbillingdata(Account_No, From_Date, To_Date, Current_Reading, Status);
		return output;
	}
	
	@GET
	@Path("/read")
	@Produces(MediaType.TEXT_HTML)
	public String readBilingDetails() {//view all billing details of users
		return billing.readBilingDetails();
	}
	
	@GET
	@Path("/getDetailsbyaccountno/{Account_No}")//view a specific billing details of user
	@Produces(MediaType.TEXT_HTML)
	public String UserBillingDetails(@PathParam("Account_No") String Account_No) {

		return billing.getuserBilingDetails(Account_No);
	}
	
	@GET
	@Path("/getDetailsbyid/{ID}")//view a specific billing details from bill id
	@Produces(MediaType.TEXT_HTML)
	public String UserBillingDetailsbyid(@PathParam("ID") String ID) {

		return billing.getuserBilingDetailsbyid(ID);
	}

	@PUT
	@Path("/updatebill")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateBillDetails(String billData) {
		
		// Convert the input string to a JSON object
		JsonObject billObj = new JsonParser().parse(billData).getAsJsonObject();
		
		// Read the values from the JSON object
		String ID = billObj.get("ID").getAsString();
		String Account_No = billObj.get("Account_No").getAsString();
		String From_Date = billObj.get("From_Date").getAsString();
		String To_Date = billObj.get("To_Date").getAsString();
		String Current_Reading = billObj.get("Current_Reading").getAsString();
		String Status = billObj.get("Status").getAsString();
		
		if(Account_No.isEmpty()||From_Date.isEmpty()|To_Date.isEmpty()||Status.isEmpty())
		{
			 return "Fields can't be empty";
		}
		else if(!From_Date.matches("^(?:[0-9][0-9])?[0-9][0-9]-[0-3][0-9]-[0-3][0-9]$"))
		{
			return "From Date not be in correct format. Reenter From Date..";
		}
		else if(!To_Date.matches("^(?:[0-9][0-9])?[0-9][0-9]-[0-3][0-9]-[0-3][0-9]$"))
		{
			return "To Date not be in correct format. Reenter To Date..";
		}
		 
		String output = billing.updateBillDetails(ID,Account_No,From_Date,To_Date,Current_Reading,Status);
		return output;
	}
	
	
	@DELETE
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public String deletebill(String billData)
	{
		//Convert the input string to an XML document
		Document doc = Jsoup.parse(billData, "", Parser.xmlParser());

		//Read the value from the element <Account No>
		String Account_No = doc.select("Account_No").text();
		
		String output = billing.deletebill(Account_No);
		return output;
	}
	
	
}
