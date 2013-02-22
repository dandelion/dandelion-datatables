### How to use AJAX source

It can be useful to populate the table using an AJAX source, let's say web service returning your entities.

Since v0.8.2, **Dandelion-datatables** support AJAX sources. But be aware, even if the data is obtained from the server via an AJAX call, DataTables will process it client-side, as for classic DOM sources. So this may be not suitable for large data sets (as DOM sources).

<p class="alert alert-info"><strong>Info !</strong> <br /> At the moment, DataTables only consumes JSON format so take care to convert data in this format.</p>

You can see an example [here](https://github.com/Dandelion/dandelion-samples/blob/master/datatables-jsp/src/main/java/com/github/dandelion/datatables/ajax/SpringMvcAjaxController.java#L37-L41), that uses server-side a Spring MVC controller, AJAX-enabled thanks to the `@ResponseBody` annotation ([available since Spring 3.0](http://blog.springsource.org/2010/01/25/ajax-simplifications-in-spring-3-0/)).

Since Spring performs the JSON serialization using the default Jackson JSON processor, make sure to have the [Jackson JARs](http://mvnrepository.com/artifact/org.codehaus.jackson/jackson-mapper-asl) in your classpath.

<br />      
#### Using JSP
Using JSP, be aware to fill in the `url` table attribute with your AJAX source URL.
Moreover:

 * If the value you pass in begins with a leading slash ("/"), the full request context will be prepended
 * Otherwise, the value is left untouched


    <datatables:table id="myTableId" url="/persons" row="person">
        <datatables:column title="Id" property="id" />
        <datatables:column title="FirstName" property="firstName" />
        <datatables:column title="LastName" property="lastName" />
        <datatables:column title="City" property="address.town.name" />
        <datatables:column title="Mail" property="mail" />
    </datatables:table>

#### Using Thymeleaf
Using Thymeleaf, you have to fill in the `dt:url` table attribute. The same rules apply as for JSP regarding the value you set.

Moreover, you have to tell <strong>Dandelion-datatables</strong> which property must be read from the JSON source for each column using the `dt:property` attribute.

    <table id="myTableId" dt:table="true" dt:url="/persons">
        <thead>
            <tr>
                <th dt:property="id">Id</th>
                <th dt:property="firstName">Firstname</th>
                <th dt:property="lastName">Lastname</th>
                <th dt:property="address.town.name">City</th>
                <th dt:property="mail">Mail</th>
            </tr>
        </thead>
     </table></tab:code>

<p class="alert alert-warn"><strong>Warning !</strong><br />Cross-domain requests are not yet supported !</p>