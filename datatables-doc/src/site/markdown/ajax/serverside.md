### Server-side processing

#### Introduction

If you are working with seriously large databases, you might want to consider using the server-side options that DataTables provides. 

Most of the DataTables features (paging, sorting, filtering, ...) can be handed off to a server. For this purpose, DataTables provides <a href="http://datatables.net/usage/server-side">some parameters</a> (sent via an XHR request) that can be processed in order to return just the data that are needed to display the table.

Since [v0.8.2](https://github.com/datatables4j/issues/issues?milestone=3&page=1&state=closed), *Dandelion-datatables* supports server-side processing by providing some utility classes that should help you to set this up.

<br />
#### What is server-side processing using Dandelion-datatables ?

Actually, the following steps are performed :

 - On table loading (or on every action performed on it), an XHR request is sent to the server with the DataTables parameters
 - The controller intercepts the request, maps its parameters into a <code>DatatablesCriterias</code>  bean using the utility `DatatablesCriterias.getFromRequest(HttpServletRequest request)` method
 - The various classic layers of the application are passed through with the DataTables criterias, SQL queries are performed depending on the criterias and the results are wrapped in a `DataSet` bean, that contains all the required informations to build a response understandable by DataTables
 - A `DatatablesResponse` is built using the returned `DataSet` and sent back to DataTables in the right format (JSON), allowing the table to be refreshed !

<br />         
#### What do I need to do ?
Obviously, server-side processing requires a bit more work than client-side one. Here are the needed steps to set this up.

 * Prepare the needed SQL queries using your favorite ORM framework<br />Waiting for other implementations are added, you can browse the following example that uses [the Hibernate implementation of JPA](https://github.com/Dandelion/dandelion-samples/blob/master/datatables-jsp/src/main/java/com/github/dandelion/datatables/dao/PersonDao.java).
 * Adapt existing or create new [business services](https://github.com/Dandelion/dandelion-samples/blob/master/datatables-jsp/src/main/java/com/github/dandelion/datatables/service/impl/PersonServiceJpaImpl.java) using the **Dandelion-datatables** utility classes
 * Create the web service that will be used by DataTables to perform the AJAX request using the <strong>Dandelion-datatables</strong> utility classes<br /> The following example uses [AJAX-enabled Spring controllers](https://github.com/Dandelion/dandelion-samples/blob/master/datatables-jsp/src/main/java/com/github/dandelion/datatables/ajax/SpringMvcAjaxController.java).
 * Use the right DataTables4j markup to activate server-side processing. Take a look just below !

<br />
#### Using JSP

    <datatables:table id="myTableId" url="/persons1" serverSide="true" processing="true">
        <datatables:column title="Id" property="id" />
        <datatables:column title="FirstName" property="firstName" />
        <datatables:column title="LastName" property="lastName" />
        <datatables:column title="City" property="address.town.name" />
        <datatables:column title="Mail" property="mail" />
    </datatables:table>

<br />
#### Using Thymeleaf

	<table id="myTableId" dt:table="true" dt:url="/persons1" dt:serverside="true" dt:processing="true">
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