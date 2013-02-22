### Installation guide with JSP

#### With Servlet 3.x compatible containers

##### Step 1 : Install the Dandelion-DataTables JARs

###### Manually

Download the JAR (see [here](../download.html)) and make sure that they are all made available via the application classpath. (Usually in **WEB-INF\\lib**)

######Using Maven
 
Just add the following dependency in the <tt>dependencies</tt> section of your <tt>pom.xml</tt> :

    <dependency>
        <groupId>com.github.dandelion</groupId>
        <artifactId>datatables-jsp</artifactId>
        <version>0.8.4</version>
    </dependency>

##### Step 2 : Declare the taglib definition

Everywhere you want to display a table using <strong>Dandelion-DataTables</strong>, you need to add the taglib definition in your JSP :

	<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables" %>

##### Step 3 : Include client-side dependencies

By default, the Javascript libraries are not embedded in <strong>Dandelion-DataTables</strong>. So ensure you declared somewhere in your pages those <tt>script</tt> and <tt>link</tt> tags :

    <!-- DataTables -->
    <link href="pathToCss/jquery.dataTables.css" rel="stylesheet">
    <script src="pathToJs/jquery.dataTables.min.js"></script> 
	
	<!-- jQuery -->
	<script src="pathToJs/jquery-1.8.3.min.js"></script>

<p class="alert alert-info"><strong>Info !</strong><br /> Actually, you can bypass the DataTables script and link tags using the <a href="./ref.tagtable.html">cdn</a> attribute of the table tag.</p>

You can now begin using the taglib ! :-)

<br />
#### With Servlet 2.x compatible containers

##### Steps 1/2/3
You need to follow the first three steps described above.

<br />
##### Step 4 : Add the backward compatibility dependency

    <dependency>
        <groupId>com.github.dandelion</groupId>
        <artifactId>datatables-servlet2</artifactId>
        <version>0.1.3</version>
    </dependency>
    
<br />
##### Step 5 : Add the Dandelion-DataTables servlet and filter definitions

Add the following configuration to your <tt>web.xml</tt> :

    <!-- DataTables4j servlet definition -->
    <servlet>
        <servlet-name>datatablesController</servlet-name>
        <servlet-class>com.github.dandelion.datatables.extras.servlet2.servlet.DatatablesServlet</servlet-class>
    </servlet>

    <!-- DataTables4j servlet mapping -->
    <servlet-mapping>
        <servlet-name>datatablesController</servlet-name>
        <url-pattern>/datatablesController/*</url-pattern>
    </servlet-mapping>

    <!-- DataTables4j filter definition -->
    <filter>
        <filter-name>datatablesFilter</filter-name>
        <filter-class>com.github.dandelion.datatables.extras.servlet2.filter.DatatablesFilter</filter-class>
    </filter>

    <!-- DataTables4j filter mapping -->
    <filter-mapping>
        <filter-name>datatablesFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

You can now begin using the taglib ! :-)

<br />
#### Optional step : Add a Dandelion-DataTables custom configuration file

See the [properties reference](../properties.html) to see more details !