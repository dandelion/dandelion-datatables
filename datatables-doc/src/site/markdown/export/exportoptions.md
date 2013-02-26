### Export :: Export options

Some options are available to customize your exports.

<br />
#### Remove header (all formats)
Usually a header that contains all column's heads is created in all exports.

##### Using JSP
You can remove it using the `includeHeader` attribute of the `datatables:export` tag, which is defaults set to `true`.

	<datatables:table id="myFirstTableId" data="${persons}" row="person" export="csv">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail">
	      <a href="mailto:${person.mail}">${person.mail}</a>
	   </datatables:column>
	   <datatables:export type="csv" includeHeader="false" fileName="my-export-name" cssClass="btn" label="CSV without header row" />
	</datatables:table>

##### Using Thymeleaf
<p class="alert alert-error"><strong>:-(</strong><br /> Not supported yet</p>

<br />
#### Column auto-sizing (only XLS/XLSX)
For XLS and XLSX export formats, you can tell Dandelion-Datatables to auto-size columns.

##### Using JSP
Just set the `autoSize` attribute of the `datatables:export` tag to `true`.

	<datatables:table id="mySecondTableId" data="${persons}" row="person" export="xls">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail">
	      <a href="mailto:${person.mail}">${person.mail}</a>
	   </datatables:column>
	   <datatables:export type="XLS" autoSize="true" fileName="my-export-name" cssClass="btn" label="XLS" />
	</datatables:table>

##### Using Thymeleaf
<p class="alert alert-error"><strong>:-(</strong><br /> Not supported yet</p>
