<%@ page pageEncoding="UTF-8"%>
<%@ include file="../common/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/head.jsp"%>
</head>
<body>
   <datatables:table id="myTableId" data="${persons}" row="person">
      <datatables:column title="Id" property="id" />
      <datatables:column title="FirstName" property="firstName" />
      <datatables:column title="LastName" property="lastName" />
      <datatables:column title="City" property="address.town.name" />
      <datatables:column title="Mail" property="mail" />
      <datatables:columnHead uid="actionColumn">
         <input type="checkbox" onclick="$('#myTableId').find(':checkbox').attr('checked', this.checked);" />
      </datatables:columnHead>
      <datatables:column uid="actionColumn" sortable="false" cssCellStyle="text-align:center;">
         <input type="checkbox" value="${person.id}" />
      </datatables:column>
   </datatables:table>
</body>
</html>