### Export :: Adapt export\'s content

It happens that some of your columns contain HTML code that you don\'t want to be rendered in the export (e.g. an \"Action\" column with some links).
By default, all column are exported but it could be useful to remove some column or just adapt its content only for exports.

In this regards, **Dandelion-Datatables** allows you to adapt export's content.

<br />
#### Using JSP
This is done thanks to the `display` column attribute. The authorized values are : `csv`, `xml`, `xls`, `xlsx `and `pdf`, i.e. all authorized export types plus `html`.

So, for instance, setting the `display` attribute to `html` means that the corresponding column will only appear in your web page (html), not in exports.
This way, you can adapt the content of each export. 

In the following example, we don\'t want HTML code to be displayed in the exported \"Mail\" column. 
That\'s why a second \"Mail\" column is added with the `display` attribute set to `csv,xls` (i.e. not `html`) and with just the bean\'s property inside (thanks to the `property` attribute).

	<datatables:table id="myTableId" data="${persons}" row="person" export="csv,xls">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" display="html" />
	   <datatables:column title="Mail" display="html">
	      <a href="mailto:${person.mail}">${person.mail}</a>
	   </datatables:column>
	   <datatables:column title="Mail" property="mail" display="csv,xls" />
	   <datatables:export type="xls" includeHeader="true" fileName="my-export-name" cssClass="btn" label="xls" />
	</datatables:table>

<br />
#### Using Thymeleaf
<p class="alert alert-error"><strong>:-(</strong><br /> Not supported yet</p>
