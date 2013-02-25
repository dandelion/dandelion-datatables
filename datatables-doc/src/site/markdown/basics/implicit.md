### Basics :: Using implicit object 

<br />
#### Using JSP
**Dandelion-Datatables** make some implicit objects available inside the `datatables:table` tag. This way, you can configure the column\'s content as you wish.

Thanks to the `row` table attribute, you will be able to give a name for the current iterated object of the collection. Then, you can access it using `${givenName.beanProperty}` inside the column tags.

	<datatables:table id="myTableId" data="${persons}" row="person">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail">
	      <a href="mailto:${person.mail}">${person.mail}</a>
	   </datatables:column>
	</datatables:table>

#### Using Thymeleaf
Actually, there\'s nothing specific to **Dandelion-Datatables** here. Thymeleaf natively already allows you to access the iterated object thanks to the `th:each` attribute (see the [documentation](http://www.thymeleaf.org/usingthymeleaf.html) for more details).

	<table id="myTable" dt:table="true">
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
	         <td><a th:href="${'mailto:' + person.mail}" th:text="${person.mail}">john@doe.com</a></td>
	      </tr>
	   </tbody>
	</table>
