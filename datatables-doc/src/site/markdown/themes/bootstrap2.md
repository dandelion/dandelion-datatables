### Themes :: Bootstrap 2

In order to activate the _Bootstrap 2_ theme, just :

 * set the `theme` / `dt:theme` table attribute to `bootstrap2` (JSP/Thymeleaf)
 * depending on your needs, add the table Bootstrap CSS classes using the `cssClass` / `class` table attribute (JSP/Thymeleaf)

Note that the following assets still need to be loaded in your web page :

 * jQuery of course :-)
 * Bootstrap (JS + CSS)
 * DataTables (JS + CSS + IMG)

<br />
#### Using JSP

	<datatables:table id="myTableId" cssClass="table table-striped table-bordered table-condensed" data="${persons}" theme="bootstrap2">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="Street" property="address.town.name" />
	   <datatables:column title="Mail" property="mail" />
	</datatables:table>

<br />
#### Using Thymeleaf

	<table id="myTable" dt:table="true" dt:theme="bootstrap2" class="table table-striped table-bordered table-condensed">
	   <thead>
	      <tr>
	         <th>Id</th>
	         <th>Firstname</th>
	         <th>Lastname</th>
	         <th>Street</th>
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
