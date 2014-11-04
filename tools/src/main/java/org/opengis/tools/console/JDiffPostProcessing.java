/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    http://www.geoapi.org
 *
 *    Copyright (C) 2011-2014 Open Geospatial Consortium, Inc.
 *    All Rights Reserved. http://www.opengeospatial.org/ogc/legal
 *
 *    Permission to use, copy, and modify this software and its documentation, with
 *    or without modification, for any purpose and without fee or royalty is hereby
 *    granted, provided that you include the following on ALL copies of the software
 *    and documentation or portions thereof, including modifications, that you make:
 *
 *    1. The full text of this NOTICE in a location viewable to users of the
 *       redistributed or derivative work.
 *    2. Notice of any changes or modifications to the OGC files, including the
 *       date changes were made.
 *
 *    THIS SOFTWARE AND DOCUMENTATION IS PROVIDED "AS IS," AND COPYRIGHT HOLDERS MAKE
 *    NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *    TO, WARRANTIES OF MERCHANTABILITY OR FITNESS FOR ANY PARTICULAR PURPOSE OR THAT
 *    THE USE OF THE SOFTWARE OR DOCUMENTATION WILL NOT INFRINGE ANY THIRD PARTY
 *    PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS.
 *
 *    COPYRIGHT HOLDERS WILL NOT BE LIABLE FOR ANY DIRECT, INDIRECT, SPECIAL OR
 *    CONSEQUENTIAL DAMAGES ARISING OUT OF ANY USE OF THE SOFTWARE OR DOCUMENTATION.
 *
 *    The name and trademarks of copyright holders may NOT be used in advertising or
 *    publicity pertaining to the software without specific, written prior permission.
 *    Title to copyright in this software and any associated documentation will at all
 *    times remain with copyright holders.
 */
package org.opengis.tools.console;

import java.io.*;
import java.util.Locale;


/**
 * Removes the javadoc tags from the XMl files generated by JDiff.
 * The purpose of this changes is to be less distracted by formatting
 * changes, so reader can focus more on content changes.
 *
 * <p>The very naive algorithm implemented in this class just search for {@code <![CDATA[...]]>}
 * sections, then applies the following modifications to their content:</p>
 * <ul>
 *   <li>When a line starting with the {@code '@'} character is found - as in {@code @author},
 *      {@code @see}, <i>etc.</i> -, discard that lines and all remaining lines (because tags
 *      may span many lines).</li>
 *   <li>Remove {@code {@link}}, {@code {@code}} and other inline tags (but keep their content),
 *       in order to not be distracted by URL or font changes.</li>
 *   <li>Remove {@code <blockquote>}, {@code <font>} and a few other selected HTML elements
 *       (but keep their content), in order to not be distracted by formatting changes.</li>
 *   <li>Collapse all whitespaces to a single space character, in order to not be distracted
 *       by space changes.</li>
 *   <li>Put all the javadoc text on a single line. This is needed in order to prevent JDiff
 *       to see documentation changes when there is only line feed changes.</li>
 *   <li>Remove all {@code <pre>} HTML elements <em>and their content</em>, because their
 *       formatting is list as a result of the above operations anyway.</li>
 * </ul>
 *
 * <p><strong>This patch is very ugly. It has been created only because I was too lazy for
 * modifying directly JDiff code.</strong></p>
 *
 * @author Martin Desruisseaux (Geomatys)
 */
public class JDiffPostProcessing {
    /**
     * Encoding used by the XML files generated by JDiff.
     */
    private static final String ENCODING = "ISO-8859-1";

    /**
     * The beginning of comment blocks.
     */
    private static final String BEGIN = "<![CDATA[";

    /**
     * The end of comment blocks.
     */
    private static final String END = "]]>";

    /**
     * Prefix of lines to skip in {@code <![CDATA[...]]>} blocks.
     */
    private static final String TO_SKIP = "@";

    /**
     * HTML elements to remove, in oder to not be distracted by pure formatting changes.
     */
    private static final String[] HTML_TO_REMOVE = {
        "<blockquote>", "</blockquote>",
        "<font",        "</font>",  // No closing ">"
        "<code>",       "</code>",
        "<cite>",       "</cite>",
        "<var>",        "</var>",
        "<em>",         "</em>",
        "<i>",          "</i>",
        "<b>",          "</b>",
        "<ul>",         "</ul>",
        "<li>",         "</li>",
        "<p",           "</p>", // No closing ">"
        "<br>"
    };

    /**
     * Do not allow instantiation of this class.
     */
    private JDiffPostProcessing() {
    }

    /**
     * Removes the javadoc tags from the given XML files.
     *
     * @param  args The XML files to process.
     * @throws IOException If an error occurred while reading or writing a file.
     */
    public static void main(final String[] args) throws IOException {
        for (final String filename : args) {
            final File inFile  = new File(filename);
            final File outFile = new File(filename + ".tmp");
            final BufferedReader in  = new BufferedReader(new InputStreamReader (new FileInputStream ( inFile), ENCODING));
            final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), ENCODING));
            final StringBuilder buffer = new StringBuilder();
            String line;
findBlock:  while ((line = in.readLine()) != null) {
                String trim = line.trim();
                if (trim.startsWith(BEGIN)) {
                    /*
                     * Found a <![CDATA[...]]> block. Copies all lines until the first occurence
                     * of a line begining with "@". During the copy, collapse all kind of spaces
                     * (including line feed) in a single ' ' character.
                     */
                    buffer.setLength(0);
                    while (!trim.startsWith(TO_SKIP)) {
                        boolean needSpace = (buffer.length() != 0);
                        int stop = trim.lastIndexOf(END);
                        if (stop < 0) stop = trim.length();
                        for (int i=0; i<stop; i++) {
                            final char c = trim.charAt(i);
                            if (Character.isSpaceChar(c)) {
                                needSpace = true;
                            } else {
                                if (needSpace) {
                                    buffer.append(' ');
                                    needSpace = false;
                                }
                                buffer.append(c);
                            }
                        }
                        if (stop != trim.length()) {
                            // Found the "]]>" terminating string.
                            out.write(simplify(buffer));
                            out.write(trim.substring(stop));
                            out.newLine();
                            continue findBlock;
                        }
                        line = readLine(in);
                        trim = line.trim();
                    }
                    out.write(simplify(buffer));
                    /*
                     * Found a line begining with "@". Skip all remaining lines until we find
                     * the block terminating string.
                     */
                    int stop;
                    while ((stop = trim.lastIndexOf(END)) < 0) {
                        line = readLine(in);
                        trim = line.trim();
                    }
                    line = trim.substring(stop);
                }
                out.write(line);
                out.newLine();
            }
            out.close();
            in.close();
            if (!inFile.delete()) {
                throw new IOException("Can't delete " + inFile);
            }
            if (!outFile.renameTo(inFile)) {
                throw new IOException("Can't rename " + outFile);
            }
        }
    }

    /**
     * Reads a mandatory line, or throws an {@link EOFException} if we reached the end of file.
     */
    private static String readLine(final BufferedReader in) throws IOException {
        final String line = in.readLine();
        if (line == null) {
            throw new EOFException();
        }
        return line;
    }

    /**
     * Removes the javadoc tags from the given string buffer,
     * in order to simplify it. Then, returns the buffer content.
     */
    private static String simplify(final StringBuilder buffer) {
        // Remove code examples (we lost their formatting anyway).
        int begin = buffer.length();
        while ((begin = buffer.lastIndexOf("<pre>", begin)) >= 0) {
            final int end = buffer.indexOf("</pre>", begin);
            if (end < 0) {
                throw new IllegalArgumentException("Missing closing \"</pre>\"}:\n" + buffer);
            }
            buffer.delete(begin, end+6);
        }
        // Other removals
        for (final String element : HTML_TO_REMOVE) {
            removeHTML(buffer, element);
            removeHTML(buffer, element.toUpperCase(Locale.US));
        }
        removeInlineTag(buffer, false, "{@code", "");
        removeInlineTag(buffer, false, "{@note", " ");
        removeInlineTag(buffer, false, "{@link", " #.");
        removeInlineTag(buffer, true,  "{@value", "#.");
        // Final removal of duplicated spaces.
        begin = buffer.length();
        while ((begin = buffer.lastIndexOf("  ", begin)) >= 0) {
            buffer.delete(begin, begin+1);
        }
        return buffer.toString();
    }

    /**
     * Removes a the buffer portion corresponding to the given tag, keeping the value inside the
     * tag.
     *
     * @param buffer     The buffer from which to remove the inline tags.
     * @param tag        The inline tag to remove, with heading '{' but without trailing '}'.
     * @param separators If one of those characters is found, keep only the text on the right side.
     */
    private static void removeInlineTag(final StringBuilder buffer, final boolean canBeStandalone,
            final String tag, final String separators)
    {
        int beginTag = buffer.length();
        while ((beginTag = buffer.lastIndexOf(tag, beginTag-1)) >= 0) {
            final int end = buffer.indexOf("}", beginTag);
            if (end < 0) {
                throw new IllegalArgumentException("Missing closing '}' in " + tag + "}:\n" + buffer);
            }
            int beginValue = buffer.indexOf(" ", beginTag);
            if (beginValue < 0 || beginValue >= end) {
                if (canBeStandalone) {
                    continue;
                }
                throw new IllegalArgumentException("Missing space in " + tag + "}:\n" + buffer);
            }
            beginValue++;
            for (int i=beginValue; i<end; i++) {
                final char c = buffer.charAt(i);
                if (separators.indexOf(c) >= 0) {
                    beginValue = i+1;
                    if (c == ' ') {
                        // Retains the first occurrence of space,
                        // and the last occurrence of everything else.
                        break;
                    }
                }
            }
            buffer.delete(end, end+1);
            buffer.delete(beginTag, beginValue);
        }
    }

    /**
     * Removes an HTML element.
     *
     * @param buffer  The buffer from which to remove elements.
     * @param element The HTML element to remove. Can be without trailing {@code '>'}.
     */
    private static void removeHTML(final StringBuilder buffer, final String element) {
        int begin = buffer.length();
        while ((begin = buffer.lastIndexOf(element, begin)) >= 0) {
            final int end = buffer.indexOf(">", begin);
            if (end < 0) {
                throw new IllegalArgumentException("Missing closing '>' after " + element + ":\n" + buffer);
            }
            buffer.delete(begin, end+1);
        }
    }
}
