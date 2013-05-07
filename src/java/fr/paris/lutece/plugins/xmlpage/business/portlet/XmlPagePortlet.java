/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
package fr.paris.lutece.plugins.xmlpage.business.portlet;

import fr.paris.lutece.plugins.xmlpage.service.XmlPageElement;
import fr.paris.lutece.plugins.xmlpage.service.XmlPageService;
import fr.paris.lutece.plugins.xmlpage.util.XmlPageContentUtils;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents business objects RssPortlet
 */
public class XmlPagePortlet extends Portlet
{
    //properties
    private static final String PROPERTY_TRANSFORM_CACHE = "xmlpage.transform.cache";
    private static final String PROPERTY_PARAMETERS_LIST = "xmlpage.parameters.list";
    private static final String PROPERTY_PARAMETERS_LIST_SEPARATOR = "xmlpage.parameters.list.separator";

    //Utils
    private static final String TAG_PORTLET_XMLPAGE = "xmlpage-portlet";
    private static final String TAG_PORTLET_XMLPAGE_CONTENT = "xmlpage-portlet-content";
    private static final String EMPTY_STRING = "";
    private String _strPageName;
    private String _strStyle;

    /**
     * Sets the identifier of the portlet type to value specified
     */
    public XmlPagePortlet(  )
    {
        setPortletTypeId( XmlPagePortletHome.getInstance(  ).getPortletTypeId(  ) );
    }

    /**
     * Sets the page's name of the portlet
     *
     * @param strPageName the XmlPage name portlet
     */
    public void setPageName( String strPageName )
    {
        _strPageName = strPageName;
    }

    /**
     * Returns the page's name of the XmlPage portlet
     *
     * @return the page's name
     */
    public String getPageName(  )
    {
        return _strPageName;
    }

    /**
     * Sets the style of the portlet
     *
     * @param strStyle the XmlPage style portlet
     */
    public void setStyle( String strStyle )
    {
        _strStyle = strStyle;
    }

    /**
     * Returns the style of the XmlPage portlet
     *
     * @return the style
     */
    public String getStyle(  )
    {
        return _strStyle;
    }

    /**
     * Returns the Xml code of the portlet with XML heading
     *
     * @param request The HTTP servlet request
     * @return the Xml code of the portlet
     */
    public String getXmlDocument( HttpServletRequest request )
    {
        return XmlUtil.getXmlHeader(  ) + getXml( request );
    }

    /**
     * Returns the Xml code of the portlet without XML heading
     *
     * @param request The HTTP servlet request
     * @return the Xml code of the portlet content
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer strXml = new StringBuffer(  );

        String strTransformCache = AppPropertiesService.getProperty( PROPERTY_TRANSFORM_CACHE );
        boolean bTransformCacheActive = Boolean.parseBoolean( strTransformCache );
        Map<String, String> mapParameters = null;

        if ( !bTransformCacheActive )
        {
            String strParametersList = AppPropertiesService.getProperty( PROPERTY_PARAMETERS_LIST );
            String strParametersListSeparator = AppPropertiesService.getProperty( PROPERTY_PARAMETERS_LIST_SEPARATOR );
            String[] listParameters = strParametersList.split( strParametersListSeparator );

            mapParameters = new HashMap<String, String>(  );

            if ( request != null )
            {
                for ( String currentParameter : listParameters )
                {
                    if ( request.getParameter( currentParameter ) != null )
                    {
                        mapParameters.put( currentParameter, request.getParameter( currentParameter ) );
                    }
                }
            }
        }

        XmlUtil.beginElement( strXml, TAG_PORTLET_XMLPAGE );

        XmlPageElement xmlPageElement = XmlPageService.getInstance(  ).getXmlPageResource( getPageName(  ) );

        String strContent = EMPTY_STRING;

        if ( xmlPageElement != null )
        {
            //TODO add boolean active transform hashMap de parameters for portlet ?
            strContent = XmlPageContentUtils.getContent( xmlPageElement, getStyle(  ), bTransformCacheActive,
                    mapParameters );
        }

        XmlUtil.addElementHtml( strXml, TAG_PORTLET_XMLPAGE_CONTENT, strContent );
        XmlUtil.endElement( strXml, TAG_PORTLET_XMLPAGE );

        return addPortletTags( strXml );
    }

    /**
     * Updates the current instance of the Portlet object
     */
    public void update(  )
    {
        XmlPagePortletHome.getInstance(  ).update( this );
    }

    /**
     * Removes the current instance of the Portlet object
     */
    public void remove(  )
    {
        XmlPagePortletHome.getInstance(  ).remove( this );
    }
}
