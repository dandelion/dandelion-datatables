package com.github.dandelion.datatables.integration;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.github.dandelion.datatables.core.ajax.ColumnDef;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.utils.Mock;
import com.github.dandelion.datatables.utils.Person;

public class MockService {

	public DataSet<Person> getData(DatatablesCriterias criterias) {

		List<Person> finalList = new LinkedList<Person>();

		// Total records =
		List<Person> persons = Mock.persons;

		Long totalDisplayRecords = null;

		/**
		 * Filtering
		 */
		System.out.println("criterias.getSearch() = " + criterias.getSearch());
		if (criterias.getSearch() != null && !"".equals(criterias.getSearch())) {
			for (Person person : persons) {
				if (person.getLastName().toLowerCase()
						.contains(criterias.getSearch().toLowerCase())) {
					finalList.add(person);
				}
			}
		} else {
			finalList.addAll(persons);
		}

		totalDisplayRecords = Long.parseLong(String.valueOf(finalList.size()));

		/**
		 * Sorting
		 */
		if (criterias.hasOneSortedColumn()) {
			for (final ColumnDef columnDef : criterias.getSortingColumnDefs()) {
				Collections.sort(finalList, new Comparator<Person>() {

					@Override
					public int compare(Person c1, Person c2) {
						int sortDirection = columnDef.getSortDirection().equals("asc") ? -1 : 1;
						Object field1 = null;
						Object field2 = null;

						try {
							field1 = PropertyUtils.getNestedProperty(c1, columnDef.getName());
							field2 = PropertyUtils.getNestedProperty(c2, columnDef.getName());
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (field1 instanceof String) {
							return ((String) field1).compareTo((String) field2) * sortDirection;
						} else if (field1 instanceof Long) {
							return ((Long) field1).compareTo((Long) field2) * sortDirection;
						}
						return 0;
					}
				});
			}
		}

		/**
		 * Paging
		 */
		if (finalList.size() < criterias.getDisplayStart() + criterias.getDisplaySize()) {
			finalList = finalList.subList(criterias.getDisplayStart(), finalList.size());
		} else {
			finalList = finalList.subList(criterias.getDisplayStart(), criterias.getDisplayStart()
					+ criterias.getDisplaySize());
		}

		System.out.println("totalRecords = " + Long.parseLong(String.valueOf(persons.size())));
		System.out.println("totalFilteredRecords = "
				+ Long.parseLong(String.valueOf(finalList.size())));
		// ResultSet<Person> resultSet = new ResultSet<Person>(finalList, pers,
		// totalDisplayRecords)
		return new DataSet<Person>(finalList, Long.parseLong(String.valueOf(persons.size())),
				totalDisplayRecords);
	}
}
