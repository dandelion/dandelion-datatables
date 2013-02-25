### Handling null/default values

This section deals with the case where the bean you\'re trying to display in the table has null properties.

<br /> 
#### Using JSP
Using JSP with AJAX sources, null values are handled in the same way than for DOM sources. 
By default, an empty string will be displayed. You can also use the `default` column attribute to replace the empty string by any string you want.

	<datatables:table id="myTableId" url="/persons">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" default="My default value !" />
	   <datatables:column title="Mail" property="mail" />
	</datatables:table>

<br /> 
#### Using Thymeleaf      
Using Thymeleaf, this is quite similar. Just use the `dt:default` TH attribute to override the default empty string.

	<table id="myTableId" dt:table="true" dt:url="/persons">
	   <thead>
	      <tr>
	         <th dt:property="id">Id</th>
	         <th dt:property="firstName">Firstname</th>
	         <th dt:property="lastName">Lastname</th>
	         <th dt:property="address.town.name" dt:default="My default value !">City</th>
	         <th dt:property="mail">Mail</th>
	      </tr>
	   </thead>
	</table>