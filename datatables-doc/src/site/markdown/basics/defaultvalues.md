### Basics :: Handling null/default values

By default, **Dandelion-Datatables** displays an empty string when a bean\'s value is null.

<br />
#### Using JSP
You can override the empty string using the `default` column attribute.

	<datatables:table id="myTableId" data="${persons}">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" default="Nothing to display !" />
	   <datatables:column title="Mail" property="mail" />
	</datatables:table>

<br />
#### Using Thymeleaf
Just use a native conditionnal operator!

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
	   <tbody th:remove="all-but-first">
	      <tr th:each="person : ${persons}">
	         <td th:text="${person.id}">1</td>
	         <td th:text="${person.firstName}">John</td>
	         <td th:text="${person.lastName}">Doe</td>
	         <td th:text="${person.address != null and person.address.town != null} ? ${person.address.town.name} : 'Nothing to display !'">Nobody knows !</td>
	         <td th:text="${person.mail}">john@doe.com</td>
	      </tr>
	   </tbody>
	</table>