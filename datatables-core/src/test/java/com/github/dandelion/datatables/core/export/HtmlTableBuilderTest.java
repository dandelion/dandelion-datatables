/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2015 Dandelion
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Dandelion nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.dandelion.datatables.core.export;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;

import com.github.dandelion.core.Context;
import com.github.dandelion.core.web.WebConstants;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.mock.Person;

import static org.assertj.core.api.Assertions.assertThat;

public class HtmlTableBuilderTest {

   private MockHttpServletRequest request;
   private List<Person> persons;

   @Before
   public void setup() {
      request = new MockHttpServletRequest();
      request.setAttribute(WebConstants.DANDELION_CONTEXT_ATTRIBUTE, new Context(new MockFilterConfig()));

      persons = new ArrayList<Person>();
      for (int i = 1; i <= 10; i++) {
         persons.add(new Person(new Long(i), "firstName" + i, "lastName" + i, "mail" + i));
      }
   }

   @Test
   public void should_generate_table_by_extracting_property_from_the_collection() {

      HtmlTable table = new HtmlTableBuilder<Person>().newBuilder("tableId", persons, request, null)
            .column().fillWithProperty("id").title("Id")
            .column().fillWithProperty("firstName").and("suffix").title("Firtname")
            .column().fillWith("prefix").andProperty("lastName").title("Lastname")
            .column().fillWithProperty("mail", "formatted-{0}").title("Mail")
            .build();
      
      assertThat(table.getBodyRows()).hasSize(10);
      assertThat(table.getBodyRows().get(0).getColumns()).hasSize(4);
      assertThat(table.getBodyRows().get(0).getColumns().get(0).getContent().toString()).isEqualTo("1");
      assertThat(table.getBodyRows().get(0).getColumns().get(1).getContent().toString()).isEqualTo("firstName1suffix");
      assertThat(table.getBodyRows().get(0).getColumns().get(2).getContent().toString()).isEqualTo("prefixlastName1");
      assertThat(table.getBodyRows().get(0).getColumns().get(3).getContent().toString()).isEqualTo("formatted-mail1");
      assertThat(table.getBodyRows().get(9).getColumns().get(0).getContent().toString()).isEqualTo("10");
      assertThat(table.getBodyRows().get(9).getColumns().get(1).getContent().toString()).isEqualTo("firstName10suffix");
      assertThat(table.getBodyRows().get(9).getColumns().get(2).getContent().toString()).isEqualTo("prefixlastName10");
      assertThat(table.getBodyRows().get(9).getColumns().get(3).getContent().toString()).isEqualTo("formatted-mail10");
   }

   @Test
   public void should_generate_table_by_extracting_property_from_the_ajax_criterias() {

      request.addParameter("columns[0][data]", "id");
      request.addParameter("columns[1][data]", "firstName");
      request.addParameter("columns[2][data]", "lastName");
      request.addParameter("columns[3][data]", "mail");

      DatatablesCriterias criteria = DatatablesCriterias.getFromRequest(request);
      
      HtmlTable table = new HtmlTableBuilder<Person>().newBuilder("tableId", persons, request, null)
            .column().fillFromCriteria(criteria).title("Id")
            .column().fillFromCriteria(criteria).title("Firtname")
            .column().fillFromCriteria(criteria).title("Lastname")
            .column().fillFromCriteria(criteria, "formatted-{0}").title("Mail")
            .build();
      
      assertThat(table.getBodyRows()).hasSize(10);
      assertThat(table.getBodyRows().get(0).getColumns()).hasSize(4);
      assertThat(table.getBodyRows().get(0).getColumns().get(0).getContent().toString()).isEqualTo("1");
      assertThat(table.getBodyRows().get(0).getColumns().get(1).getContent().toString()).isEqualTo("firstName1");
      assertThat(table.getBodyRows().get(0).getColumns().get(2).getContent().toString()).isEqualTo("lastName1");
      assertThat(table.getBodyRows().get(0).getColumns().get(3).getContent().toString()).isEqualTo("formatted-mail1");
      assertThat(table.getBodyRows().get(9).getColumns().get(0).getContent().toString()).isEqualTo("10");
      assertThat(table.getBodyRows().get(9).getColumns().get(1).getContent().toString()).isEqualTo("firstName10");
      assertThat(table.getBodyRows().get(9).getColumns().get(2).getContent().toString()).isEqualTo("lastName10");
      assertThat(table.getBodyRows().get(9).getColumns().get(3).getContent().toString()).isEqualTo("formatted-mail10");
   }
   
   @Test
   public void should_autogenerate_table_from_the_ajax_criterias() {

      request.addParameter("columns[0][data]", "id");
      request.addParameter("columns[1][data]", "firstName");
      request.addParameter("columns[2][data]", "lastName");
      request.addParameter("columns[3][data]", "mail");

      DatatablesCriterias criteria = DatatablesCriterias.getFromRequest(request);
      
      HtmlTable table = new HtmlTableBuilder<Person>().newBuilder("tableId", persons, request, null)
            .auto(criteria)
            .build();
      
      assertThat(table.getBodyRows()).hasSize(10);
      assertThat(table.getBodyRows().get(0).getColumns()).hasSize(4);
      assertThat(table.getBodyRows().get(0).getColumns().get(0).getContent().toString()).isEqualTo("1");
      assertThat(table.getBodyRows().get(0).getColumns().get(1).getContent().toString()).isEqualTo("firstName1");
      assertThat(table.getBodyRows().get(0).getColumns().get(2).getContent().toString()).isEqualTo("lastName1");
      assertThat(table.getBodyRows().get(0).getColumns().get(3).getContent().toString()).isEqualTo("mail1");
      assertThat(table.getBodyRows().get(9).getColumns().get(0).getContent().toString()).isEqualTo("10");
      assertThat(table.getBodyRows().get(9).getColumns().get(1).getContent().toString()).isEqualTo("firstName10");
      assertThat(table.getBodyRows().get(9).getColumns().get(2).getContent().toString()).isEqualTo("lastName10");
      assertThat(table.getBodyRows().get(9).getColumns().get(3).getContent().toString()).isEqualTo("mail10");
   }
}
