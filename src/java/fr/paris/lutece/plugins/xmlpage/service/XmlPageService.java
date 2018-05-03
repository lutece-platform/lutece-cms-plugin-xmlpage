/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.xmlpage.service;

import fr.paris.lutece.portal.service.resource.ResourceService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


/**
 * The XmlPage implementation of ResourceService
 */
public final class XmlPageService extends ResourceService
{
    private static final String PROPERTY_LOADER = "xmlpage.service.loaders";
    private static final String PROPERTY_NAME = "xmlpage.service.name";
    private static final String PROPERTY_CACHE = "xmlpage.service.cache";
    private static final String PARAMETER_VALUE_SEPARATOR = "-";
    private static XmlPageService _singleton = new XmlPageService(  );
    private static final Comparator<ReferenceItem> COMPARATOR_TRI_ASC = new Comparator<ReferenceItem>(  )
        {
            public int compare( ReferenceItem item1, ReferenceItem item2 )
            {
                int nOrder = item1.getName(  ).compareTo( item2.getName(  ) );

                return nOrder;
            }
        };

    /**
     * Creates a new instance of XmlPageService
     */
    private XmlPageService(  )
    {
        setNameKey( PROPERTY_NAME );
        setCacheKey( PROPERTY_CACHE );
    }

    /**
     * Returns the instance of the singleton
     * @return The instance of the singleton
     */
    public static XmlPageService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Returns the property key that contains the loaders list
     * @return A property key
     */
    protected String getLoadersProperty(  )
    {
        return PROPERTY_LOADER;
    }

    /**
     * Returns the XmlPageElement
     * @param strPageName The XmlPageElement name
     * @return the XmlPageElement
     */
    public XmlPageElement getXmlPageResource( String strPageName )
    {
        XmlPageElement xmlPageElement = (XmlPageElement) getResource( strPageName );

        return xmlPageElement;
    }

    /**
     * Returns a list of different XmlPage.
     * The result list is a reference list of all the (xmlpage, style) available.
     * @return The list of all Xmlpage
     */
    public ReferenceList getXmlPageList(  )
    {
        ReferenceList list = new ReferenceList(  );
        Collection colXmlPages = getResources(  );
        Iterator iColXmlPages = colXmlPages.iterator(  );

        while ( iColXmlPages.hasNext(  ) )
        {
            XmlPageElement xmlPageElement = (XmlPageElement) iColXmlPages.next(  );

            if ( xmlPageElement.getListXslContent(  ) != null )
            {
                Iterator iXslElement = xmlPageElement.getListXslContent(  ).keySet(  ).iterator(  );

                while ( iXslElement.hasNext(  ) )
                {
                    String strStyle = (String) iXslElement.next(  );
                    list.addItem( xmlPageElement.getName(  ).concat( PARAMETER_VALUE_SEPARATOR ).concat( strStyle ),
                        xmlPageElement.getTitle(  ).concat( " (" ).concat( strStyle ).concat( ")" ) );
                }
            }
        }

        // Ordering the list
        Collections.sort( list, COMPARATOR_TRI_ASC );

        return list;
    }
}
