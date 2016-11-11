/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    http://www.geoapi.org
 *
 *    Copyright (C) 2004-2016 Open Geospatial Consortium, Inc.
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
package org.opengis.referencing.crs;

import java.util.Map;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A 2- or 3-dimensional coordinate reference system based on an ellipsoidal approximation of the geoid.
 * This provides an accurate representation of the geometry of geographic features for a large
 * portion of the earth's surface.
 *
 * <p>A Geographic CRS is not suitable for mapmaking on a planar surface, because it describes geometry
 * on a curved surface. It is impossible to represent such geometry in a Euclidean plane without
 * introducing distortions. The need to control these distortions has given rise to the development
 * of the science of {@linkplain org.opengis.referencing.operation.Projection map projections}.</p>
 *
 * <p>This type of CRS can be used with coordinate systems of type
 * {@link org.opengis.referencing.cs.EllipsoidalCS}.</p>
 *
 * @departure constraint
 *   This interface is kept conformant with the specification published in 2003. The 2007 revision
 *   of ISO 19111 removed the <code>SC_GeographicCRS</code> and <code>SC_GeocentricCRS</code> types,
 *   handling both using the <code>SC_GeodeticCRS</code> parent type. GeoAPI keeps them for two reasons:
 *   <ul>
 *     <li>The distinction between those two types is in wide use.</li>
 *     <li>A distinct geographic type allows GeoAPI to restrict the coordinate system type to <code>EllipsoidalCS</code>.
 *         ISO 19111 uses a <code>union</code> for expressing this restriction at the <code>SC_GeodeticCRS</code> level,
 *         but the Java language does not provide such construct. A distinct geographic type is one way to achieve the
 *         same goal.</li>
 *   </ul>
 *
 * @author  Martin Desruisseaux (IRD)
 * @version 3.0
 * @since   1.0
 *
 * @see CRSAuthorityFactory#createGeographicCRS(String)
 * @see CRSFactory#createGeographicCRS(Map, GeodeticDatum, EllipsoidalCS)
 */
@UML(identifier="SC_GeographicCRS", specification=ISO_19111, version=2003)
public interface GeographicCRS extends GeodeticCRS {
    /**
     * Returns the coordinate system, which shall be ellipsoidal.
     *
     * @return the ellipsoidal coordinate system.
     */
    @Override
    @UML(identifier="coordinateSystem", obligation=MANDATORY, specification=ISO_19111)
    EllipsoidalCS getCoordinateSystem();
}
