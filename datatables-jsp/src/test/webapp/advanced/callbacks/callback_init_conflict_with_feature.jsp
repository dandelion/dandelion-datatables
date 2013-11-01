<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../common/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script>
	function callback(oSettings, json) {
		console.log(oSettings);
		console.log(json);
	}
</script>
</head>
<body>
   <datatables:table id="myTableId" url="/persons">
      <datatables:column title="Id" property="id" />
      <datatables:column title="Firstname" property="firstName" />
      <datatables:column title="LastName" property="lastName" />
      <datatables:column title="City" property="address.town.name" sortable="false" />
      <datatables:column title="Mail" property="mail" />
      <datatables:callback type="init" function="callback" />
   </datatables:table>
</body>
</html>