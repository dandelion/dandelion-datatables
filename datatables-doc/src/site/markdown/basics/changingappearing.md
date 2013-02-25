### Basics :: Changing table\'s appearing

By default, the table is displayed during the HTML markup and the server-side Javascript generation, sometimes resulting in a ugly display for some milliseconds, depending on browsers. 

This behaviour can be changed using the `appear` / `dt:appear` table attribute (JSP/Thymeleaf).

In the following example, a duration of 1500 milliseconds has been set to the fadeIn animation instead of the default 400 milliseconds but you cal also set the `block` value to trigger the jQuery show() function.

<br />
#### Using JSP

	<datatables:table id="myTableId" data="${persons}" appear="fadein,1500">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail" property="mail" />
	</datatables:table>

#### Using Thymeleaf

	<table id="myTable" dt:table="true" dt:appear="fadein,1500">
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