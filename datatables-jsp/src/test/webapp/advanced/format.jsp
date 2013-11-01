<%@ page pageEncoding="UTF-8"%>
<%@ include file="../common/taglib.jsp"%>

<!DOCTYPE html>
<html>
<head>
</head>
<body>
   <datatables:table id="myTableId" data="${persons}">
      <datatables:column title="Id" property="id" />
      <datatables:column title="FirstName" property="firstName" />
      <datatables:column title="LastName" property="lastName" />
      <datatables:column title="City" property="address.town.name" />
      <datatables:column title="Mail" property="mail" format="<a href=''mailto:{0}''>{0}</a>" />
      <datatables:column title="Birth date" property="birthDate" format="{0,date,dd-MM-yyyy}" />
      <datatables:column title="Test2" property="birthDate" format="{0,date,wrong-format}" />
   </datatables:table>
</body>
</html>