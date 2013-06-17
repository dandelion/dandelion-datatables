package com.github.dandelion.datatables.core.util;

import java.util.Iterator;
import java.util.Random;

public class StringUtils {

	private static final String NUMERIC = "0123456789";
	private static Random RANDOM = new Random();
	public static final String EMPTY = "";

	/**
	 * <p>
	 * Checks if a String is whitespace, empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 * @since 2.0
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if a String is not empty (""), not null and not whitespace only.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.isNotBlank(null)      = false
	 * StringUtils.isNotBlank("")        = false
	 * StringUtils.isNotBlank(" ")       = false
	 * StringUtils.isNotBlank("bob")     = true
	 * StringUtils.isNotBlank("  bob  ") = true
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is not empty and not null and not
	 *         whitespace
	 * @since 2.0
	 */
	public static boolean isNotBlank(String str) {
		return !StringUtils.isBlank(str);
	}

	/**
	 * <p>
	 * Capitalizes a String changing the first letter to title case as per
	 * {@link Character#toTitleCase(char)}. No other letters are changed.
	 * </p>
	 * 
	 * <p>
	 * For a word based algorithm, see {@link WordUtils#capitalize(String)}. A
	 * <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.capitalize(null)  = null
	 * StringUtils.capitalize("")    = ""
	 * StringUtils.capitalize("cat") = "Cat"
	 * StringUtils.capitalize("cAt") = "CAt"
	 * </pre>
	 * 
	 * @param str
	 *            the String to capitalize, may be null
	 * @return the capitalized String, <code>null</code> if null String input
	 * @see WordUtils#capitalize(String)
	 * @see #uncapitalize(String)
	 * @since 2.0
	 */
	public static String capitalize(String str) {
		if (str == null) {
			return null;
		}
		StringBuilder result = new StringBuilder(str.toString());
		if (result.length() > 0) {
			result.setCharAt(0, Character.toTitleCase(result.charAt(0)));
		}
		return result.toString();

	}

	/**
	 * <p>
	 * Uncapitalizes a String changing the first letter to title case as per
	 * {@link Character#toLowerCase(char)}. No other letters are changed.
	 * </p>
	 * 
	 * <p>
	 * For a word based algorithm, see {@link WordUtils#uncapitalize(String)}. A
	 * <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.uncapitalize(null)  = null
	 * StringUtils.uncapitalize("")    = ""
	 * StringUtils.uncapitalize("Cat") = "cat"
	 * StringUtils.uncapitalize("CAT") = "cAT"
	 * </pre>
	 * 
	 * @param str
	 *            the String to uncapitalize, may be null
	 * @return the uncapitalized String, <code>null</code> if null String input
	 * @see WordUtils#uncapitalize(String)
	 * @see #capitalize(String)
	 * @since 2.0
	 */
	public static String uncapitalize(String str) {
		if (str == null) {
			return null;
		}
		StringBuilder result = new StringBuilder(str);

		if (result.length() > 0) {
			result.setCharAt(0, Character.toLowerCase(result.charAt(0)));
		}

		return result.toString();
	}

	// Joining
	// -----------------------------------------------------------------------

	public static String join(Object[] target, String separator) {

		final StringBuilder sb = new StringBuilder();
		if (target.length > 0) {
			sb.append(target[0]);
			for (int i = 1; i < target.length; i++) {
				sb.append(separator);
				sb.append(target[i]);
			}
		}
		return sb.toString();
	}

	public static String join(Iterable<?> target, String separator) {

		StringBuilder sb = new StringBuilder();
		Iterator<?> it = target.iterator();
		if (it.hasNext()) {
			sb.append(it.next());
			while (it.hasNext()) {
				sb.append(separator);
				sb.append(it.next());
			}
		}
		return sb.toString();

	}

	public static String randomNumeric(int count) {
		StringBuilder strBuilder = new StringBuilder(count);
		int anLen = NUMERIC.length();
		synchronized (RANDOM) {
			for (int i = 0; i < count; i++) {
				strBuilder.append(NUMERIC.charAt(RANDOM.nextInt(anLen)));
			}
		}
		return strBuilder.toString();
	}
}