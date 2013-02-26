### Plugins :: FixedHeader

This plugin allows you to fixed the table header by scrolling.

<br />
#### Using JSP
You just need to set the `fixedheader` table attribute to `true` to activate the plugin.

	<datatables:table id="myTableId" data="${persons}" fixedHeader="true" offsetTop="40">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail" property="mail" />
	</datatables:table>

<br />
#### Using Thymeleaf
Just set the `dt:fixedheader` thead attribute to `true`.

	<table id="myTable" dt:table="true">
	   <thead dt:fixedheader="true">
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
