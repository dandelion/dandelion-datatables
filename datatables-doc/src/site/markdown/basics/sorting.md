### Basics :: Sorting data

<br />
#### Individual column sorting
You can enable/disable the sorting feature in a specific column using the `sortable`/`dt:sortable` column attribute (JSP/Thymeleaf).

##### Using JSP

	<datatables:table id="mySecondTableId" data="${persons}">
	   <datatables:column title="Id" property="id" sortable="false" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail" property="mail" sortable="false" />
	</datatables:table>

##### Using Thymeleaf

	<table id="mySecondTableId" dt:table="true">
	   <thead>
	      <tr>
	         <th dt:sortable="false">Id</th>
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
<br />
#### Sorting initialisation

##### Using JSP
You can also initialise the column sorting direction using the `sortInit` column attribute.
Just set it to `asc` or `desc` on each column you want sorting to be initialised.

	<datatables:table id="myThirdTableId" data="${persons}">
	   <datatables:column title="Id" property="id" sortable="false" />
	   <datatables:column title="FirstName" property="firstName" sortInit="desc" />
	   <datatables:column title="LastName" property="lastName" sortInit="desc" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail" property="mail" sortable="false" />
	</datatables:table>

##### Using Thymeleaf
<p class="alert alert-error"><strong>:-(</strong><br /> Not supported yet</p>

<br />
#### Sorting direction control

##### Using JSP
Finally, you can control the column sorting behaviour using the `sortDirection` column attribute.

You can set it using comma-separated list of `asc` or `desc`.

	<datatables:table id="myFourthTableId" data="${persons}">
	   <datatables:column title="Id" property="id" sortable="false" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" sortDirection="asc" />
	   <datatables:column title="Mail" property="mail" sortDirection="desc" />
	</datatables:table>

##### Using Thymeleaf
<p class="alert alert-error"><strong>:-(</strong><br /> Not supported yet</p>