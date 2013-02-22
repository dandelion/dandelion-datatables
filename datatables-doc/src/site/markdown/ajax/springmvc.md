### Spring MVC integration

#### Introduction
Since 0.8.2, a Spring3 module has been added to <strong>Dandelion-datatables</strong>. It's a bit light at the moment but you can already use it if you need server-side processing.

Indeed, a custom annotation has been added in order to automatically map the DataTables request parameter (sent when server-side processing is enabled) in the `DatatablesCriterias` bean : `@DatatablesParams`.

#### How to use it ?
First, add the new dependency :

	<dependency>
	   <groupId>com.github.dandelion</groupId>
	   <artifactId>datatables-spring3</artifactId>
	   <version>0.8.4</version>
	</dependency>

Moreover, you need to register a new custom WebArgumentResolvers as follows :
      
	<mvc:annotation-driven>
	   <mvc:argument-resolvers>
	      <bean class="com.github.datatables4j.spring3.ajax.DatatablesCriteriasResolver" />
	   </mvc:argument-resolvers>
	</mvc:annotation-driven>

Once the dependency added, just update your Spring MVC controller as follows :
     
	@RequestMapping(value = "/persons2")
	public @ResponseBody DatatablesResponse<Person> getCustomers1(@DatatablesParams DatatablesCriterias criterias) {
	   DataSet<Person> dataSet = personService.findPersonsWithDatatablesCriterias(criterias);
	   return DatatablesResponse.build(dataSet, criterias);
	}

As you can see, it's just the Spring equivalent of `DatatablesCriterias.getFromRequest(HttpServletRequest request)`

#### Using JSP
	
	<datatables:table id="myTableId" url="/persons2" serverSide="true" processing="true">
	    <datatables:column title="Id" property="id" />
	    <datatables:column title="FirstName" property="firstName" />
	    <datatables:column title="LastName" property="lastName" />
	    <datatables:column title="City" property="address.town.name" />
	    <datatables:column title="Mail" property="mail" />
	</datatables:table>

#### Using Thymeleaf
   
	<table id="myTableId" dt:table="true" dt:url="/persons2" dt:serverside="true" dt:processing="true">
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

