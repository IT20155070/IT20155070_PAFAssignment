<%@page import="model.BillingModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="Views/bootstrap.min.css">
<script src="Components/jquery-3.2.1.min.js"></script>
<script src="Components/BillingDetails.js"></script>

</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-6">
				<h1>Billing Service</h1>
				<form id="formBill" name="formBill">
					Account No: <input id="Account_No" name="Account_No" type="text" class="form-control form-control-sm"> 
					<br> From Date:
					<input id="From_Date" name="From_Date" type="text" class="form-control form-control-sm">
					 <br> To Date
					<input id="To_Date" name="To_Date" type="text" class="form-control form-control-sm"> 
					<br> Current Reading
					<input id="Current_Reading" name="Current_Reading" type="text" class="form-control form-control-sm"> 
					<br> Status
					<input id="Status" name="Status" type="text" class="form-control form-control-sm"> 
					<br> 
					<input id="btnBillSave" name="btnBillSave" type="button" value="Save" class="btn btn-primary"> 
					
					<input type="hidden" id="hidBillIDSave" name="hidBillIDSave" value="">
				</form>
				<br>
				<div id="alertSuccess" class="alert alert-success"></div>
				<div id="alertError" class="alert alert-danger"></div>
				<br>
				<div class="row">
				<div class="col-12" id="divBillGrid">
				
					<%
						BillingModel BillObj = new BillingModel();
						out.print(BillObj.readBilingDetails());
					%>
				</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>