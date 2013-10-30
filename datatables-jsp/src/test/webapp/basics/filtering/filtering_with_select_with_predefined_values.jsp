<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/head.jsp"%>
</head>
<body>
   <script>
				var myPredefinedValues = [ 'val1', 'val2', 'val3' ];
			</script>
   <datatables:table id="myTableId" data="${persons}">
      <datatables:column title="Id" property="id" />
      <datatables:column title="FirstName" property="firstName" filterable="true" filterType="select" />
      <datatables:column title="LastName" property="lastName" filterable="true" filterType="select" filterValues="myPredefinedValues" />
      <datatables:column title="City" property="address.town.name" />
      <datatables:column title="Mail" property="mail" />
   </datatables:table>
</body>
</html>