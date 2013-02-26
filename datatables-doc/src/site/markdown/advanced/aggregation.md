### Advanced :: Using aggregation

<img src="./../images/logo_yui_compressor.jpg" style="float:right;" width="250px" height="75px" />
**Dandelion-Datatables** allows you to have all your web resources minimified. A compression module has been written, based on YUI Compressor, allowing you to compress both Javascript and CSS code. If you don't want to use YUI Compressor, you can also write your own compression module, using the compressor you want.

<br />
#### How to activate compression ?

Two things are needed to enable compression :

 * Tell **Dandelion-Datatables** you want to use compression :
	
	* Either you want all the tables in your application to be affected, so just set the property `compressor.enable` of your custom properties file to `true`
	* Or you want to activate compression in a few pages only, so locally override the default configuration. See how [here](overrideconf.html).


 * Add the YUI Compressor module to your classpath :
 
	* Using Maven or Maven-compatible dependency manager, just add the following dependency to your POM :

		<dependency>
			<groupId>com.github.datatables4j</groupId>
			<artifactId>datatables4j-compression-yui</artifactId>
			<version>0.3.7</version>
		</dependency>
	
	* Manually, just grab the latest version of the module [here](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22datatables-compression-yui%22) and all needed dependencies.

<br />
#### Using JSP

	<datatables:table id="myTableId" data="${persons}">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="FirstName" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" />
	   <datatables:column title="Mail" property="mail" />
	   <datatables:prop name="compressor.enable" value="true" />
	</datatables:table>

<br />
#### Using Thymeleaf
<p class="alert alert-error"><strong>:-(</strong><br /> Not supported yet</p>
