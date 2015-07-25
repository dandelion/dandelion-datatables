/*
 * [The "BSD licence"]
 * Copyright (c) 2012 Dandelion
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.ajax.ColumnDef;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.DatatableOptions;

/**
 * <p>
 * Builder used to create instances of {@link HtmlTable}. This builder is mainly
 * used as an export utility and for testing.
 * </p>
 * <p>
 * For example, considering the following simple {@code Person} class:
 * </p>
 * 
 * <pre>
 * public class Person {
 *    private Long id;
 *    private String firstName;
 *    private String lastName;
 *    private String mail;
 *    private Date birthDate;
 * 
 *    // Accessors...
 * }
 * </pre>
 * <p>
 * The builder allows to create fully configured instance of {@link HtmlTable}
 * as follows:
 * </p>
 * 
 * <pre>
 * HtmlTable table = new HtmlTableBuilder&lt;Person&gt;().newBuilder(&quot;yourTableId&quot;, persons, request).column()
 *       .fillWithProperty(&quot;id&quot;).title(&quot;Id&quot;).column().fillWithProperty(&quot;firstName&quot;).title(&quot;Firtname&quot;).column()
 *       .fillWithProperty(&quot;lastName&quot;).title(&quot;Lastname&quot;).column().fillWithProperty(&quot;mail&quot;).title(&quot;Mail&quot;).column()
 *       .fillWithProperty(&quot;birthDate&quot;, &quot;{0,date,dd-MM-yyyy}&quot;).title(&quot;BirthDate&quot;).build();
 * </pre>
 * <p>
 * where:
 * </p>
 * <ul>
 * <li>{@code yourTableId} is the HTML id that has be assigned to the
 * {@code table} tag</li>
 * <li>{@code persons} is a collection of {@code Person}</li>
 * <li>{@code request} is the current {@link HttpServletRequest}</li>
 * </ul>
 * 
 * @param <T>
 *           Type of the data used to build the table.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class HtmlTableBuilder<T> {

   private static Logger logger = LoggerFactory.getLogger(HtmlTableBuilder.class);

   public ColumnStep newBuilder(String id, List<T> data, HttpServletRequest request) {
      return new Steps<T>(id, data, request);
   }

   public ColumnStep newBuilder(String id, List<T> data, HttpServletRequest request, ExportConf exportConf) {
      return new Steps<T>(id, data, request, exportConf);
   }

   public static interface ColumnStep {

      /**
       * <p>
       * Starts the definition of a new column in the exported file.
       * </p>
       */
      FirstContentStep column();

      /**
       * <p>
       * Automatically builds the instance using all {@link ColumnDef} present
       * in the passed {@code criteria}.
       * </p>
       * <p>
       * All columns will be rendered as-is, i.e. with no formatting. All titles
       * will be computed from the capitalized property used to extract value
       * from the collection.
       * </p>
       * <p>
       * If you need a more fine-grained rendering, use the
       * {@link FirstContentStep#fillFromCriteria(DatatablesCriterias)} or
       * {@link FirstContentStep#fillFromCriteria(DatatablesCriterias, String)}
       * instead.
       * </p>
       * 
       * @param criteria
       *           All criteria sent by DataTable.
       * @since 1.1.0
       */
      BuildStep auto(DatatablesCriterias criteria);
   }

   public static interface FirstContentStep {

      /**
       * <p>
       * Fills the current column with the property extracted from the
       * collection passed in the builder.
       * </p>
       * 
       * @param propertyName
       *           Name of the property to be extracted from the collection.
       */
      SecondContentStep fillWithProperty(String propertyName);

      /**
       * <p>
       * Fills the current column with the property extracted from the
       * collection passed in the builder.
       * </p>
       * <p>
       * The extracted value is then formatted using the passed {@code pattern}
       * with a {@link MessageFormat}.
       * </p>
       * 
       * @param propertyName
       *           Name of the property to be extracted from the collection.
       * @param pattern
       *           Pattern to be used by the {@link MessageFormat} to format the
       *           returned value.
       */
      SecondContentStep fillWithProperty(String propertyName, String pattern);

      /**
       * <p>
       * Fills the current column with the property extracted from the
       * collection passed in the builder.
       * </p>
       * <p>
       * The extracted value is then formatted using the passed {@code pattern}
       * with a {@link MessageFormat}.
       * </p>
       * 
       * @param propertyName
       *           Name of the property to be extracted from the collection.
       * @param pattern
       *           Pattern to be used by the {@link MessageFormat} to format the
       *           returned value.
       * @param defaultContent
       *           The contents to render if the extrated value is null.
       */
      SecondContentStep fillWithProperty(String propertyName, String pattern, String defaultContent);

      /**
       * <p>
       * Simply fills the current column with the passed {@code content}.
       * </p>
       * 
       * @param content
       *           The string to be used in the column.
       */
      SecondContentStep fillWith(String content);

      /**
       * <p>
       * Fills the current column using the corresponding {@link ColumnDef}
       * instance of the passed {@code criterias}.
       * </p>
       * <p>
       * As soon as this method is invoked, an internal counter is incremented.
       * The increment is used to directly access the list of {@link ColumnDef}
       * of {@link DatatablesCriterias}.
       * </p>
       * <p>
       * <b>WARNING:</b> don't call this method more times than the number of
       * columns registered.
       * </p>
       * 
       * @param criteria
       *           All criteria sent by DataTable.
       * @since 1.1.0
       */
      SecondContentStep fillFromCriteria(DatatablesCriterias criteria);

      /**
       * <p>
       * Fills the current column using the corresponding {@link ColumnDef}
       * instance of the passed {@code criterias}.
       * </p>
       * <p>
       * The extracted value is then formatted using the passed {@code pattern}
       * with a {@link MessageFormat}.
       * </p>
       * <p>
       * As soon as this method is invoked, an internal counter is incremented.
       * The increment is used to directly access the list of {@link ColumnDef}
       * of {@link DatatablesCriterias}.
       * </p>
       * <p>
       * <b>WARNING:</b> don't call this method more times than the number of
       * columns registered.
       * </p>
       * 
       * @param criteria
       *           All criteria sent by DataTable.
       * @param pattern
       *           Pattern to be used by the {@link MessageFormat} to format the
       *           returned value.
       * @since 1.1.0
       */
      SecondContentStep fillFromCriteria(DatatablesCriterias criteria, String pattern);
   }

   public static interface SecondContentStep {

      /**
       * @see {@link FirstContentStep#fillWithProperty(String)}
       */
      SecondContentStep andProperty(String propertyName);

      /**
       * @see {@link FirstContentStep#fillWithProperty(String, String)}
       */
      SecondContentStep andProperty(String propertyName, String pattern);

      /**
       * @see {@link FirstContentStep#fillWithProperty(String, String, String)}
       */
      SecondContentStep andProperty(String propertyName, String pattern, String defaultContent);

      /**
       * @see {@link FirstContentStep#fillWith(String)}
       */
      SecondContentStep and(String content);

      /**
       * <p>
       * Sets the title of the current column.
       * </p>
       * 
       * @param title
       *           Title to be used in the exported file.
       */
      BuildStep title(String title);
   }

   public static interface TitleStep extends ColumnStep {

      /**
       * <p>
       * Sets the title of the current column.
       * </p>
       * 
       * @param title
       *           Title to be used in the exported file.
       */
      ColumnStep title(String title);
   }

   public static interface BuildStep {

      /**
       * <p>
       * Finalizes the build of the {@link HtmlTable}.
       * </p>
       * 
       * @return the instance of {@link HtmlTable}.
       */
      HtmlTable build();

      FirstContentStep column();
   }

   private static class Steps<T> implements ColumnStep, FirstContentStep, SecondContentStep, BuildStep {

      private String id;
      private List<T> data;
      private LinkedList<HtmlColumn> headerColumns = new LinkedList<HtmlColumn>();
      private HttpServletRequest request;
      private HttpServletResponse response;
      private ExportConf exportConf;
      private int columnInvocationCount = 0;

      public Steps(String id, List<T> data, HttpServletRequest request) {
         this(id, data, request, null);
      }

      public Steps(String id, List<T> data, HttpServletRequest request, ExportConf exportConf) {
         this.id = id;
         this.data = data;
         this.request = request;
         this.exportConf = new ExportConf(request);
         if (exportConf != null) {
            this.exportConf.mergeWith(exportConf);
         }
      }

      public Steps<T> column() {
         HtmlColumn column = new HtmlColumn(true, "");
         headerColumns.add(column);
         return this;
      }

      public Steps<T> title(String title) {
         headerColumns.getLast().getColumnConfiguration().getOptions().put(DatatableOptions.TITLE, title);
         return this;
      }

      /**
       * Add a new column to the table and complete it using the passed
       * property. Convenient if you need to display a single property in the
       * column.
       * 
       * @param property
       *           name of the (possibly nested) property of the bean which is
       *           part of the collection being iterated on.
       */
      public Steps<T> fillWithProperty(String property) {
         return fillWithProperty(property, null, "");
      }

      public Steps<T> fillWithProperty(String property, String pattern) {
         return fillWithProperty(property, pattern, "");
      }

      public Steps<T> fillFromCriteria(DatatablesCriterias criteria) {
         return fillWithProperty(criteria.getColumnDefs().get(columnInvocationCount).getName(), null, "");
      }

      public Steps<T> fillFromCriteria(DatatablesCriterias criteria, String pattern) {
         return fillWithProperty(criteria.getColumnDefs().get(columnInvocationCount).getName(), pattern, "");
      }

      public Steps<T> fillWithProperty(String property, String pattern, String defaultContent) {
         if (headerColumns.getLast().getColumnConfiguration().getColumnElements() == null) {
            headerColumns.getLast().getColumnConfiguration().setColumnElements(new ArrayList<ColumnElement>());
         }
         headerColumns.getLast().getColumnConfiguration().getColumnElements()
               .add(new ColumnElement(property, pattern, "", defaultContent));
         columnInvocationCount++;
         return this;
      }

      public Steps<T> fillWith(String content) {
         if (headerColumns.getLast().getColumnConfiguration().getColumnElements() == null) {
            headerColumns.getLast().getColumnConfiguration().setColumnElements(new ArrayList<ColumnElement>());
         }
         headerColumns.getLast().getColumnConfiguration().getColumnElements()
               .add(new ColumnElement(null, null, content, null));
         columnInvocationCount++;
         return this;
      }

      public Steps<T> andProperty(String property) {
         return andProperty(property, null, "");
      }

      public Steps<T> andProperty(String property, String pattern) {
         return andProperty(property, pattern, "");
      }

      public Steps<T> andProperty(String property, String pattern, String defaultContent) {
         if (headerColumns.getLast().getColumnConfiguration().getColumnElements() == null) {
            headerColumns.getLast().getColumnConfiguration().setColumnElements(new ArrayList<ColumnElement>());
         }
         headerColumns.getLast().getColumnConfiguration().getColumnElements()
               .add(new ColumnElement(property, pattern, null, defaultContent));

         return this;
      }

      public Steps<T> and(String content) {
         if (headerColumns.getLast().getColumnConfiguration().getColumnElements() == null) {
            headerColumns.getLast().getColumnConfiguration().setColumnElements(new ArrayList<ColumnElement>());
         }
         headerColumns.getLast().getColumnConfiguration().getColumnElements()
               .add(new ColumnElement(null, null, content, null));
         return this;
      }

      public HtmlTable build() {
         HtmlTable table = new HtmlTable(id, request, response);

         table.getTableConfiguration().getExportConfigurations().put(exportConf.getFormat(), exportConf);

         if (data != null && data.size() > 0) {
            DatatableOptions.INTERNAL_OBJECTTYPE.setIn(table.getTableConfiguration().getOptions(),
                  data.get(0).getClass().getSimpleName());
         }
         else {
            DatatableOptions.INTERNAL_OBJECTTYPE.setIn(table.getTableConfiguration().getOptions(), "???");
         }

         table.addHeaderRow();

         for (HtmlColumn column : headerColumns) {
            String title = DatatableOptions.TITLE.valueFrom(column.getColumnConfiguration().getOptions());
            if (StringUtils.isNotBlank(title)) {
               column.setContent(new StringBuilder(title));
            }
            else {
               column.setContent(new StringBuilder(""));
            }
            table.getLastHeaderRow().addColumn(column);
         }

         if (data != null) {

            for (T o : data) {

               table.addRow();
               for (HtmlColumn column : headerColumns) {

                  String content = "";
                  for (ColumnElement columnElement : column.getColumnConfiguration().getColumnElements()) {

                     if (StringUtils.isNotBlank(columnElement.getPropertyName())) {
                        try {
                           Object tmpObject = PropertyUtils.getNestedProperty(o,
                                 columnElement.getPropertyName().trim());

                           if (StringUtils.isNotBlank(columnElement.getPattern())) {
                              MessageFormat messageFormat = new MessageFormat(columnElement.getPattern());
                              content += messageFormat.format(new Object[] { tmpObject });
                           }
                           else {
                              content += String.valueOf(tmpObject);
                           }
                        }
                        catch (Exception e) {
                           logger.warn(
                                 "Something went wrong with the property {}. Check that an accessor method for this property exists in the bean.");
                           content += columnElement.getDefaultValue();
                        }
                     }
                     else if (columnElement.getContent() != null) {
                        content += columnElement.getContent();
                     }
                     else {
                        content += columnElement.getDefaultValue();
                     }
                  }

                  table.getLastBodyRow().addColumn(String.valueOf(content));
               }
            }
         }

         return table;
      }

      @Override
      public BuildStep auto(DatatablesCriterias criteria) {
         for (ColumnDef columnDef : criteria.getColumnDefs()) {
            HtmlColumn headerColumn = new HtmlColumn(true, "");
            headerColumn.getColumnConfiguration().getOptions().put(DatatableOptions.TITLE,
                  StringUtils.capitalize(columnDef.getName()));
            headerColumn.getColumnConfiguration().setColumnElements(new ArrayList<ColumnElement>());
            headerColumn.getColumnConfiguration().getColumnElements()
                  .add(new ColumnElement(columnDef.getName(), null, null, null));
            headerColumns.add(headerColumn);
         }
         return this;
      }
   }
}