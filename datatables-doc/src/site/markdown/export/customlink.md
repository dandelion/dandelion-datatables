### Export :: Links style and position

By defaut, export links are a bit ugly but you can of course customize them to fit your needs.

<br />
### Link style
#### Using JSP
You can use the `datatables:export` tag to customize the export links. This tag allows you to configure one type of export.

Using this tag, you can for instance add CSS classes to the links or change the link\'s label.

	<datatables:table id="myTableId" data="${persons}" export="csv,xml">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail" property="mail" />
	   <datatables:export type="csv" cssClass="btn btn-success" />
	   <datatables:export type="xml" cssClass="btn btn-info" label="XML export !" />
	</datatables:table>

#### Using Thymeleaf
<p class="alert alert-error"><strong>:-(</strong><br /> Not supported yet</p>

<br />
### Link position
#### Using JSP
Depending on your needs, you may want to move links around the table. You can do it using the `exportLinks` table attribute.

Just set on or more values (comma-separated) among `top_left`, `top_middle`, `top_right`, `bottom_left`, `bottom_middle` and `bottom_right`.

	<datatables:table id="mySecondTableId" data="${persons}" export="csv,xml" exportLinks="top_right,bottom_right">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail" property="mail" />
	   <datatables:export type="csv" cssClass="btn btn-info" />
	   <datatables:export type="xml" cssClass="btn btn-info" />
	</datatables:table>

#### Using Thymeleaf
<p class="alert alert-error"><strong>:-(</strong><br /> Not supported yet</p>
