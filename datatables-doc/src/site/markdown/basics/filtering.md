### Basics :: Filtering data

<br />
#### Using input field
##### Using JSP
By default, specific column filtering is disable but you can enable it using the `filterable` column attribute.

If you set the `filterable` attribute to `true`, an input field will be created in the corresponding tfoot cell.

	<datatables:table id="myTableId" data="${persons}">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" filterable="true" />
	   <datatables:column title="LastName" property="lastName" filterable="true" />
	   <datatables:column title="City" property="address.town.name" filterable="true" />
	   <datatables:column title="Mail" property="mail" />
	</datatables:table>

##### Using Thymeleaf
With Thymeleaf, you can use the `dt:filterable` th attribute.

	<table id="myTableId" dt:table="true">
	   <thead>
	      <tr>
	         <th>Id</th>
	         <th dt:filterable="true">Firstname</th>
	         <th dt:filterable="true">Lastname</th>
	         <th dt:filterable="true">City</th>
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

<br />
#### Using drop down lists
By default, if filtering is enabled on a specific column, an input field will be added in the corresponding tfoot cell. But you can also use select boxes!

##### Using JSP
Set the `filterType` column attribute to `select` to replace the input field by a drop down list.

	<datatables:table id="myThirdTableId" data="${persons}">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" filterable="true" filterType="select" />
	   <datatables:column title="LastName" property="lastName" filterable="true" filterType="select" />
	   <datatables:column title="City" property="address.town.name" filterable="true" filterType="select" />
	   <datatables:column title="Mail" property="mail" />
	</datatables:table>

##### Using Thymeleaf
With Thymeleaf, set the `dt:filterType` th attribute to `select`.

	<table id="myThirdTableId" dt:table="true">
	   <thead>
	      <tr>
	         <th>Id</th>
	         <th dt:filterable="true" dt:filterType="select">Firstname</th>
	         <th dt:filterable="true" dt:filterType="select">Lastname</th>
	         <th dt:filterable="true" dt:filterType="select">City</th>
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
