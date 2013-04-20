<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglib.jsp"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/head.jsp"%>
</head>
<body>
   <script>
      function myInitCallback(oSettings, json){
         console.log("test");  
      }
   </script>
   
   <datatables:table id="myTableId" url="/persons">
      <datatables:column title="Id" property="id" />
      <datatables:column title="FirstName" property="firstName" />
      <datatables:column title="LastName" property="lastName" />
      <datatables:column title="City" property="address.town.name" default="default value" />
      <datatables:column title="Mail" property="mail" />
      <datatables:callback type="init" function="myInitCallback" />
   </datatables:table>
</body>
</html>