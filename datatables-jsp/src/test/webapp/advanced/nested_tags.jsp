<%@ page pageEncoding="UTF-8"%>
<%@ include file="../common/taglib.jsp"%>

<c:set var="yes" value="true" />
<c:set var="no" value="false" />

<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/head.jsp"%>
</head>
<body>
   <datatables:table id="myTableId" data="${persons}" row="person">
      <c:if test="${yes}">
         <datatables:column title="Id" property="id" />
      </c:if>
      <c:forEach var="i" begin="1" end="3"> 
         <datatables:column title="FirstName" property="firstName" />
      </c:forEach>
      <datatables:column title="LastName" property="lastName" />
      <datatables:column title="City" property="address.town.name" />
      <datatables:column title="Mail" property="mail" />
   </datatables:table>
</body>
</html>