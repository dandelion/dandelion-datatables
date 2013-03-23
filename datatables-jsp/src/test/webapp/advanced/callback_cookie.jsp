<%@ page pageEncoding="UTF-8"%>
<%@ include file="../common/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/head.jsp"%>
<script>
	function callback(sName, oData, sExpires, sPath) {
		console.log(sName);
		console.log(oData);
		console.log(sExpires);
		console.log(sPath);
	}
</script>
</head>
<body>
   <datatables:table id="myTableId" data="${persons}">
      <datatables:column title="Id" property="id" />
      <datatables:column title="Firstname" property="firstName" />
      <datatables:column title="LastName" property="lastName" />
      <datatables:column title="City" property="address.town.name" sortable="false" />
      <datatables:column title="Mail" property="mail" />
      <datatables:callback type="cookie" function="callback" />
   </datatables:table>
</body>
</html>