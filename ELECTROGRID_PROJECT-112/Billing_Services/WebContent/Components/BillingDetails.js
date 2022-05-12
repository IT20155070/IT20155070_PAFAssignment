$(document).ready(function()
{
	if ($("#alertSuccess").text().trim() == "")
	{
		$("#alertSuccess").hide();
	}
	$("#alertError").hide();
});
// SAVE ============================================
$(document).on("click", "#btnBillSave", function(event)
{
	// Clear alerts---------------------
	 $("#alertSuccess").text("");
	 $("#alertSuccess").hide();
	 $("#alertError").text("");
	 $("#alertError").hide();
	 
	 // Form validation-------------------
	 var status = validateBillForm();
	 if (status != true)
	 {
		 $("#alertError").text(status);
		 $("#alertError").show();
		 return;
	 }
	 
	 // If valid------------------------
	 var type = ($("#hidBillIDSave").val() == "") ? "POST" : "PUT";
	 
	 $.ajax(
	 {
		 url : "BillingAPI",
		 type : type,
		 data : $("#formBill").serialize(),
		 dataType : "text",
		 complete : function(response, status)
		 {
		 	onBillSaveComplete(response.responseText, status);
		 }
	 });
});


function onBillSaveComplete(response, status)
{
	 if (status == "success")
	 {
		 var resultSet = JSON.parse(response);
		 if (resultSet.status.trim() == "success")
		 {
		 $("#alertSuccess").text("Record Successfully Saved!");
		 $("#alertSuccess").show();
		 
		 $("#divBillGrid").html(resultSet.data);
		 } else if (resultSet.status.trim() == "error")
		 {
		 $("#alertError").text(resultSet.data);
		 $("#alertError").show();
		 }
		 
	 } else if (status == "error")
	 {
		 $("#alertError").text("Error While Saving!");
		 $("#alertError").show();
	 } else
	 {
		 $("#alertError").text("Unknown Error While Saving!");
		 $("#alertError").show();
	 }
	 
	 $("#hidBillIDSave").val("");
	 $("#formBill")[0].reset();
}

// UPDATE==========================================
$(document).on("click", ".btnUpdate", function(event)
{
	 $("#hidBillIDSave").val($(this).data("billid"));
	 $("#Account_No").val($(this).closest("tr").find('td:eq(0)').text()); 
	 $("#From_Date").val($(this).closest("tr").find('td:eq(3)').date()); 
	 $("#To_Date").val($(this).closest("tr").find('td:eq(5)').date()); 
	 $("#Current_Reading").val($(this).closest("tr").find('td:eq(6)').text());
	 $("#Status").val($(this).closest("tr").find('td:eq(11)').text());
});

$(document).on("click", ".btnRemove", function(event)
{
	 $.ajax(
	 {
		 url : "BillingAPI",
		 type : "DELETE",
		 data : "Account_No=" + $(this).data("accountno"),
		 dataType : "text",
		 complete : function(response, status)
	    {
			 onBillDeleteComplete(response.responseText, status);
	    }
	 });
});

function onBillDeleteComplete(response, status)
{
	if (status == "success")
	{
		var resultSet = JSON.parse(response);

		if (resultSet.status.trim() == "success")
		{
			$("#alertSuccess").text("Record Successfully Deleted!");
			$("#alertSuccess").show();
			$("#divBillGrid").html(resultSet.data);
		} else if (resultSet.status.trim() == "error")
		{
			$("#alertError").text(resultSet.data);
			$("#alertError").show();
		}
	} else if (status == "error")
	{
		$("#alertError").text("Error While Deleting!");
		$("#alertError").show();
	} else
	{
		$("#alertError").text("Unknown Error While Deleting!");
		$("#alertError").show();
	}
}
//CLIENT-MODEL================================================================
function validateBillForm() 
{ 
	// CODE
	if ($("#Account_No").val().trim() == "") 
	 { 
	 return "Insert Account No."; 
	 } 
	// NAME
	if ($("#From_Date").val().trim() == "") 
	 { 
	 return "Insert From Date."; 
	 } 
	
	// PRICE-------------------------------
	if ($("#To_Date").val().trim() == "") 
	 { 
	 return "Insert To Date."; 
	 } 
	
	if ($("#Current_Reading").val().trim() == "") 
	 { 
	 return "Insert Current Reading."; 
	 } 
	 
	
	 
	// DESCRIPTION------------------------
	if ($("#Status").val() == "0")
	{
		return "Insert Bill status.";
	}
	
	return true; 
}
