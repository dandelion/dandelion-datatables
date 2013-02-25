### Export :: Exporting data

**Dandelion-Datatables** allows you to export your tables in multiple formats.

All plain text exports (as CSV or XML) don\'t require any additional dependency whereas binary ones do (as PDF or XLS). 
In this latter case, make sure the right dependency is present in the classpath.

<br />
#### Using JSP
To activate the export, just set the `export` table attribute to `csv` or whatever format your need among proposed ones.

	<datatables:table id="myTableId" data="${persons}" row="person" export="csv">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail">
	      <a href="mailto:${person.mail}">${person.mail}</a>
	   </datatables:column>
	</datatables:table>

<br />
#### Using Thymeleaf
With Thymeleaf, set the `dt:export` table attribute.

	<table id="myTable" dt:table="true" dt:export="csv">
	   <thead>
	      <tr>
	         <th>Id</th>
	         <th>Firstname</th>
	         <th>Lastname</th>
	         <th>City</th>
	         <th>Mail</th>
	      </tr>
	   </thead>
	   <tbody>
	      <tr th:each="person : ${persons}">
	         <td th:text="${person.id}">1</td>
	         <td th:text="${person.firstName}">John</td>
	         <td th:text="${person.lastName}">Doe</td>
	         <td th:text="${person.address.town.name}">Nobody knows !</td>
	         <td><a th:href="${'mailto:' + person.mail}" th:text="${person.mail}">john@doe.com</a></td>
	      </tr>
	   </tbody>
	</table>

