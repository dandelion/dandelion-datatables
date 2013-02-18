### Migrating from DataTables4j to Dandelion-datatables
**Dandelion-datatables** is a continuation of the previous [DataTables4j](http://datatables4j.github.com/docs) project. For now, it's just a simple renaming (and also internal and transparent structure simplifications) but in a near future, we will inform you of features and services connected with Dandelion! 

At the moment, we just ask you to start migrating your apps from DataTables4j to Dandelion-datatables. As you will see in the following section, the workload is quite limited! :-) 

<br />
#### Step 1 : update all your dependencies 

The following table lists the old and new dependencies.

<table>
  <thead>
  	<tr>
  	  <th>Description</th>
  	  <th>Maven</th>
  	  <th>Old</th>
  	  <th>New</th>
  	</tr>  
  </thead>
  <tbody>
  	<tr>
  	  <td>Project groupId</td>
  	  <td>groupId</td>
  	  <td>com.github.datatables4j</td>
  	  <td>com.github.dandelion</td>
  	</tr>
  	<tr>
  	  <td>JSP implementation</td>
  	  <td>artifactId</td>
  	  <td>datatables4j-core-jsp</td>
  	  <td>datatables-jsp</td>
  	</tr>
  	<tr>
  	  <td>Thymeleaf implementation</td>
  	  <td>artifactId</td>
  	  <td>datatables4j-core-thymeleaf</td>
  	  <td>datatables-thymeleaf</td>
  	</tr>
  	<tr>
  	  <td>Servlet2.x backward compatibility extra</td>
  	  <td>artifactId</td>
  	  <td>datatables4j-servlet2</td>
  	  <td>datatables-servlet2</td>
  	</tr>
  	<tr>
  	  <td>Spring3 extra</td>
  	  <td>artifactId</td>
  	  <td>datatables4j-spring3</td>
  	  <td>datatables-spring3</td>
  	</tr>
  </tbody>
</table>

**Warning !**
Ensure to delete all DataTables4j dependencies to avoid classpath conflicts.

<br />
#### Step 2 (JSP): update the taglib URI

From:

    <%@ taglib prefix="datatables" uri="http://github.com/datatables4j" %>

To:

    <%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables" %>

<br />
#### Step 2 (Thymeleaf): update the dialect namespace

From:

    <html xmlns:th="http://www.thymeleaf.org" xmlns:dt="http://github.com/datatables4j/thymeleaf">

To:

    <html xmlns:th="http://www.thymeleaf.org" xmlns:dt="http://github.com/dandelion/datatables">

<br />
#### Step 3 (optional) : servlet 2.x
If you were using the [Servlet2 compatibility module](https://github.com/datatables4j/datatables4j-backward-servlet2.x), first ensure to update the corresponding dependency (see above).

Then, ensure to update the qualified class names in `web.xml` :

    <!-- Dandelion-datatables servlet definition -->
    <servlet>
       <servlet-name>datatablesController</servlet-name>
       <servlet-class>com.github.dandelion.datatables.extras.servlet2.servlet.DatatablesServlet</servlet-class>
    </servlet>
    
    <!-- Dandelion-datatables servlet mapping -->
    <servlet-mapping>
       <servlet-name>datatablesController</servlet-name>
       <url-pattern>/datatablesController/*</url-pattern>
    </servlet-mapping>
    
    <!-- Dandelion-datatables filter definition (export feature) -->
    <filter>
       <filter-name>datatablesFilter</filter-name>
       <filter-class>com.github.dandelion.datatables.extras.servlet2.filter.DatatablesFilter</filter-class>
    </filter>
    
    <!-- Dandelion-datatables filter mapping (export feature) -->
    <filter-mapping>
       <filter-name>datatablesFilter</filter-name>
       <url-pattern>/*</url-pattern>
    </filter-mapping>

<br />
#### Step 4 (optional) : Spring 3
If you were using the [Spring 3 extra module](https://github.com/datatables4j/datatables4j-core-parent/tree/master/datatables4j-spring3)(used in server-side processing), first ensure to update the corresponding dependency (see above). 

Then, ensure to update the qualified class name in you Spring MVC configuration:

    <mvc:annotation-driven>
        <mvc:argument-resolvers>
            <bean class="com.github.dandelion.datatables.extras.spring3.ajax.DatatablesCriteriasResolver" />
        </mvc:argument-resolvers>
    </mvc:annotation-driven>
