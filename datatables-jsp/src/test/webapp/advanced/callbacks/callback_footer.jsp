<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../common/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script>
	function callback(nFoot, aData, iStart, iEnd, aiDisplay) {
		console.log(nFoot);
		console.log(aData);
		console.log(iStart);
		console.log(iEnd);
		console.log(aiDisplay);
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
      <datatables:callback type="footer" function="callback" />
   </datatables:table>
</body>
</html>