/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    http://www.geoapi.org
 *
 *    Copyright (C) 2007-2018 Open Geospatial Consortium, Inc.
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
package org.opengis.filter.capability;

import java.util.Collection;

/**
 * Supported functions in a capabilities document.
 *
 * <pre>
 *  &lt;xsd:complexType name="FunctionsType"&lt;
 *    &lt;xsd:sequence&lt;
 *       &lt;xsd:element name="FunctionNames" type="ogc:FunctionNamesType"/&lt;
 *    &lt;/xsd:sequence&lt;
 *  &lt;/xsd:complexType&lt;
 * </pre>
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface Functions {
    /**
     * Provided functions.
     *
     * <pre>
     * &lt;xsd:element name="FunctionNames" type="ogc:FunctionNamesType"/&lt;
     * </pre>
     */
    Collection<FunctionName> getFunctionNames();

    /**
     * Looks up a function by name, returning null if no such function is found.
     *
     * @param  name  the name of the function.
     *
     * @return a function, or null.
     */
    FunctionName getFunctionName( String name );
}
