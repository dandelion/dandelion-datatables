<%@ page pageEncoding="UTF-8"%>
<%@ include file="../common/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/head.jsp"%>
<script>
	function callback(oSettings, iStart, iEnd, iMax, iTotal, sPre) {
		console.log(oSettings);
		console.log(iStart);
		console.log(iEnd);
		console.log(iMax);
		console.log(iTotal);
		console.log(sPre);
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
      <datatables:callback type="info" function="callback" />
   </datatables:table>
</body>
</html>