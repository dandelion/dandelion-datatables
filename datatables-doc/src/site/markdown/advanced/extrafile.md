### Advanced :: Using extra file (only JSP)

There are so many possibilities with [DataTables](http://datatables.net/) it would be too bad to limit its configuration with the taglib attributes.

That\'s why `datatables:extraFile` and `datatables:extraConf` tags exist and provide extension points. Let\'s focus on extra file here.

The `datatables:extraFile` tag allows you to specify the location of a file containing Javascript code, which will be simply appended in the main generated Javascript file. 
Depending on what you need to do, the code can be inserted at different locations, using the `insert` extraFile attribute.


#### Using extra file
A classic use case is an external drop down list which can filter the table\'s data.

In the following example, an external drop down list is added to filter the \"lastName\" column of the table.
	
	<p>
	   Lastname filter<br /> 
	   <select class="input-medium" id="lastNameChoice">
	      <option value="All">All</option>
	      <option value="Carpenter">Carpenter</option>
	      <option value="Holloway">Holloway</option>
	      <option value="Peck">Peck</option>
	      <option value="Pena">Pena</option>
	      <option value="Wilder">Wilder</option>
	   </select>
	</p>
	
The **DataTables** initialization code must be then placed in a file that is accessible by your webapp. In the example, the file is located in `/assets/js/datatables.extraFile.js` and contains the following Javascript code :

	$.fn.dataTableExt.afnFiltering.push(
	   function( oSettings, aData, iDataIndex ) {
	      var chosenName = $("#lastNameChoice").val(); // Grab selected item in the dropdown list
	      var nameInColumn = aData[2]; // 2 is the index (begins from 0) of the column I want to sort
	       
	      // If nothing or "All" is selected, don't filter
	      if ( chosenName == "" || chosenName == "All")
	      {
	         return true;
	      }
	      // Filtering on the city name
	      else if ( chosenName == nameInColumn)
	      {
	         return true;
	      }
	      return false;
	   }
	);
	 
	// Redraw the table as soon as the selected item changes
	// Important : the DataTable is accessible through the taglib with the name : oTable_[HTML table id]
	$("#lastNameChoice").change( function() { oTable_myTableId.fnDraw(); });

<p class="alert alert-info"><strong>Info!</strong><br />Note that the global Javascript variable containing the table is accessible in a global scope with the name : <strong>oTable_[HTML table id]</strong>. That's why you can access the table in the script.</p>

Finally, you can add an `datatables:extraFile` tag in your table declaration as follows :

	<datatables:table id="myTableId" data="${persons}">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="Firstname" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" sortable="false" />
	   <datatables:column title="Mail" property="mail" />
	   <datatables:extraFile src="/assets/js/datatables.extraFile.js" />
	</datatables:table>

#### Insert mode
The `datatables:extraFile` tag has an `insert` attribute that is used to determine where the content of the external Javascript file will be inserted in the main generated Javascript file (datatables-XXXXX.js).

<img src="./../images/img_extraFile.png" width="500px" height="1300px" style="float:left;"/>
The `insert` attribute can take 4 different values :

 * `BEFOREALL` : the Javascript code will be inserted before all existing generated code 
 * `AFTERSTARTDOCUMENTREADY` : the Javascript code will be inserted just after the start of the jQuery `ready()` method
 * `BEFOREENDDOCUMENTREADY` : the Javascript code will be inserted just before the end of the jQuery `ready()` method
 * `AFTERALL` : the Javascript code will be inserted after all existing generated code

