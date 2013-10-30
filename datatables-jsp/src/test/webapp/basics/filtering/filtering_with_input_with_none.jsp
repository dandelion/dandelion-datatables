<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglib.jsp"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/head.jsp"%>
</head>
<body>
   <datatables:table id="myTableId" data="${persons}" filterPlaceholder="none">
      <datatables:column title="Id" property="id" />
      <datatables:column title="FirstName" property="firstName" filterable="true" />
      <datatables:column title="LastName" property="lastName" filterable="true" />
      <datatables:column title="City" property="address.town.name" />
      <datatables:column title="Mail" property="mail" />
   </datatables:table>
</body>
</html>