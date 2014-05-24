<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglib.jsp"%>

<!DOCTYPE html>
<html>
<head>
</head>
<body>
   <datatables:table id="myTableId" data="${persons}" export="csv,xls,xml" exportLinks="top_right">
      <datatables:column title="Id HTML" property="id" display="html" />
      <datatables:column title="Id CSV" display="csv">
         Id CSV
      </datatables:column>
      <datatables:column title="Id XML" display="xml">
         Id XML
      </datatables:column>
      <datatables:column title="FirstName" property="firstName" />
      <datatables:column title="LastName" property="lastName" />
      <datatables:column title="City" property="address.town.name" default="default value" />
      <datatables:column title="Mail HTML" display="html">
         HTML
      </datatables:column>
      <datatables:column title="Mail CSV" display="csv">
         CSV
      </datatables:column>
   </datatables:table>
</body>
</html>