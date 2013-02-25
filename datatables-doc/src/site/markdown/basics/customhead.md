### Basics :: Specific column\'s head

A classic use case for column\'s head is the \"master\" checkbox.

<br />
#### Using JSP
This can be done using the `datatables:columnHead` tag and the dedicated `uid` attribute.

As soon as you need to customize a column\'s head, you will have to set the `uid` attribute both on the `datatables:columnHead` and on the corresponding `datatables:column` tags. 

This way, the first will be used to generate the column\'s head whereas the latter will be used for the corresponding column\'s cells.
 
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

<br />
#### Using Thymeleaf
Using Thymeleaf, you just need to use the Standard Thymeleaf Expression.

	<table id="myTableId" dt:table="true">
	   <thead>
	      <tr>
	         <th>Id</th>
	         <th>Firstname</th>
	         <th>Lastname</th>
	         <th>City</th>
	         <th>Mail</th>
	         <th dt:sortable="false">
	            <input type="checkbox" onclick="$('#myTableId').find(':checkbox').attr('checked', this.checked);" />
	         </th>
	      </tr>
	   </thead>
	   <tbody>
	      <tr th:each="person : ${persons}">
	         <td th:text="${person.id}">1</td>
	         <td th:text="${person.firstName}">John</td>
	         <td th:text="${person.lastName}">Doe</td>
	         <td th:text="${person.address.town.name}">Nobody knows !</td>
	         <td th:text="${person.mail}">john@doe.com</td>
	         <td style="text-align: center;"><input type="checkbox" th:value="${person.id}" /></td>
	      </tr>
	   </tbody>
	</table>
