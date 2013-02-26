### Advanced :: Row id

Sometimes, it can be useful to add an HTML id to each row (tr tags). Sometimes too, the row id cannot just be an incremented id but a dynamic builded string, for instance, from a bean\'s attribute.

<br />
#### Using JSP
You have at one's disposal 3 table attributes : `rowIdBase`, `rowIdPrefix` and `rowIdSuffix`.

 * `rowIdBase` : String **evaluated** as a property of the current iterated bean
 * `rowIdPrefix` : String prepended to the `rowIdBase` attribute
 * `rowIdSufix` : String appended to the `rowIdBase` attribute

<p class="alert alert-warn"><strong>Warning!</strong><br /> Currently, the row id feature can only be used with DOM sources (not AJAX).</p>

In the following example, Dandelion-Datatables will build rows (tr tags) with the ids : person_1, person_2, \...

	<datatables:table id="myTableId" data="${persons}" rowIdBase="id" rowIdPrefix="person_">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail" property="mail" />
	</datatables:table>

<br />
#### Using Thymeleaf
Nothing\'s specific to **Dandelion-Datatables** is needed, just the native Thymeleaf `th:attr` attribute!

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
	      <tr th:each="person : ${persons}" th:attr="id=${'person_' + person.id}">
	         <td th:text="${person.id}">1</td>
	         <td th:text="${person.firstName}">John</td>
	         <td th:text="${person.lastName}">Doe</td>
	         <td th:text="${person.address.town.name}">Nobody knows !</td>
	         <td th:text="${person.mail}">john@doe.com</td>
	      </tr>
	   </tbody>
	</table>