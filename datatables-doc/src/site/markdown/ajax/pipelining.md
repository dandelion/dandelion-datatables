### Pipelining data

#### Introduction
When using server-side processing with DataTables, it can be quite intensive on your server having an Ajax call every time the user performs some kind of interaction - you can effectively DDOS your server with your own application!

You might over-come this by modifying the request set to the server to retrieve more information than is actually required for a single page's display. This means that the user can page multiple times (5 times the display size is the default) before a request must be made of the server. Paging is typically the most common interaction performed with a DataTable, so this can be most beneficial to your server's resource usage. Of course the pipeline must be cleared for interactions other than paging (sorting, filtering etc), but that's the trade off that can be made (sending extra information is cheap - while another XHR is expensive).

<br />
#### Using JSP
Just set the `pipelining` table attribute to `true`.
You can also set the pipe size using the `pipeSize` attribute (which is defaults set to 5).

	<datatables:table id="myTableId" url="/persons2" serverSide="true" processing="true" pipelining="true" pipeSize="6">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail" property="mail" />
	</datatables:table>

<br />
#### Using Thymeleaf
Just set the `dt:pipelinin` to `true`.
The pipe size can be set using the `dt:pipesize` attribute. 

	<table id="myTableId" dt:table="true" dt:url="/persons2" dt:serverside="true" dt:pipelining="true" dt:pipesize="6">
	   <thead>
	      <tr>
	         <th dt:property="id">Id</th>
	         <th dt:property="firstName">Firstname</th>
	         <th dt:property="lastName">Lastname</th>
	         <th dt:property="address.town.name">City</th>
	         <th dt:property="mail">Mail</th>
	      </tr>
	   </thead>
	</table>