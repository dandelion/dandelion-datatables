### Advanced :: Overriding default configuration

By default, **Dandelion-Datatables** internally uses a properties file containing all needed configuration. But there are two ways to override it.

 * First, you can add a file called **datatables.properties** in your classpath, allowing you to redefine every property you need. Your custom global configuration will then be merged with the [default one](./../properties.html).
 * Or you can locally override properties using the `datatables:prop` tag. Just define the property\'s name and value.

For instance, the compression feature is disabled by default. Thanks to the `datatables:prop` tag, you can enable it locally, just for the current table.

#### Using JSP

	<datatables:table id="myTableId" data="${persons}">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail" property="mail" />
	   <datatables:prop name="compressor.enable" value="true" />
	</datatables:table>

##### Using Thymeleaf
<p class="alert alert-error"><strong>:-(</strong><br /> Not supported yet</p>