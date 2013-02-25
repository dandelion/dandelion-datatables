### Basics :: Multiple tables

**Dandelion-Datatables** can handle multiple tables on the same page.

Just ensure to give a unique ID to each table.

<br />
#### Using JSP

	<datatables:table id="myTableId" data="${persons}">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="Firstname" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" sortable="false" />
	   <datatables:column title="Mail" property="mail" />
	</datatables:table>
	 
	<datatables:table id="myOtherTableId" data="${persons}">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="Firstname" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" sortable="false" />
	   <datatables:column title="Mail" property="mail" />
	</datatables:table>

<br />
#### Using Thymeleaf

	<table id="myTableId" dt:table="true">
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
	         <td th:text="${person.mail}">john@doe.com</td>
	      </tr>
	   </tbody>
	</table>
	 
	<table id="myOtherTableId" dt:table="true">
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
	         <td th:text="${person.mail}">john@doe.com</td>
	      </tr>
	   </tbody>
	</table>