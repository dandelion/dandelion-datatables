### Basics :: Specific column\'s content

<br />
#### Using JSP
The `datatables:column` tag can either have a body or not.

Using the `property` attribute, the column\'s content will be filled with the property of the current bean of the iterated collection. In this case, you do not need to set a body for the tag.

Or you can set a body with plain text, HTML code or any other JSP tags. In this case, the `property` attribute will be ignored.

	<datatables:table id="myTableId" data="${persons}" row="person">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail" property="mail" />
	   <datatables:column title="Actions" sortable="false">
	      <a onclick="alert('Person ' + ${person.id} + ' deleted !');" class="btn btn-mini" title="Remove"><i class="icon-trash"></i></a>
	      <a onclick="alert('Person ' + ${person.id} + ' edited !');" class="btn btn-mini" title="Edit"><i class="icon-edit"></i></a>
	   </datatables:column>
	</datatables:table>

<br />
#### Using Thymeleaf
Actually, you just need to use the Standard Thymeleaf Expression to set any content you want inside the columns.

	<table id="myTable" dt:table="true">
	   <thead>
	      <tr>
	         <th>Id</th>
	         <th>Firstname</th>
	         <th>Lastname</th>
	         <th>City</th>
	         <th>Mail</th>
	         <th>Actions</th>
	      </tr>
	   </thead>
	   <tbody>
	      <tr th:each="person : ${persons}">
	         <td th:text="${person.id}">1</td>
	         <td th:text="${person.firstName}">John</td>
	         <td th:text="${person.lastName}">Doe</td>
	         <td th:text="${person.address.town.name}">Nobody knows !</td>
	         <td th:text="${person.mail}">john@doe.com</td>
	         <td>
	            <a th:onclick="'alert(\'Person ' + ${person.id} + ' deleted !\');'" class="btn btn-mini" title="Remove"><i class="icon-trash"></i></a>
	            <a th:onclick="'alert(\'Person ' + ${person.id} + ' edited !\');'" class="btn btn-mini" title="Edit"><i class="icon-edit"></i></a>
	         </td>
	      </tr>
	   </tbody>
	</table>

