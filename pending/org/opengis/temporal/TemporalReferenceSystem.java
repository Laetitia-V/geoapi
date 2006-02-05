/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source$
 **
 ** Copyright (C) 2005 Open GIS Consortium, Inc.
 ** All Rights Reserved. http://www.opengis.org/legal/
 **
 *************************************************************************************************/
package org.opengis.temporal;

// J2SE direct dependencies
import java.util.Collection;
 
// OpenGIS direct dependencies
import org.opengis.metadata.Identifier;
import org.opengis.metadata.extent.Extent;

// Annotations
import org.opengis.annotation.UML;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Provides information about a temporal reference system.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 *
 * @revisit Maybe it should extends {@link org.opengis.referencing.ReferenceSystem}.
 */
@UML(identifier="TM_ReferenceSystem", specification=ISO_19108)
public interface TemporalReferenceSystem {
    /**
     * Provides a name that uniquely identifies the temporal referece system.
     *
     * RS_Identifier is actually defined in ISO 19115, while ISO 19108 claims that
     * it is defined in ISO 19111.
     */
    @UML(identifier="name", obligation=MANDATORY, specification=ISO_19108)
    Identifier getName();

    /**
     * Identifies the space and time within which the reference system is applicable.
     * The return type allows for describing both spatial and temporal extent.
     * This attribute shall be used whenever an application schema includes
     * {@link TemporalPosition}s referenced to a reference system which has a valid extent
     * that is less than the extent of a data set containing such values.
     */
    @UML(identifier="DomainOfValidity", obligation=OPTIONAL, specification=ISO_19108)
    Collection<Extent> getDomainOfValidity();
}