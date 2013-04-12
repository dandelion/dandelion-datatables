<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglib.jsp"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/head.jsp"%>
</head>
<body>
   <datatables:table id="myTableId" url="/persons" row="person">
      <datatables:column title="Id" property="id" cssClass="column1class" />
      <datatables:column title="FirstName" property="firstName" cssClass="column2class" cssStyle="text-align:center;" />
      <datatables:column title="LastName" property="lastName" cssStyle="text-align:center;" />
      <datatables:column title="City" property="address.town.name" cssCellClass="cityClass" />
      <datatables:column title="Mail" property="mail" />
   </datatables:table>
</body>
</html>