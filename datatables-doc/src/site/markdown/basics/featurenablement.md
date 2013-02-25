### Basics :: Feature enablement/disablement

Basic features as sorting, paging, filtering, \... can be easily enabled/disabled using the corresponding table attribute.
You can see beelow all the features you can enable/disable setting its value to `true`/`false`:

<table>
  <thead>
    <tr>
      <th>Feature</th>
      <th>JSP attribute</th>
      <th>Thymeleaf attribute</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Sorting</td>
      <td>sort</td>
      <td>dt:sort</td>
    </tr>
    <tr>
      <td>Filtering (Global top right input field)</td>
      <td>filter</td>
      <td>dt:filter</td>
    </tr>
    <tr>
      <td>Paging</td>
      <td>paginate</td>
      <td>dt:paginate</td>
    </tr>
    <tr>
      <td>Info ("Showing 1 to 10 of 200 entries")</td>
      <td>info</td>
      <td>dt:info</td>
    </tr>
    <tr>
      <td>Length change (top left drop down list)</td>
      <td>lengthChange</td>
      <td>dt:lengthchange</td>
    </tr>
  </tbody>
</table>

Note that those features are by default enabled.

<br />
#### Using JSP
For instance, using JSP, you can set the `sort` table attribute to `false` to disable sorting.

	<datatables:table id="myTableId" data="${persons}" sort="false">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail" property="mail" />
	</datatables:table>

<br />
#### Using Thymeleaf
Using Thymeleaf, set the `dt:sort` table attribute to `false`.

	<table id="myTable" dt:table="true" dt:sort="false">
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
