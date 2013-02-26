### Plugins :: Scroller

This plugin allows you to scroll inside the table data.

<br />
#### Using JSP
You just need to set the `scroller` table attribute to `true` to activate the Scroller plugin.

By default, the height is `300px` but you can configure it with the `scrollY` table attribute.

	<datatables:table id="myTableId" data="${persons}" scroller="true">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail" property="mail" />
	</datatables:table>

<br />
#### Using Thymeleaf
Just set the `dt:scroller` thead attribute to `true`.

	<table id="myTable" dt:table="true">
	   <thead dt:scroller="true">
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
