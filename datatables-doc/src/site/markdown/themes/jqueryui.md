### Themes :: jQuery UI

In order to activate the _jQueryUI_ theme, just :

 * set the `theme` / `dt:theme` table attribute to `jqueryui` (JSP/Thymeleaf)
 * set a `themeOption` / `themeOption` (JSP/Thymeleaf) among the 24 supported jQuery UI themes : `blacktie`, `blitzer`, `cupertino`, `darkhive`, `dotluv`, `eggplant`, `excitebike`, `flick`, `hotsneaks`, `humanity`, `lefrog`, `mintchoc`, `overcast`, `peppergrinder`, `redmond`, `smoothness`, `southstreet`, `start`, `sunny`, `swankypurse`, `trontastic`, `uidarkness`, `uilightness`, and `vader`.

Note that the following assets still need to be loaded in your web page :

 * jQuery of course :-)
 * jQuery-UI (JS)
 * DataTables (JS)

<br />
#### Using JSP

	<datatables:table id="myTableId" data="${persons}" theme="jqueryui" themeOption="blacktie">
	    <datatables:column title="Id" property="id" />
	    <datatables:column title="FirstName" property="firstName" />
	    <datatables:column title="LastName" property="lastName" />
	    <datatables:column title="City" property="address.town.name" />
	    <datatables:column title="Mail" property="mail" />
	</datatables:table>

<br />
#### Using Thymeleaf

	<table id="myTable" dt:table="true" dt:theme="jquery" themeOption="blacktie">
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
