### Export :: Exporting data

**Dandelion-Datatables** allows you to export your tables in multiple formats.

<br />
#### Available formats
The following formats are available : CSV, XML, PDF, XLS and XLSX.

All plain text exports (as CSV or XML) don\'t require any additional dependency whereas binary ones do (as PDF or XLS). 

Regardind binary exports, **Dandelion-Datatables** provides some extras : [datatables-export-itext](https://github.com/dandelion/dandelion-datatables/tree/master/datatables-extras/datatables-export-itext), [datatables-export-poi](https://github.com/dandelion/dandelion-datatables/tree/master/datatables-extras/datatables-export-poi), [datatables-export-poi-ooxml](https://github.com/dandelion/dandelion-datatables/tree/master/datatables-extras/datatables-export-poi-ooxml) which always embed :

 * the third-party dependency (e.g. the iText library) 
 * a basic export class that will be used by default by **Dandelion-Datatables**

You can of course write your own export class to fit your needs.

<br />
#### Servlet filter
Using containers that are Servlet 3.x compatibles (as Tomcat7), the filter is automatically declared, no other configuration is required. 

But using containers that are Servlet 2.x compatibles (as Tomcat6), some configuration is needed.

First, you will need to add an extra dependency in your `pom.xml`:

    <dependency>
        <groupId>com.github.dandelion</groupId>
        <artifactId>datatables-servlet2</artifactId>
        <version>0.8.6</version>
    </dependency>

Then, you need to declare the Datatables servlet filter in you `web.xml` as follows :

    <!-- Dandelion-Datatables filter definition -->
    <filter>
        <filter-name>datatablesFilter</filter-name>
        <filter-class>com.github.dandelion.datatables.extras.servlet2.filter.DatatablesFilter</filter-class>
    </filter>

    <!-- Dandelion-Datatables filter mapping -->
    <filter-mapping>
        <filter-name>datatablesFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping> 

<p class="alert alert-warn"><strong>Important note</strong><br /> Ensure to declare the Datatables servlet filter after any other filter that is used to encode your data, as the typical <strong>org.springframework.web.filter.CharacterEncodingFilter</strong></p>

<br />
#### Using JSP
To activate the export, just set the `export` table attribute to `csv` or whatever format your need among proposed ones. An export link will be generated (defaults to top right of the table).

	<datatables:table id="myTableId" data="${persons}" row="person" export="csv">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail">
	      <a href="mailto:${person.mail}">${person.mail}</a>
	   </datatables:column>
	</datatables:table>

<br />
#### Using Thymeleaf
With Thymeleaf, set the `dt:export` table attribute.

	<table id="myTable" dt:table="true" dt:export="csv">
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

