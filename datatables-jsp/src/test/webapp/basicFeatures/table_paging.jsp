<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../common/taglib.jsp" %>

<!DOCTYPE html>
<html>
<head>
<jsp:include page="../common/head.jsp" />
</head>
<body>
   <datatables:table id="myTableId" data="${persons}" paginate="false">
      <datatables:column title="Id" property="id" />
      <datatables:column title="FirstName" property="firstName" />
      <datatables:column title="LastName" property="lastName" />
      <datatables:column title="City" property="address.town.name" />
      <datatables:column title="Mail" property="mail" />
   </datatables:table>
</body>
</html>